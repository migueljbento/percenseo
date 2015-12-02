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

import com.google.common.base.MoreObjects;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Enumerates all the possible Twilio call status.
 * <p/>
 * Used to store the call status in the database in a more efficient manner. Strings are ugly.
 *
 * @author Miguel Bento (migueljbento@gmail.com)
 * @version 1.0.0
 */
public enum CallStatus {
    /**
     * All new calls are created with a status of queued, indicating Twilio has received your request to create the call.
     */
    QUEUED          ("queued", 0),

    /**
     * Twilio has dialed the call.
     */
    INITIATED       ("initiated", 1),

    /**
     * The destination number has started ringing.
     */
    RINGING         ("ringing", 2),

    /**
     * The call has been answered and is current in progress.
     */
    IN_PROGRESS     ("in-progress", 3),

    /**
     * The call was canceled using the REST API.
     */
    CANCELED        ("canceled", 4),

    /**
     * After an answered call ends it has a completed status. Since the call is over the Status will always remain in this state.
     */
    COMPLETED       ("completed", 5),

    /**
     * Twilio dialed the number and received a busy signal.
     */
    BUSY            ("busy", 6),

    /**
     * Twilio’s carriers could not connect the call, most likely because the phone number was entered incorrectly or was disconnected.
     */
    FAILED          ("failed", 7),

    /**
     * Twilio dialed the number but no one answered before the Timeout value elapsed.
     */
    NO_ANSWER       ("no-answer", 8),

    /**
     * Used whenever Twilio replies with an unknown state.
     */
    UNKNOWN         ("unknown", -1);

    /**
     * Map containing the {@link CallStatus call status} instances by its desc.
     */
    private static final Map<String, CallStatus> FROM_DESC = Arrays.stream(CallStatus.values())
            .collect(Collectors.toMap(CallStatus::getDesc, Function.identity()));

    /**
     * Map containing the {@link CallStatus call status} instances by its internal code.
     */
    private static final Map<Integer, CallStatus> FROM_INTERNAL_CODE = Arrays.stream(CallStatus.values())
            .collect(Collectors.toMap(CallStatus::getInternalCode, Function.identity()));

    /**
     * The status description used by the Twilio API.
     */
    private String desc;

    /**
     * The internal code used to represent the instance in an efficient manner.
     */
    private int internalCode;

    /**
     * Creates a new instance of {@link CallStatus}.
     *
     * @param desc          The status description used by the Twilio API.
     * @param internalCode  The internal code used to represent the instance in an efficient manner.
     */
    private CallStatus(String desc, int internalCode) {
        this.desc = desc;
        this.internalCode = internalCode;
    }

    /**
     * Gets the status description used by the Twilio API.
     *
     * @return  The status description used by the Twilio API.
     */
    public String getDesc() {
        return desc;
    }

    /**
     * Gets the internal code used to represent the instance in an efficient manner.
     *
     * @return  The internal code used to represent the instance in an efficient manner.
     */
    public int getInternalCode() {
        return internalCode;
    }

    /**
     * Gets the {@link CallStatus call status} instance from its public desc.
     *
     * @param desc  The public description of the {@link CallStatus call status}.
     * @return      The {@link CallStatus call status} instance.
     */
    public static CallStatus fromDesc(String desc) {
        return FROM_DESC.getOrDefault(desc, UNKNOWN);
    }

    /**
     * Gets the {@link CallStatus call status} instance from its internal code.
     *
     * @param internalCode  The internal code of the {@link CallStatus call status}.
     * @return              The {@link CallStatus call status} instance.
     */
    @SuppressWarnings("UnusedDeclaration")
    public static CallStatus fromInternalCode(Integer internalCode) {
        return FROM_INTERNAL_CODE.getOrDefault(internalCode, UNKNOWN);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("desc", desc)
                .add("internalCode", internalCode)
                .toString();
    }
}
