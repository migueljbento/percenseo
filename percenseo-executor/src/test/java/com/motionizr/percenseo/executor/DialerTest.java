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

import com.google.common.collect.ImmutableMap;
import com.motionizr.percenseo.executor.configuration.SurveyConfiguration;
import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.CallFactory;
import com.twilio.sdk.resource.instance.Account;
import mockit.Deencapsulation;
import mockit.Expectations;
import mockit.Mocked;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

/**
 * @author Miguel Bento (migueljbento@gmail.com)
 * @version 1.0.0
 */
public class DialerTest {

    @Mocked private TwilioRestClient client;

    @Mocked private Account account;

    @Mocked private CallFactory factory;

    /**
     * The survey configuration.
     */
    private SurveyConfiguration configuration;

    private String to = "+351321321321";

    private Map<String, String> expectedParams;

    @Before
    public void setUp() throws Exception {
        configuration = new SurveyConfiguration();
        Deencapsulation.setField(configuration, "callerNumber", "+351123123123");
        Deencapsulation.setField(configuration, "callHandlerURL", "handlerURL");
        Deencapsulation.setField(configuration, "callResultURL", "resultURL");
        Deencapsulation.setField(configuration, "accountSID", "anSID");
        Deencapsulation.setField(configuration, "authToken", "aToken");

        ImmutableMap.Builder expectedParamsBuilder = ImmutableMap.builder();
        expectedParamsBuilder.put("From", configuration.getCallerNumber());
        expectedParamsBuilder.put("To", to);
        expectedParamsBuilder.put("Url", configuration.getCallHandlerURL());
        expectedParamsBuilder.put("StatusCallback", configuration.getCallResultURL());
        expectedParamsBuilder.put("IfMachine", "Hangup");
        expectedParamsBuilder.put("Timeout", "30");

        expectedParams = expectedParamsBuilder.build();
    }

    @Test
    public void testDial() throws Exception {
        new Expectations() {
            {
                new TwilioRestClient(configuration.getAccountSID(), configuration.getAuthToken());
                client.getAccount(); times = 1; result = account;
                account.getCallFactory(); times = 1; result = factory;
                factory.create(expectedParams); times = 1;
            }
        };

        Dialer dialer = new Dialer(configuration);
        dialer.dial(to);

    }

    @Test(expected = TwilioRestException.class)
    public void testDialException() throws Exception {
        new Expectations() {
            {
                new TwilioRestClient(configuration.getAccountSID(), configuration.getAuthToken());
                client.getAccount(); times = 1; result = account;
                account.getCallFactory(); times = 1; result = factory;
                factory.create(expectedParams); times = 1; result = new TwilioRestException("mocked error", 0);
            }
        };

        Dialer dialer = new Dialer(configuration);
        dialer.dial(to);
    }
}