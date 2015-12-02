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
 * Enumerates all the call directions supported by the Twilio API.
 *
 * @author Miguel Bento (migueljbento@gmail.com)
 * @version 1.0.0
 */
public enum CallDirection {

    /**
     * Call initiated by the survey member to the survey number.
     */
    INBOUND("inbound", 0),

    /**
     * Call initiated by the survey executor.
     */
    OUTBOUND("outbound-api", 1),

    /**
     * Unknown call direction.
     */
    UNKNOWN("unknown", -1)
    ;

    /**
     * The call direction description used by the Twilio API.
     */
    private String desc;

    /**
     * The internal internalCode used to represent the call direction in an efficient manner.
     */
    private int internalCode;

    /**
     * Creates a new instance of {@link com.motionizr.percensio.commons.CallDirection}.
     *
     * @param desc          The call direction description used by the Twilio API.
     * @param internalCode  The internal internalCode used to represent the call direction in an efficient manner.
     */
    CallDirection(String desc, int internalCode) {
        this.desc = desc;
        this.internalCode = internalCode;
    }

    /**
     * Gets the call direction description used by the Twilio API.
     *
     * @return  The call direction description used by the Twilio API.
     */
    public String getDesc() {
        return desc;
    }

    /**
     * Gets the internal internalCode used to represent the call direction in an efficient manner.
     *
     * @return  The internal internalCode used to represent the call direction in an efficient manner.
     */
    public int getInternalCode() {
        return internalCode;
    }

    /**
     * Map containing the {@link com.motionizr.percensio.commons.CallDirection} by its internal code.
     */
    public static final Map<Integer, CallDirection> FROM_INTERNAL_CODE = Arrays.stream(CallDirection.values())
            .collect(Collectors.toMap(CallDirection::getInternalCode, Function.identity()));

    /**
     * Map containing the {@link com.motionizr.percensio.commons.CallDirection} by its public description.
     */
    public static final Map<String, CallDirection> FROM_DESC = Arrays.stream(CallDirection.values())
            .collect(Collectors.toMap(CallDirection::getDesc, Function.identity()));

    /**
     * Gets the {@link com.motionizr.percensio.commons.CallDirection} from its internal code.
     *
     * @param internalCode  The internal code of the {@link com.motionizr.percensio.commons.CallDirection}.
     * @return              The {@link com.motionizr.percensio.commons.CallDirection} instance.
     */
    public static CallDirection fromInternalCode(int internalCode) {
        return FROM_INTERNAL_CODE.getOrDefault(internalCode, UNKNOWN);
    }

    /**
     * Gets the {@link com.motionizr.percensio.commons.CallDirection} from its description.
     *
     * @param desc  The description of the {@link com.motionizr.percensio.commons.CallDirection}.
     * @return      The {@link com.motionizr.percensio.commons.CallDirection} instance.
     */
    public static CallDirection fromDesc(String desc) {
        return FROM_DESC.getOrDefault(desc, UNKNOWN);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("internalCode", internalCode)
                .add("desc", desc)
                .toString();
    }
}
