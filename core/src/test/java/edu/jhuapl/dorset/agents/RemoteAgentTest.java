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
import edu.jhuapl.dorset.http.HttpRequest;
import edu.jhuapl.dorset.http.HttpResponse;

public class RemoteAgentTest {

    private HttpClient getMockClient(String json) {
        HttpResponse httpResponse = mock(HttpResponse.class);
        when(httpResponse.asString()).thenReturn(json);
        HttpClient client = mock(HttpClient.class);
        when(client.execute(any(HttpRequest.class))).thenReturn(httpResponse);
        return client;
    }

    @Test
    public void testPing() {
        HttpResponse httpResponse1 = mock(HttpResponse.class);
        when(httpResponse1.asString()).thenReturn("\"pong\"");
        when(httpResponse1.isSuccess()).thenReturn(true);
        HttpResponse httpResponse2 = mock(HttpResponse.class);
        when(httpResponse2.asString()).thenReturn("{\"pong\"}");
        when(httpResponse2.isSuccess()).thenReturn(true);
        HttpResponse httpResponse3 = mock(HttpResponse.class);
        when(httpResponse3.asString()).thenReturn(null);
        when(httpResponse3.isSuccess()).thenReturn(false);
        HttpClient client = mock(HttpClient.class);
        when(client.execute(any(HttpRequest.class))).thenReturn(httpResponse1)
                        .thenReturn(httpResponse2).thenReturn(httpResponse3);
        RemoteAgent agent = new RemoteAgent("http://example.org/", client);

        assertTrue(agent.ping());
        assertFalse(agent.ping());
        assertFalse(agent.ping());
    }

    @Test
    public void testProcess() {
        String json = "{\"type\":\"text\",\"text\":\"2\", \"status\":{\"code\":0, \"message\":\"Success\"}}";
        HttpClient client = getMockClient(json);
        RemoteAgent agent = new RemoteAgent("http://example.org/", client);
        AgentRequest request = new AgentRequest("what is 1 + 1?");

        AgentResponse response = agent.process(request);

        assertEquals("2", response.getText());
        assertEquals(ResponseStatus.createSuccess(), response.getStatus());
    }

    @Test
    public void testProcessWithInvalidJson() {
        HttpClient client = getMockClient("{\"ans\":, \"statusCode\":0}");
        RemoteAgent agent = new RemoteAgent("http://example.org/", client);
        AgentRequest request = new AgentRequest("what is 1 + 1?");

        AgentResponse response = agent.process(request);

        assertNull(response.getText());
        assertEquals(ResponseStatus.Code.INVALID_RESPONSE_FROM_AGENT, response.getStatus().getCode());
    }

    @Test
    public void testProcessWithInvalidResponse() {
        HttpClient client = getMockClient("{\"ans\":\"2\", \"statusCode\":0}");
        RemoteAgent agent = new RemoteAgent("http://example.org/", client);
        AgentRequest request = new AgentRequest("what is 1 + 1?");

        AgentResponse response = agent.process(request);

        assertNull(response.getText());
        assertEquals(ResponseStatus.Code.INVALID_RESPONSE_FROM_AGENT, response.getStatus().getCode());
    }
}
