package edu.jhuapl.dorset.agents;

import static org.junit.Assert.*;


import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import static org.junit.matchers.JUnitMatchers.*;

import org.junit.Test;

import edu.jhuapl.dorset.agent.AgentRequest;
import edu.jhuapl.dorset.agent.AgentResponse;

public class DateTimeAgentTest {

	private Date testDate; 
	
	public DateTimeAgentTest(){
    	Calendar cal = new GregorianCalendar(2016, Calendar.JANUARY, 1, 1, 0, 0);
    	cal.set(Calendar.DAY_OF_WEEK, 2);
    	this.testDate =cal.getTime();
	}
	
	@Test
	public void testProcess() {
		String testErrorString = "there are no good words here";
    	String testDateString = "What is today's date?";
    	String testDayString = "What is of the the week is it today?";
    	String testTimeString = "What is the time right now?";
    	
    	AgentRequest request = new AgentRequest(testErrorString); 
		DateTimeAgent dayTimeAgent = new DateTimeAgent(this.testDate);
		AgentResponse testAgentResponse = dayTimeAgent.process(request);
		assertEquals(testAgentResponse.text, "Request must contain words time, date or day.");
	}
	

	@Test
	public void testGetFunctionName(){
		String getFunctionNameTestString = "date test me";
		DateTimeAgent dayTimeAgent = new DateTimeAgent(this.testDate);		
		assertThat(Arrays.asList("getTime", "getDate", "getDay"), hasItem(dayTimeAgent.getFunctionName(getFunctionNameTestString)));
	}
	
	@Test
	public void testGetTime(){
			DateTimeAgent dayTimeAgent = new DateTimeAgent(this.testDate);	
			assertEquals(dayTimeAgent.getTime(),"1:00 AM");
	}

	@Test
	public void testGetDay(){
		DateTimeAgent dayTimeAgent = new DateTimeAgent(this.testDate);
    	assertEquals(dayTimeAgent.getDay(), "Friday");
	}
	
	@Test
	public void testGetDate(){
		DateTimeAgent dayTimeAgent = new DateTimeAgent(this.testDate);
    	assertEquals(dayTimeAgent.getDate(), "January 01, 2016");
	}
	
	@Test
	public void testErrorFunc(){
		String testErrorString = "there are no good words here";
		AgentRequest request = new AgentRequest(testErrorString); 
		DateTimeAgent dayTimeAgent = new DateTimeAgent(this.testDate);
		assertEquals(dayTimeAgent.errorFunc(), "Request must contain words time, date or day.");
		
	}
}
