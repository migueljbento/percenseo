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
package com.motionizr.percensio.handler;

import com.twilio.sdk.verbs.Say;
import com.twilio.sdk.verbs.TwiMLException;
import com.twilio.sdk.verbs.TwiMLResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This endpoint handles the survey call once it's established.
 * <p/>
 * Uses Twilio verbs to take action. For more information please consult <a href="https://www.twilio.com/docs/api/twiml">Twilio documentation</a>.
 *
 * @author Miguel Bento (migueljbento@gmail.com)
 * @version 1.0.0
 */
public class SurveyCallHandler extends HttpServlet {

    /**
     * The logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(SurveyCallHandler.class);

    /**
     * Endpoint used to reply to the call.
     * </p>
     * Instructs Twilio what content should be played back to the survey member.
     *
     * @param request           The Twilio request containing the call parameters.
     * @param response          The response object containing the instructions
     * @throws IOException      If an error occurred processing the servlet
     * @throws ServletException If an error occurred processing the servlet
     */
    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        TwiMLResponse twiml = new TwiMLResponse();

        Say say1 = new Say("Please configure.");
        say1.setVoice("alice");
        say1.setLanguage("en-US");

        Say say2 = new Say("Your message.");
        say2.setVoice("alice");
        say2.setLanguage("en-US");

        Say say3 = new Say("Here.");
        say3.setVoice("alice");
        say3.setLanguage("en-US");

        try {
            twiml.append(say1);
            twiml.append(say2);
            twiml.append(say3);
        } catch (TwiMLException e) {
            logger.error("Unable to construct call response.", e);
        }

        response.setContentType("application/xml");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().print("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + twiml.toXML());
    }

}
