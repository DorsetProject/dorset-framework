package edu.jhuapl.dorset.routing;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import org.junit.Test;

import edu.jhuapl.dorset.Request;
import edu.jhuapl.dorset.agent.Agent;
import edu.jhuapl.dorset.config.MultiValuedMap;

public class TriggerWordRouterTest {

    @Test
    public void testRouting() {
        Agent agent = mock(Agent.class);
        MultiValuedMap params = new MultiValuedMap();
        params.addString(TriggerWordRouter.TRIGGERS, "twitter");
        RouterAgentConfig config = RouterAgentConfig.create().add(agent, params);
        Router router = new TriggerWordRouter(config);

        Agent agents[] = router.route(new Request("twitter hello world"));

        Agent expected[] = new Agent[]{agent};
        assertArrayEquals(expected, agents);
    }

    @Test
    public void testRoutingWithNoAgentMatch() {
        Agent agent = mock(Agent.class);
        MultiValuedMap params = new MultiValuedMap();
        params.addString(TriggerWordRouter.TRIGGERS, "twitter");
        RouterAgentConfig config = RouterAgentConfig.create().add(agent, params);
        Router router = new TriggerWordRouter(config);

        Agent agents[] = router.route(new Request("film hello world"));

        Agent expected[] = new Agent[0];
        assertArrayEquals(expected, agents);
    }

    @Test
    public void testRoutingWithEmptyRequest() {
        Router router = new TriggerWordRouter(RouterAgentConfig.create());

        Agent agents[] = router.route(new Request(""));

        Agent expected[] = new Agent[0];
        assertArrayEquals(expected, agents);
    }

    @Test
    public void testMissingTrigger() {
        Agent agent1 = mock(Agent.class);
        MultiValuedMap params1 = new MultiValuedMap();
        params1.addString(TriggerWordRouter.TRIGGERS, "twitter");
        Agent agent2 = mock(Agent.class);
        MultiValuedMap params2 = new MultiValuedMap();
        RouterAgentConfig config = RouterAgentConfig.create().add(agent1, params1).add(agent2, params2);

        Router router = new TriggerWordRouter(config);

        assertEquals(1, router.getAgents().length);
    }

}
