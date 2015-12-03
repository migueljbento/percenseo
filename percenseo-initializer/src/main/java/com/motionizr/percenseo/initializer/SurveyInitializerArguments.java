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
import com.beust.jcommander.Parameter;
import com.google.common.base.MoreObjects;

/**
 * Configuration arguments support for a
 *
 * @author Miguel Bento (migueljbento@gmail.com)
 * @version 1.0.0
 */
public class SurveyInitializerArguments {

    /**
     * The path to the CSV file containing the list of numbers to call in the survey.
     */
    @Parameter(names = {"-n", "--numbers"}, description = "Path to the CSV file containing the list of phone numbers", required = true)
    public String numbersFile;

    /**
     * The path to the H2 database file to be used.
     */
    @Parameter(names = {"-d", "--database"}, description = "The path to the H2 database file to be used", required = true)
    public String databaseFile;

    /**
     * The help parameter used to print the help menu.
     */
    @Parameter(names = {"-h", "--help"}, help = true, description = "Shows this help menu")
    public boolean help;

    /**
     * The URL of the endpoint used to handle the call once it's established.
     */
    @Parameter(names = {"-c", "--callhandler"}, description = "The URL of the endpoint used to handle the call once it's established", required = true)
    public String callHandlerUrl;


    /**
     * The URL of the endpoint used to receive the result of the call.
     */
    @Parameter(names = {"-r", "--resulthandler"}, description = "The URL of the endpoint used to receive the result of the call", required = true)
    public String callResultUrl;

    /**
     * The SID of your Twilio account.
     */
    @Parameter(names = {"-s", "--sid"}, description = "The SID of your Twilio account", required = true)
    public String accountSid;

    /**
     * The authentication token for your Twilio account.
     */
    @Parameter(names = {"-a", "-authtoken"}, description = "The authentication token for your Twilio account", required = true)
    public String authToken;

    /**
     * The Twilio phone number that should be used to make the calls.
     */
    @Parameter(names = {"-p", "--callerphone"}, description = "The Twilio phone number that should be used to make the calls", required = true)
    public String callerNumber;

    @Parameter(names = {"-i", "--internationalprefix"}, description = "The internation prefix that should be added to all the numbers being called.")
    public String internationalPrefix;


    /**
     * Prints the help menu and exits the execution.
     *
     * @param cmd   The reference to the Jcommander instance.
     */
    public void printHelpAndExit(JCommander cmd) {
        cmd.setProgramName("java -jar twillio-caller-*.jar");

        StringBuilder helpBuilder = new StringBuilder();
        cmd.usage(helpBuilder);
        System.out.println(helpBuilder.toString());
        System.exit(0);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("numbersFile", numbersFile)
                .add("databaseFile", databaseFile)
                .add("help", help)
                .add("callHandlerUrl", callHandlerUrl)
                .add("callResultUrl", callResultUrl)
                .add("accountSid", accountSid)
                .add("authToken", authToken)
                .add("callerNumber", callerNumber)
                .add("internationalPrefix", internationalPrefix)
                .toString();
    }
}