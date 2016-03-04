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

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import edu.jhuapl.dorset.ResponseStatus;
import edu.jhuapl.dorset.agent.AgentRequest;
import edu.jhuapl.dorset.agent.AgentResponse;

public class DateTimeAgentTest {

    private Date testDate;
    private DateTimeAgent agent;

    @Before
    public void setup() {
        agent = new DateTimeAgent();

        Calendar cal = new GregorianCalendar(2016, Calendar.JANUARY, 1, 1, 0, 0);
        cal.set(Calendar.DAY_OF_WEEK, 2);
        testDate = cal.getTime();
    }

    @Test
    public void testGetTime() {
        assertEquals("1:00 AM", agent.getTime(testDate));
    }

    @Test
    public void testGetDay() {
        assertEquals("Friday", agent.getDay(testDate));
    }

    @Test
    public void testGetDate() {
        assertEquals("January 01, 2016", agent.getDate(testDate));
    }

    @Test
    public void testBadRequest() {
        AgentResponse response = agent.process(new AgentRequest("where is pluto?"));
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertEquals(ResponseStatus.Code.AGENT_DID_NOT_UNDERSTAND_REQUEST, response.getStatus().getCode());
    }

    @Test
    public void testProcessForDayOfWeek() {
        AgentResponse response = agent.process(new AgentRequest("what is today?"));
        assertNotNull(response);
        assertTrue(response.isSuccess());
        Set<String> daysOfWeek = new HashSet<String>(Arrays.asList("Sunday", "Monday", "Tuesday",
                        "Wednesday", "Thursday", "Friday", "Saturday"));
        assertTrue(daysOfWeek.contains(response.getText()));
    }
}
