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
import static org.mockito.Mockito.*;

import org.junit.Test;

import edu.jhuapl.dorset.ResponseStatus;
import edu.jhuapl.dorset.agents.AgentRequest;
import edu.jhuapl.dorset.agents.AgentResponse;
import edu.jhuapl.dorset.agents.RemoteAgent;
import edu.jhuapl.dorset.http.HttpClient;

public class RemoteAgentTest {
/*
    @Test
    public void testPing() {
        HttpClient client = mock(HttpClient.class);
        when(client.get("http://example.org/ping")).thenReturn("pong").thenReturn(null)
                        .thenReturn("{\"pong\"}");
        RemoteAgent agent = new RemoteAgent("http://example.org/", client);

        assertTrue(agent.ping());
        assertFalse(agent.ping());
        assertFalse(agent.ping());
    }

    @Test
    public void testProcess() {
        HttpClient client = mock(HttpClient.class);
        when(client.post(eq("http://example.org/request"), any(String.class),
                        eq(HttpClient.APPLICATION_JSON))).thenReturn("{\"type\":\"text\",\"text\":\"2\", \"status\":{\"code\":0, \"message\":\"Success\"}}");
        RemoteAgent agent = new RemoteAgent("http://example.org/", client);
        AgentRequest request = new AgentRequest("what is 1 + 1?");

        AgentResponse response = agent.process(request);

        assertEquals("2", response.getText());
        assertEquals(ResponseStatus.createSuccess(), response.getStatus());
    }

    @Test
    public void testProcessWithInvalidJson() {
        HttpClient client = mock(HttpClient.class);
        when(client.post(eq("http://example.org/request"), any(String.class),
                        eq(HttpClient.APPLICATION_JSON))).thenReturn("{\"ans\":, \"statusCode\":0}");
        RemoteAgent agent = new RemoteAgent("http://example.org/", client);
        AgentRequest request = new AgentRequest("what is 1 + 1?");

        AgentResponse response = agent.process(request);

        assertNull(response.getText());
        assertEquals(ResponseStatus.Code.INVALID_RESPONSE_FROM_AGENT, response.getStatus().getCode());
    }

    @Test
    public void testProcessWithInvalidResponse() {
        HttpClient client = mock(HttpClient.class);
        when(client.post(eq("http://example.org/request"), any(String.class),
                        eq(HttpClient.APPLICATION_JSON))).thenReturn("{\"ans\":\"2\", \"statusCode\":0}");
        RemoteAgent agent = new RemoteAgent("http://example.org/", client);
        AgentRequest request = new AgentRequest("what is 1 + 1?");

        AgentResponse response = agent.process(request);

        assertNull(response.getText());
        assertEquals(ResponseStatus.Code.INVALID_RESPONSE_FROM_AGENT, response.getStatus().getCode());
    }

    @Test
    public void testUrlForming() {
        HttpClient client = mock(HttpClient.class);
        when(client.get("http://example.org/ping")).thenReturn("pong").thenReturn(null)
                        .thenReturn("{\"pong\"}");
        // passing url without trailing slash to make sure we add it
        RemoteAgent agent = new RemoteAgent("http://example.org", client);

        assertTrue(agent.ping());
    }
    */
}
