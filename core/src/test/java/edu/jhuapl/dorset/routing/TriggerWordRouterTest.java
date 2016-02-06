package edu.jhuapl.dorset.routing;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Properties;

import org.junit.Test;

import edu.jhuapl.dorset.Request;
import edu.jhuapl.dorset.agent.Agent;
import edu.jhuapl.dorset.agent.AgentRegistry;

public class TriggerWordRouterTest {

    @Test
    public void testRouting() {
        Agent agent = mock(Agent.class);
        when(agent.getName()).thenReturn("twitter");
        AgentRegistry registry = new AgentRegistry();
        registry.register(agent, new Properties());
        Router router = new TriggerWordRouter();
        router.initialize(registry);

        Agent agents[] = router.getAgents(new Request("twitter hello world"));

        Agent expected[] = new Agent[]{agent};
        assertArrayEquals(expected, agents);
    }

    @Test
    public void testRoutingWithNoAgentMatch() {
        Agent agent = mock(Agent.class);
        when(agent.getName()).thenReturn("twitter");
        AgentRegistry registry = new AgentRegistry();
        registry.register(agent, new Properties());
        Router router = new TriggerWordRouter();
        router.initialize(registry);

        Agent agents[] = router.getAgents(new Request("film hello world"));

        Agent expected[] = new Agent[0];
        assertArrayEquals(expected, agents);
    }

    @Test
    public void testRoutingWithBadRequest() {
        AgentRegistry registry = new AgentRegistry();
        Router router = new TriggerWordRouter();
        router.initialize(registry);

        Agent agents[] = router.getAgents(new Request("blah"));

        Agent expected[] = new Agent[0];
        assertArrayEquals(expected, agents);
    }

}
