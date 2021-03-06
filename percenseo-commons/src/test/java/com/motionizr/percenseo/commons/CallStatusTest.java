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

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * @author Miguel Bento (migueljbento@gmail.com)
 * @version 1.0.0
 */
public class CallStatusTest {

    @Test
    public void testFromDesc() throws Exception {
        Arrays.stream(CallStatus.values())
                .forEach(status -> assertEquals("Should read the status from the desc correctly", status, CallStatus.fromDesc(status.getDesc())));

        assertEquals("Should default to unknown for new status", CallStatus.UNKNOWN, CallStatus.fromDesc("unknown_status"));
    }

    @Test
    public void testFromInternalCode() throws Exception {
        Arrays.stream(CallStatus.values())
                .forEach(status -> assertEquals("Should read the status from the internal code correctly", status, CallStatus.fromInternalCode(status.getInternalCode())));

        assertEquals("Should default to unknown for new status", CallStatus.UNKNOWN, CallStatus.fromInternalCode(-100));
    }
}