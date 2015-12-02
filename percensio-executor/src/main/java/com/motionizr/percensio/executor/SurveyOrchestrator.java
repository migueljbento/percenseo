/*
 * The MIT License (MIT)
 *
 * Copyright © 2015 Miguel José Carvalho Bento (migueljbento@gmail.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.motionizr.percensio.executor;

import com.feedzai.commons.sql.abstraction.dml.result.ResultColumn;
import com.feedzai.commons.sql.abstraction.engine.DatabaseEngine;
import com.feedzai.commons.sql.abstraction.engine.DatabaseEngineException;
import com.feedzai.commons.sql.abstraction.engine.DatabaseFactoryException;
import com.motionizr.percensio.commons.CallResult;
import com.motionizr.percensio.commons.CallStatus;
import com.motionizr.percensio.commons.DatabaseHelper;
import com.motionizr.percensio.commons.SurveyEntities;
import com.motionizr.percensio.executor.configuration.SurveyConfiguration;
import com.twilio.sdk.TwilioRestException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.feedzai.commons.sql.abstraction.dml.dialect.SqlBuilder.*;

/**
 * Orchestrates the survey process by queueing the necessary calls.
 * </p>
 * Supports multiple runs. Makes sure that survey members that were successfully contacted in previous runs are not contacted again.
 * Does this by consulting previous call results in the database.
 *
 * @author Miguel Bento (migueljbento@gmail.com)
 * @version 1.0.0
 */
public class SurveyOrchestrator {

    /**
     * The logger.
     */
    private static Logger logger = LoggerFactory.getLogger(SurveyOrchestrator.class);

    /**
     * The {@link com.motionizr.percensio.executor.configuration.SurveyConfiguration survey configuration}.
     */
    private SurveyConfiguration configuration;

    /**
     * The {@link Dialer} used to queue the calls.
     */
    private Dialer dialer;

    /**
     * The database connection.
     */
    private DatabaseEngine engine;

    /**
     * Creates a new instance of {@link SurveyOrchestrator}.
     *
     * @param configuration     The {@link com.motionizr.percensio.executor.configuration.SurveyConfiguration survey configuration}.
     */
    public SurveyOrchestrator(SurveyConfiguration configuration) {
        this.configuration = configuration;
        this.dialer = new Dialer(configuration);
    }

    /**
     * Executes the survey.
     * </p>
     * Queues all the calls and gathers feedback over what calls were successfully queued and which failed.
     *
     * @return  A stream containing the {@link CallResult result} of the calls.
     */
    public Stream<CallResult> execute() {
        logger.info("Starting the survey.");

        try {
            engine = DatabaseHelper.initializeDbConnection(configuration.getDatabaseFile());
            logger.debug("Database connection initialized");

            final List<String> callsAlreadyCompleted = getCallsAlreadyCompleted();
            logger.debug("Got {} calls made previously.", callsAlreadyCompleted.size());

            Stream<String> surveyNumbers = getInputSurveyNumbers();
            logger.debug("Got submitted survey numbers.");

            logger.debug("Queueing the phone calls");
            Stream<CallResult> results = queuePhoneCalls(surveyNumbers, callsAlreadyCompleted);

            Map<CallStatus, Long> groupedResults = results.collect(Collectors.groupingByConcurrent(CallResult::getStatus, Collectors.counting()));
            logger.info("Successfully queued {} phone calls. There were {} failures.",
                    groupedResults.getOrDefault(CallStatus.QUEUED, 0L),
                    groupedResults.getOrDefault(CallStatus.FAILED, 0L)
            );

            DatabaseHelper.closeDbConnection(engine);
            logger.debug("Database connection closed");

            logger.info("Survey ended.");
            return results;
        } catch (IOException e) {
            logger.error("An exception occurred trying to read the numbers CSV.", e);
        } catch (DatabaseEngineException e) {
            logger.error("An exception occurred trying to execute a database query.", e);
        } catch (DatabaseFactoryException e) {
            logger.error("An exception occurred trying to initialize the database.", e);
        }

        return Stream.empty();
    }

    /**
     * Generates a stream of the numbers submitted to the survey.
     */
    private Stream<String> getInputSurveyNumbers() throws IOException{
        if (!configuration.isFileBased()) {
            return configuration.getNumbers().stream();
        } else {
            try (CSVParser parser = CSVParser.parse(new File(configuration.getNumbersCSV()), Charset.defaultCharset(), CSVFormat.EXCEL)) {
                return parser.getRecords().stream()
                        .map(record -> record.get(0));
            }
        }
    }

    /**
     * Fetches the list of calls that were previously made with {@link com.motionizr.percensio.commons.CallStatus#COMPLETED} from the database.
     *
     * @return                          The list of calls that were previously made with {@link com.motionizr.percensio.commons.CallStatus#COMPLETED} from the database.
     * @throws DatabaseEngineException  If an error occurs fetching the calls from the database.
     */
    private List<String> getCallsAlreadyCompleted() throws DatabaseEngineException{
        List<Map<String, ResultColumn>> results = engine.query(
                select(column(SurveyEntities.CALL_RESULT_TO))
                .from(table(SurveyEntities.CALL_RESULT_TABLE))
                .where(eq(column(SurveyEntities.CALL_RESULT_STATUS), k(CallStatus.COMPLETED.getInternalCode())))
        );

        return results.stream()
                .map(e -> e.get(SurveyEntities.CALL_RESULT_TO).toString())
                .collect(Collectors.toList());
    }


    /**
     * Uses the {@link Dialer dialer} to queue the survey calls.
     *
     * @param surveyNumbers         The numbers that should be contacted.
     * @param callsAlreadyCompleted The numbers that were already contacted with success.
     * @return                      A stream containing each and every {@link com.motionizr.percensio.commons.CallResult call result}.
     */
    private Stream<CallResult> queuePhoneCalls(final Stream<String> surveyNumbers, final List<String> callsAlreadyCompleted) {
        Stream<String> internationalSurveyNumbers = configuration.isPrefixConfigured() ?
                surveyNumbers.map(n -> configuration.getInternationalPrefix() + n) :
                surveyNumbers;

        return internationalSurveyNumbers.filter(n -> !callsAlreadyCompleted.contains(n))
                        .map(this::handleDialResult);
    }

    /**
     * Handles the result of a dial operation.
     * </p>
     * Converts from the API call result to a {@link CallResult call result} object.
     *
     * @param number    The number to dial.
     * @return          The {@link CallResult call result}.
     */
    private CallResult handleDialResult(final String number) {
        try {
            return CallResult.fromCall(dialer.dial(number));
        } catch (TwilioRestException e) {
            logger.error("An exception occurred calling {}.", number, e);
            return CallResult.failedCall(number);
        }
    }
}
