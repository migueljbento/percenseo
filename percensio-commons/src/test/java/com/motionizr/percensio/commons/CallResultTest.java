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
package com.motionizr.percensio.commons;

import com.feedzai.commons.sql.abstraction.entry.EntityEntry;
import com.google.common.collect.ImmutableMap;
import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.resource.instance.Call;
import mockit.Deencapsulation;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author Miguel Bento (migueljbento@gmail.com)
 * @version 1.0.0
 */
public class CallResultTest {

    private final String to = "+16175551212";
    private final String sid = "ae851c56d7d6a91b";
    private final int duration = 28;
    private final boolean humanAnswered = true;
    private final CallStatus status = CallStatus.COMPLETED;
    private final LocalDateTime callDate = LocalDateTime.of(2015, 11, 18, 19, 0, 0);
    private final CallDirection direction = CallDirection.OUTBOUND;
    private final String digits = "013";


    @Test
    public void testToEntity() throws Exception {
        CallResult result = new CallResult();
        EntityEntry resultEntity = result.toEntity();

        assertEquals("Empty status should not throw an NPE and should default to UNKNOWN", CallStatus.UNKNOWN.getInternalCode(), resultEntity.get(SurveyEntities.CALL_RESULT_STATUS));
        assertEquals("Empty direction should not throw an NPE and should default to UNKNOWN", CallDirection.UNKNOWN.getInternalCode(), resultEntity.get(SurveyEntities.CALL_RESULT_DIRECTION));
        assertNull("Empty call date should not throw an NPE", resultEntity.get(SurveyEntities.CALL_RESULT_DATE));

        Deencapsulation.setField(result, "to", to);
        Deencapsulation.setField(result, "callSID", sid);
        Deencapsulation.setField(result, "callDuration", duration);
        Deencapsulation.setField(result, "humanAnswered", humanAnswered);
        Deencapsulation.setField(result, "status", status);
        Deencapsulation.setField(result, "callDate", callDate);
        Deencapsulation.setField(result, "direction", direction);
        Deencapsulation.setField(result, "digits", digits);

        resultEntity = result.toEntity();

        assertEquals("To should be correctly set", to, resultEntity.get(SurveyEntities.CALL_RESULT_TO));
        assertEquals("SID should be correctly set", sid, resultEntity.get(SurveyEntities.CALL_RESULT_SID));
        assertEquals("Duration should be correctly set", duration, resultEntity.get(SurveyEntities.CALL_RESULT_DURATION));
        assertEquals("Human answered flag should be correctly set", humanAnswered, resultEntity.get(SurveyEntities.CALL_RESULT_HUMAN_ANSWERED));
        assertEquals("Status should be correctly set", status.getInternalCode(), resultEntity.get(SurveyEntities.CALL_RESULT_STATUS));
        assertEquals("Call date should be correctly set", callDate.toInstant(ZoneOffset.UTC).getEpochSecond(), resultEntity.get(SurveyEntities.CALL_RESULT_DATE));
        assertEquals("Direction should be correctly set", direction.getInternalCode(), resultEntity.get(SurveyEntities.CALL_RESULT_DIRECTION));
        assertEquals("Digits should be correctly set", digits, resultEntity.get(SurveyEntities.CALL_RESULT_DIGITS));
    }

    @Test
    public void testFromCall() throws Exception {
        ImmutableMap.Builder builder = ImmutableMap.builder();
        builder.put("to", to);
        builder.put("sid", sid);
        builder.put("duration", String.valueOf(duration));
        builder.put("answered_by", humanAnswered ? "human" : "machine");
        builder.put("status", status.getDesc());
        builder.put("date_created", "Wed, 18 Nov 2015 19:00:00 +0000");
        builder.put("direction", direction.getDesc());

        Call call = new Call(new TwilioRestClient("AC34567890123456789012345678901234", "anAuthToken"), builder.build());
        CallResult result = CallResult.fromCall(call);

        assertEquals("To should be correctly set", to, result.getTo());
        assertEquals("SID should be correctly set", sid, result.getCallSID());
        assertEquals("Duration should be correctly set", duration, result.getCallDuration());
        assertEquals("Human answered flag should be correctly set", humanAnswered, result.isHumanAnswered());
        assertEquals("Status should be correctly set", status, result.getStatus());
        assertEquals("Call date should be correctly set", callDate, result.getCallDate());
        assertEquals("Direction should be correctly set", direction, result.getDirection());
    }


    @Test
    public void testFailedCall() throws Exception {
        CallResult failedCall = CallResult.failedCall(to);

        assertEquals("To should be correctly set", to, failedCall.getTo());
        assertEquals("Status should be correctly set", CallStatus.FAILED, failedCall.getStatus());
    }
}