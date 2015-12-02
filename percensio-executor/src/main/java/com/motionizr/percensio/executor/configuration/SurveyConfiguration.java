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

import com.google.common.base.Preconditions;

import java.util.List;

/**
 * @TODO: Fill description
 *
 * @author Miguel Bento (migueljbento@gmail.com)
 * @version 1.0.0
 */
public class SurveyConfiguration {

    /**
     * The path to the CSV file containing the list of numbers to call in the survey.
     */
    protected String numbersCSV;

    /**
     * Flag indicating if the list of numbers should be read from a CSV file.
     */
    protected boolean fileBased = true;

    /**
     * The list of numbers.
     */
    protected List<String> numbers;

    /**
     * The path to the H2 database file to be used.
     */
    protected String databaseFile;

    /**
     * The URL of the endpoint used to handle the call once it's established.
     */
    protected String callHandlerURL;


    /**
     * The URL of the endpoint used to receive the result of the call.
     */
    protected String callResultURL;

    /**
     * The SID of your Twilio account.
     */
    protected String accountSID;

    /**
     * The authentication token for your Twilio account.
     */
    protected String authToken;

    /**
     * The Twilio phone number that should be used to make the calls.
     */
    protected String callerNumber;

    /**
     * Flag indicating if an international prefix should be added to the survey numbers.
     */
    protected boolean prefixConfigured = false;

    /**
     * The international prefix that should be added to the numbers.
     */
    protected String internationalPrefix;

    /**
     * Gets the path to the CSV file containing the list of numbers to call in the survey.
     *
     * @return  The path to the CSV file containing the list of numbers to call in the survey.
     */
    public String getNumbersCSV() {
        return numbersCSV;
    }

    /**
     * Indicates if the numbers to dial should be read from a file or from memory.
     *
     * @return  {@code true} if the numbers should be read from a CSV file, {@code false} otherwise.
     */
    public boolean isFileBased() {
        return fileBased;
    }

    /**
     * Gets the list of numbers.
     *
     * @return  The list of numbers.
     */
    public List<String> getNumbers() {
        return numbers;
    }

    /**
     * Gets the path to the H2 database file to be used.
     *
     * @return  The path to the H2 database file to be used.
     */
    public String getDatabaseFile() {
        return databaseFile;
    }

    /**
     * Gets the URL of the endpoint used to handle the call once it's established.
     *
     * @return  The URL of the endpoint used to handle the call once it's established.
     */
    public String getCallHandlerURL() {
        return callHandlerURL;
    }

    /**
     * Gets the URL of the endpoint used to receive the result of the call.
     *
     * @return  The URL of the endpoint used to receive the result of the call.
     */
    public String getCallResultURL() {
        return callResultURL;
    }

    /**
     * Gets the SID of your Twilio account.
     *
     * @return  The SID of your Twilio account.
     */
    public String getAccountSID() {
        return accountSID;
    }

    /**
     * Gets the authentication token for your Twilio account.
     *
     * @return  The authentication token for your Twilio account.
     */
    public String getAuthToken() {
        return authToken;
    }

    /**
     * Gets the Twilio phone number that should be used to make the calls.
     *
     * @return  The Twilio phone number that should be used to make the calls.
     */
    public String getCallerNumber() {
        return callerNumber;
    }

    /**
     * Indicates if a prefix should be added to all the number dialed as part of the survey.
     *
     * @return  {@code true} if a prefix should be added, {@code false} otherwise.
     */
    public boolean isPrefixConfigured() {
        return prefixConfigured;
    }

    /**
     * Gets the international prefix that should be added to the numbers.
     *
     * @return  The international prefix that should be added to the numbers.
     */
    public String getInternationalPrefix() {
        return internationalPrefix;
    }

    /**
     * Validates the survey configuration.
     *
     * @throws IllegalArgumentException If the current configuration is invalid.
     */
    protected void validate() throws IllegalArgumentException {
        if (fileBased) {
            Preconditions.checkArgument(
                    numbersCSV != null &&
                    numbersCSV.trim().length() > 0 &&
                    numbersCSV.toLowerCase().endsWith(".csv"),
                    String.format("Invalid CSV file: %s", numbersCSV)
            );
        } else {
            Preconditions.checkArgument(
                    numbers != null &&
                    numbers.size() > 0,
                    "Invalid or empty numbers list."
            );
        }

        Preconditions.checkArgument(
                databaseFile != null &&
                databaseFile.trim().length() > 0,
                "Invalid H2 database file"
        );

        Preconditions.checkArgument(
                callHandlerURL != null &&
                callHandlerURL.trim().length() > 0,
                String.format("Invalid call handler URL: %s", callHandlerURL)
        );

        Preconditions.checkArgument(
                callResultURL != null &&
                callResultURL.trim().length() > 0,
                String.format("Invalid call result URL: %s", callResultURL)
        );

        Preconditions.checkArgument(
                accountSID != null &&
                accountSID.trim().length() > 0,
                String.format("Invalid Twilio account SID: %s", accountSID)
        );

        Preconditions.checkArgument(
                authToken != null &&
                authToken.trim().length() > 0,
                String.format("Invalid auth token: %s", authToken)
        );

        Preconditions.checkArgument(
                callerNumber != null &&
                callerNumber.trim().length() > 0,
                String.format("Invalid Twilio caller number: %s", callerNumber)
        );

        if (prefixConfigured) {
            Preconditions.checkArgument(
                    internationalPrefix != null &&
                    internationalPrefix.trim().length() > 0,
                    String.format("Invalid international prefix: %s", internationalPrefix)
            );
        }

    }
}
