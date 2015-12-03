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
package com.motionizr.percenseo.initializer;

import com.beust.jcommander.JCommander;
import com.motionizr.percenseo.executor.SurveyOrchestrator;
import com.motionizr.percenseo.executor.configuration.SurveyBuilder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Command line utility to initialize a survey that follows a certain configuration.
 *
 * @author Miguel Bento (migueljbento@gmail.com)
 * @version 1.0.0
 */
public class SurveyInitializer {

    /**
     * The logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(SurveyInitializer.class);

    /**
     * Empty constructor to avoid accidental initialization.
     */
    private SurveyInitializer() {}

    public static void main(String[] args) {

        SurveyInitializerArguments initializerArgs = new SurveyInitializerArguments();
        JCommander cmd = new JCommander(initializerArgs, args);

        if (initializerArgs.help) {
            initializerArgs.printHelpAndExit(cmd);
        }
        logger.debug("All arguments read successfully: {}", initializerArgs);

        SurveyBuilder builder = new SurveyBuilder()
                .withAccountSID(initializerArgs.accountSid)
                .withAuthToken(initializerArgs.authToken)
                .withCallerNumber(initializerArgs.callerNumber)
                .withCallHandlerURL(initializerArgs.callHandlerUrl)
                .withCallResultURL(initializerArgs.callResultUrl)
                .withDatabaseFile(initializerArgs.databaseFile)
                .withNumbersCSV(initializerArgs.numbersFile);

        if (StringUtils.isNotBlank(initializerArgs.internationalPrefix)) {
            builder.withInternationalPrefix(initializerArgs.internationalPrefix);
        }

        SurveyOrchestrator orchestrator = builder.build();

        orchestrator.execute();
        logger.info("Survey executed. Please note that this operation is asynchronous, the survey was merely queued. Results will be gathered in the following minutes.");
    }
}
