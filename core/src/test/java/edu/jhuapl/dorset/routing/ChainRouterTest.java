package edu.jhuapl.dorset.routing;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;

import edu.jhuapl.dorset.Request;
import edu.jhuapl.dorset.agent.Agent;

public class ChainRouterTest {

    @Test
    public void testGetAgentsWithFirstEmptyResponse() {
        Router r1 = mock(Router.class);
        Router r2 = mock(Router.class);
        Agent a = mock(Agent.class);
        Agent[] resp = {a};
        Request request = mock(Request.class);
        when(r1.getAgents(request)).thenReturn(new Agent[0]);
        when(r2.getAgents(request)).thenReturn(resp);
        Router router = new ChainRouter(r1, r2);

        assertArrayEquals(resp, router.getAgents(request));
    }

    @Test
    public void testGetAgentsWithNoEmptyResponse() {
        Router r1 = mock(Router.class);
        Router r2 = mock(Router.class);
        Agent a1 = mock(Agent.class);
        Agent a2 = mock(Agent.class);
        Agent[] resp1 = {a1};
        Agent[] resp2 = {a2};
        Request request = mock(Request.class);
        when(r1.getAgents(request)).thenReturn(resp1);
        when(r2.getAgents(request)).thenReturn(resp2);
        Router router = new ChainRouter(r1, r2);

        assertArrayEquals(resp1, router.getAgents(request));
    }

}
