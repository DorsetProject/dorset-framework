/*
 * Copyright 2016 The Johns Hopkins University Applied Physics Laboratory LLC
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.jhuapl.dorset;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.jhuapl.dorset.ResponseStatus.Code;

public class ResponseStatusTest {

    @Test
    public void testEquals() {
        ResponseStatus status1 = new ResponseStatus(Code.AGENT_INTERNAL_ERROR, "this is a test");
        ResponseStatus status2 = new ResponseStatus(Code.AGENT_INTERNAL_ERROR, "this is a test");
        ResponseStatus status3 = new ResponseStatus(Code.AGENT_INTERNAL_ERROR, "hello world");
        ResponseStatus status4 = new ResponseStatus(Code.INTERNAL_ERROR, "hello world");

        assertEquals(status1, status2);
        assertNotEquals(status1, status3);
        assertNotEquals(status1, status4);
        assertNotEquals(status3, status4);
    }

    @Test
    public void testFromValue() {
        assertEquals(Code.SUCCESS, Code.fromValue(0));
        assertEquals(Code.INTERNAL_ERROR, Code.fromValue(100));
        assertNull(Code.fromValue(34));
    }

}
