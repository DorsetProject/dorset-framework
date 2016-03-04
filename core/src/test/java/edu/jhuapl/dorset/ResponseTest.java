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

import edu.jhuapl.dorset.Response.Type;

public class ResponseTest {

    @Test
    public void testFromValue() {
        assertEquals(Type.TEXT, Type.fromValue("text"));
        assertEquals(Type.ERROR, Type.fromValue("error"));
        assertNull(Type.fromValue("nonsuch"));
    }

}
