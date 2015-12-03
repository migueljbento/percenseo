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
package com.motionizr.percenseo.handler;

import com.feedzai.commons.sql.abstraction.engine.DatabaseEngine;
import com.feedzai.commons.sql.abstraction.engine.DatabaseEngineException;
import com.feedzai.commons.sql.abstraction.engine.DatabaseFactoryException;
import com.motionizr.percenseo.commons.CallResult;
import com.motionizr.percenseo.commons.DatabaseUtils;
import com.motionizr.percenseo.commons.SurveyEntities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * This endpoint collects information about the outcome of the survey calls.
 * <p/>
 * Basically it creates a {@link com.motionizr.percenseo.commons.CallResult} from the information provided to the endpoint.
 *
 * @author Miguel Bento (migueljbento@gmail.com)
 * @version 1.0.0
 */
public class SurveyResultHandler extends HttpServlet {

    /**
     * The database connection.
     */
    private DatabaseEngine engine;

    /**
     * The logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(SurveyResultHandler.class);

    @Override
    public void init() throws ServletException {
        try {
            engine = DatabaseUtils.initializeDbConnection("YOU_DB_FILE_HERE");
        } catch (DatabaseFactoryException | DatabaseEngineException e) {
            logger.error("Unable to initilize the database connection.", e);
            throw new ServletException("Unable to initialize the database connection. Please correct this error before proceeding.");
        }
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("Call result request: {}", req.getParameterMap().entrySet().stream()
                .map(entry -> entry.getKey() + ":" + Arrays.toString(entry.getValue()))
                .collect(Collectors.joining(", ")));

        CallResult result = CallResult.fromHttpServletRequest(req);
        logger.info("Call result: {}", result);

        persistCallResult(result);
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    public void destroy() {
        logger.info("Destroy called");
        DatabaseUtils.closeDbConnection(engine);
        logger.info("Engine closed");
    }

    /**
     * Persists a {@link CallResult call result} in the database.
     *
     * @param result    The {@link CallResult call result} to persist.
     */
    private void persistCallResult(CallResult result) {

        /* No need to use nothing too fancy, this won't handle a huge amount of requests per second. */
        synchronized (this) {
            try {
                engine.beginTransaction();

                engine.persist(SurveyEntities.CALL_RESULT_TABLE, result.toEntity());

                engine.flush();
                engine.commit();
                logger.debug("Call {} persisted.", result.getCallSID());

            } catch (DatabaseEngineException e) {
                logger.error("Unable to store call results for SID: {}.", result.getCallSID(), e);
            } finally {
                if (engine.isTransactionActive()) {
                    engine.rollback();
                }
            }
        }

    }
}
