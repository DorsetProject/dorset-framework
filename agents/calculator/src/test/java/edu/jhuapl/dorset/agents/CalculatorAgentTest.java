package edu.jhuapl.dorset.agents;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.jhuapl.dorset.agent.Agent;
import edu.jhuapl.dorset.agent.AgentRequest;
import edu.jhuapl.dorset.agent.AgentResponse;

public class CalculatorAgentTest {

    @Test
    public void testSimpleAddition() {
        Agent agent = new CalculatorAgent();

        AgentResponse response = agent.process(new AgentRequest("56 + 89"));

        assertEquals("145", response.text);
    }

}
