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

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import edu.jhuapl.dorset.agents.Agent;
import edu.jhuapl.dorset.agents.AgentRequest;
import edu.jhuapl.dorset.agents.AgentResponse;
import edu.jhuapl.dorset.filters.RequestFilter;
import edu.jhuapl.dorset.filters.WakeupRequestFilter;
import edu.jhuapl.dorset.routing.Router;
import edu.jhuapl.dorset.routing.SingleAgentRouter;

public class ApplicationTest {
    @Test
    public void testProcessWithNoAgents() {
        Request request = new Request("test");
        Router router = mock(Router.class);
        when(router.route(request)).thenReturn(new Agent[0]);
        Application app = new Application(router);

        Response response = app.process(request);

        assertFalse(response.isSuccess());
        assertEquals(ResponseStatus.Code.NO_AVAILABLE_AGENT, response.getStatus().getCode());
    }

    @Test
    public void testProcessWithAgentWithResponse() {
        Request request = new Request("test");
        Agent agent = mock(Agent.class);
        when(agent.process((AgentRequest)anyObject())).thenReturn(new AgentResponse("the answer"));

        Agent rtn[] = new Agent[1];
        rtn[0] = agent;
        Router router = mock(Router.class);
        when(router.route(request)).thenReturn(rtn);
        Application app = new Application(router);

        Response response = app.process(request);

        assertEquals("the answer", response.getText());
    }

    @Test
    public void testProcessWithAgentWithNoResponse() {
        Request request = new Request("test");
        Agent agent = mock(Agent.class);
        when(agent.process((AgentRequest)anyObject())).thenReturn(null);

        Agent rtn[] = new Agent[1];
        rtn[0] = agent;
        Router router = mock(Router.class);
        when(router.route(request)).thenReturn(rtn);
        Application app = new Application(router);

        Response response = app.process(request);

        assertFalse(response.isSuccess());
        assertEquals(ResponseStatus.Code.NO_RESPONSE_FROM_AGENT, response.getStatus().getCode());
    }

    @Test
    public void testAddingRequestFilter() {
        RequestFilter filter = new WakeupRequestFilter("Dorset");
        Agent agent = mock(Agent.class);
        when(agent.process((AgentRequest)anyObject())).thenAnswer(new Answer<AgentResponse>() {
            @Override
            public AgentResponse answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                return new AgentResponse(((AgentRequest)args[0]).getText());
            }
        });
        Router router = new SingleAgentRouter(agent);
        Application app = new Application(router);
        app.addRequestFilter(filter);

        Request request = new Request("Dorset test");
        Response response = app.process(request);

        assertEquals("test", response.getText());
    }

    @Test
    public void testShutdown() {
        Router router = mock(Router.class);
        Application app = new Application(router);
        ShutdownListener listener = mock(ShutdownListener.class);
        app.addShutdownListener(listener);

        app.shutdown();

        verify(listener, times(1)).shutdown();
    }
}
