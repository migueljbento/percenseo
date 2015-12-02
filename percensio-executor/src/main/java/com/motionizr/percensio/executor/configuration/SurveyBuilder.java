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
package com.motionizr.percensio.executor.configuration;

import com.motionizr.percensio.executor.SurveyOrchestrator;

import java.util.List;

/**
 * POJO holding all the configuration necessary for the {@link com.motionizr.percensio.executor.SurveyOrchestrator}.
 *
 * @author Miguel Bento (migueljbento@gmail.com)
 * @version 1.0.0
 */
public class SurveyBuilder {

    /**
     * POJO containing the configuration necessary to execute the survey.
     */
    private final SurveyConfiguration configuration;

    /**
     * Creates a new instance of {@link SurveyBuilder}.
     */
    public SurveyBuilder() {
        configuration = new SurveyConfiguration();
    }


    /**
     * Configures the Twilio phone number that should be used to make the calls.
     *
     * @param callerNumber  The Twilio phone number that should be used to make the calls.
     * @return              The {@link SurveyBuilder} instance.
     */
    public SurveyBuilder withCallerNumber(String callerNumber) {
        configuration.callerNumber = callerNumber;

        return this;
    }

    /**
     * Configures the path to the CSV file containing the list of numbers to call in the survey.
     *
     * @param numbersCSV    The path to the CSV file containing the list of numbers to call in the survey.
     * @return              The {@link SurveyBuilder} instance.
     */
    public SurveyBuilder withNumbersCSV(String numbersCSV) {
        configuration.numbersCSV = numbersCSV;
        configuration.fileBased = true;

        return this;
    }

    /**
     * Configures the list of numbers.
     *
     * @param numbers   The list of numbers.
     * @return          The {@link SurveyBuilder} instance.
     */
    public SurveyBuilder withNumbers(List<String> numbers) {
        configuration.numbers = numbers;
        configuration.fileBased = false;

        return this;
    }

    /**
     * Configures the path to the H2 database file to be used.
     *
     * @param dbFile    The path to the H2 database file to be used.
     * @return          The {@link SurveyBuilder} instance.
     */
    public SurveyBuilder withDatabaseFile(String dbFile) {
        configuration.databaseFile = dbFile;

        return this;
    }

    /**
     * Configures the URL of the endpoint used to handle the call once it's established.
     *
     * @param callHandlerURL    The URL of the endpoint used to handle the call once it's established.
     * @return                  The {@link SurveyBuilder} instance.
     */
    public SurveyBuilder withCallHandlerURL(String callHandlerURL) {
        configuration.callHandlerURL = callHandlerURL;

        return this;
    }

    /**
     * Configures the URL of the endpoint used to receive the result of the call.
     *
     * @param callResultURL The URL of the endpoint used to receive the result of the call.
     * @return              The {@link SurveyBuilder} instance.
     */
    public SurveyBuilder withCallResultURL(String callResultURL) {
        configuration.callResultURL = callResultURL;

        return this;
    }

    /**
     * Configures the SID of your Twilio account.
     *
     * @param accountSID    The SID of your Twilio account.
     * @return              The {@link SurveyBuilder} instance.
     */
    public SurveyBuilder withAccountSID(String accountSID) {
        configuration.accountSID = accountSID;

        return this;
    }

    /**
     * Configures the authentication token for your Twilio account.
     *
     * @param authToken     The authentication token for your Twilio account.
     * @return              The {@link SurveyBuilder} instance.
     */
    public SurveyBuilder withAuthToken(String authToken) {
        configuration.authToken = authToken;

        return this;
    }

    /**
     * Configured an international prefix that will be added to all the numbers dialed.
     *
     * @param internationalPrefix   The international prefix.
     * @return                      The {@link SurveyBuilder} instance.
     */
    public SurveyBuilder withInternationalPrefix(String internationalPrefix) {
        configuration.internationalPrefix = internationalPrefix;
        configuration.prefixConfigured = true;

        return this;
    }

    /**
     * Builds a {@link com.motionizr.percensio.executor.SurveyOrchestrator}.
     *
     * @return                          An instance of {@link com.motionizr.percensio.executor.SurveyOrchestrator}.
     * @throws IllegalArgumentException If the configuration being used for the survey is invalid.
     */
    public SurveyOrchestrator build() throws IllegalArgumentException {
        configuration.validate();

        return new SurveyOrchestrator(configuration);

    }
}
