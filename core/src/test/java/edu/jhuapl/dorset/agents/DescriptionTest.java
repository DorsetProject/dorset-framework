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
package edu.jhuapl.dorset.agents;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.jhuapl.dorset.agents.Description;

public class DescriptionTest {

    @Test
    public void testSetName() {
        Description d = new Description("Agent007", "test", "test");
        assertEquals("agent007", d.getName());
    }

    @Test
    public void testSetExample() {
        Description d = new Description();
        d.setExample("What is 1 + 1?");
        assertEquals(1, d.getExamples().length);
        assertEquals("What is 1 + 1?", d.getExamples()[0]);
    }

    @Test
    public void testSetExamples() {
        Description d = new Description();
        String[] examples = {"Where are you?", "What is this?"};
        d.setExamples(examples);
        assertEquals(2, d.getExamples().length);
        assertArrayEquals(examples, d.getExamples());
    }

}
