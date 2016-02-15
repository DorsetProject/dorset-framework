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

import edu.jhuapl.dorset.agent.AgentMessages;
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
        assertEquals(AgentMessages.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testProcessForDayOfWeek() {
        AgentResponse response = agent.process(new AgentRequest("what is today?"));
        assertNotNull(response);
        assertEquals(AgentMessages.SUCCESS, response.getStatusCode());
        Set<String> daysOfWeek = new HashSet<String>(Arrays.asList("Sunday", "Monday", "Tuesday",
                        "Wednesday", "Thursday", "Friday", "Saturday"));
        assertTrue(daysOfWeek.contains(response.getText()));
    }
}
