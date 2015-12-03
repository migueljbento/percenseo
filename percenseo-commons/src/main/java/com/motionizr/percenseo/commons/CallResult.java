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
package com.motionizr.percenseo.commons;

import com.feedzai.commons.sql.abstraction.entry.EntityEntry;
import com.google.common.base.MoreObjects;
import com.twilio.sdk.resource.instance.Call;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import static com.feedzai.commons.sql.abstraction.dml.dialect.SqlBuilder.entry;

/**
 * Holds the result of an outbound call that was part of the survey.
 * <p/>
 * Stores several important details for further analysis like the call outcome and its duration.
 *
 * @author Miguel Bento (migueljbento@gmail.com)
 * @version 1.0.0
 */
public class CallResult {

    /**
     * The logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(CallResult.class);

    /**
     * The formatter used to parse the call date in RFC 2822 representation.
     */
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss Z");

    /**
     * Instance used to represent calls that failed.
     */
    private static final CallResult failedCallResult;

    static {
        failedCallResult = new CallResult();
        failedCallResult.status = CallStatus.FAILED;
    }

    /**
     * The phone number dialed.
     */
    private String destination;

    /**
     * The SID of the call.
     */
    private String callSID;

    /**
     * The duration of the call in seconds.
     */
    private int callDuration;

    /**
     * Flag indicating if an human answered the call or if it was an answering machine.
     */
    private boolean humanAnswered;

    /**
     * The {@link CallStatus status} of the call.
     */
    private CallStatus status;

    /**
     * The call date.
     */
    private LocalDateTime callDate;

    /**
     * The {@link CallDirection}, outbound or inbound.
     */
    private CallDirection direction;

    /**
     * The digits pressed by the survey member during the call.
     */
    private String digits;

    /**
     * Creates a new instance of {@link CallResult}.
     */
    protected CallResult() {}

    /**
     * Gets the phone number dialed.
     *
     * @return  The phone number dialed.
     */
    public String getDestination() {
        return destination;
    }

    /**
     * Gets the SID of the call.
     *
     * @return  The SID of the call.
     */
    public String getCallSID() {
        return callSID;
    }

    /**
     * Gets the duration of the call in seconds.
     *
     * @return  The duration of the call in seconds.
     */
    public int getCallDuration() {
        return callDuration;
    }

    /**
     * Verifies if an human answered the call.
     *
     * @return  {@code true} if the call was answered by an human, {@code false} otherwise.
     */
    public boolean isHumanAnswered() {
        return humanAnswered;
    }

    /**
     * Gets the {@link CallStatus status} of the call.
     *
     * @return  The {@link CallStatus status} of the call.
     */
    public CallStatus getStatus() {
        return status;
    }

    /**
     * Gets the {@link CallDirection call direction}.
     *
     * @return  The {@link CallDirection call direction}.
     */
    public CallDirection getDirection() {
        return direction;
    }

    /**
     * Gets the call date.
     *
     * @return  The call date.
     */
    public LocalDateTime getCallDate() {
        return callDate;
    }

    /**
     * Gets the digits pressed by the survey member during the call.
     *
     * @return  The digits pressed by the survey member during the call.
     */
    public String getDigits() {
        return digits;
    }

    /**
     * Converts a {@link CallResult call result} to a database entity.
     *
     * @return  The database entity that represents the {@link CallResult call result}.
     */
    public EntityEntry toEntity() {
        return entry()
                .set(SurveyEntities.CALL_RESULT_SID, callSID)
                .set(SurveyEntities.CALL_RESULT_TO, destination)
                .set(SurveyEntities.CALL_RESULT_DURATION, callDuration)
                .set(SurveyEntities.CALL_RESULT_STATUS, status != null ? status.getInternalCode() : CallStatus.UNKNOWN.getInternalCode())
                .set(SurveyEntities.CALL_RESULT_DATE, callDate != null ? callDate.toInstant(ZoneOffset.UTC).getEpochSecond() : null)  //@TODO: Improve TZ support.
                .set(SurveyEntities.CALL_RESULT_HUMAN_ANSWERED, humanAnswered)
                .set(SurveyEntities.CALL_RESULT_DIRECTION, direction != null ? direction.getInternalCode() : CallDirection.UNKNOWN.getInternalCode())
                .set(SurveyEntities.CALL_RESULT_DIGITS, digits)
                .build();
    }

    /**
     * Creates a new instance of {@link CallResult call result} from a Twilio call.
     *
     * @param call  The Twilio Call.
     * @return      The {@link CallResult call result}.
     */
    public static CallResult fromCall(Call call) {
        CallResult result = new CallResult();
        result.destination = call.getTo();
        result.callSID = call.getSid();

        try {
            result.callDuration = Integer.parseInt(call.getDuration());
        } catch (NumberFormatException e) {
            logger.warn("Unable to fetch the call duration.");
        }

        if (call.getAnsweredBy() != null) {
            result.humanAnswered = call.getAnsweredBy().equals("human");
        }

        result.status = CallStatus.fromDesc(call.getStatus());
        result.direction = CallDirection.fromDesc(call.getDirection());

        if (call.getDateCreated() != null) {
            result.callDate = LocalDateTime.ofInstant(call.getDateCreated().toInstant(), ZoneId.systemDefault());
        }

        return result;
    }

    /**
     * Creates a new instance of {@link CallResult call result} from a Twilio StatusCallback request.
     *
     * @param request   The Twilio StatusCallback request.
     * @return          The {@link CallResult call result}.
     */
    public static CallResult fromHttpServletRequest(HttpServletRequest request) {
        CallResult result = new CallResult();
        result.destination = request.getParameter("Caller");
        result.callSID = request.getParameter("CallSid");

        try {
            result.callDuration = Integer.parseInt(request.getParameter("CallDuration"));
        } catch (NumberFormatException e) {
            logger.warn("Unable to fetch the call duration.");
        }

        String answeredBy = request.getParameter("AnsweredBy");
        result.humanAnswered = answeredBy != null && answeredBy.equals("human");

        result.status = CallStatus.fromDesc(request.getParameter("CallStatus"));
        result.direction = CallDirection.fromDesc(request.getParameter("Direction"));
        result.digits = request.getParameter("Digits");

        String callTs = request.getParameter("Timestamp");
        if (StringUtils.isNotBlank(callTs)) {
            result.callDate = LocalDateTime.parse(callTs, formatter);
        }

        return result;
    }

    /**
     * Returns a {@link CallResult call result} for a failed call to a specified number.
     *
     * @param number    The number dialed.
     * @return          A call failure result for the specified number.
     */
    public static CallResult failedCall(String number) {
        failedCallResult.destination = number;
        return failedCallResult;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("destination", destination)
                .add("callSID", callSID)
                .add("callDuration", callDuration)
                .add("humanAnswered", humanAnswered)
                .add("status", status)
                .add("callDate", callDate)
                .add("direction", direction)
                .add("digits",  digits)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CallResult that = (CallResult) o;

        return callSID.equals(that.callSID);
    }

    @Override
    public int hashCode() {
        return callSID.hashCode();
    }
}
