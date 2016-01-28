package edu.jhuapl.dorset.routing;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import edu.jhuapl.dorset.Request;
import edu.jhuapl.dorset.agent.Agent;
import edu.jhuapl.dorset.agent.AgentRegistry;

public class FixedAgentRouterTest {
    @Test
    public void testGetAgents() {
        Agent agent = mock(Agent.class);
        when(agent.getName()).thenReturn("test");
        AgentRegistry registry = new AgentRegistry();
        registry.register(agent, null);
        Router router = new FixedAgentRouter("test");
        router.initialize(registry);

        Agent agents[] = router.getAgents(new Request("hello world"));

        Agent expected[] = new Agent[]{agent};
        assertArrayEquals(expected, agents);
    }
}
