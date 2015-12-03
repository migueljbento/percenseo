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
package com.motionizr.percenseo.executor;

import com.motionizr.percenseo.executor.configuration.SurveyConfiguration;
import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.CallFactory;
import com.twilio.sdk.resource.instance.Account;
import com.twilio.sdk.resource.instance.Call;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Queues a call to a given number using the Twilio API.
 * <p/>
 * Configures a 30 seconds timeout for the call and configures an endpoint to receive the final status of the call.
 * If an answering machine picks up the call Twilio is configured to automatically hang up the call.
 *
 * @author Miguel Bento (migueljbento@gmail.com)
 * @version 1.0.0
 */
public class Dialer {

    /**
     * The logger.
     */
    private static Logger logger = LoggerFactory.getLogger(Dialer.class);

    /**
     * The factory used to queue the calls.
     */
    private final CallFactory callFactory;

    /**
     * The map containing the parameters used in the call.
     */
    private final Map<String, String> callParams;

    /**
     * Creates a new instance of {@link Dialer}.
     *
     * @param configuration The {@link com.motionizr.percenseo.executor.configuration.SurveyConfiguration survey configuration}.
     */
    public Dialer(SurveyConfiguration configuration) {
        callFactory = setupCallFactory(configuration);

        callParams = new HashMap<>(6);
        callParams.put("From", configuration.getCallerNumber());
        callParams.put("Url", configuration.getCallHandlerURL());
        callParams.put("StatusCallback", configuration.getCallResultURL());
        callParams.put("IfMachine", "Hangup");
        callParams.put("Timeout", "30");
    }

    /**
     * Instatiates a Twilio call factory.
     *
     * @param configuration The {@link com.motionizr.percenseo.executor.configuration.SurveyConfiguration survey configuration}.
     * @return              A Twilio call factory.
     */
    private CallFactory setupCallFactory(SurveyConfiguration configuration) {
        TwilioRestClient client = new TwilioRestClient(configuration.getAccountSID(), configuration.getAuthToken());
        Account mainAccount = client.getAccount();
        return mainAccount.getCallFactory();
    }

    /**
     * Dials a survey member.
     *
     * @param memberNumber          The number of the survey member that we want to dial.
     * @return                      The actual state of the call, which will differ from its final state.
     * @throws TwilioRestException  If an error occurs dialing the member.
     */
    public Call dial(String memberNumber) throws TwilioRestException {
        callParams.put("To", memberNumber);

        logger.debug("Queuing phone call to {}", memberNumber);
        return callFactory.create(callParams);
    }
}
