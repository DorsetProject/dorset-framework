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
import edu.jhuapl.dorset.agents.Agent;
import edu.jhuapl.dorset.agents.AgentRequest;
import edu.jhuapl.dorset.agents.AgentResponse;
import edu.jhuapl.dorset.http.HttpClient;
import edu.jhuapl.dorset.http.HttpRequest;
import edu.jhuapl.dorset.http.HttpResponse;

public class FlickrAgentTest {

    @Test
    public void testGoodResponse() {        
        String query = "Show me an apple";
        String jsonData = FileReader.getFileAsString("flickr/apple.json");
        byte[] image = FileReader.getFileAsBytes("flickr/15429554069_08c6748145_z.jpg");
        HttpResponse httpResponse1 = mock(HttpResponse.class);
        when(httpResponse1.isSuccess()).thenReturn(true);
        when(httpResponse1.asString()).thenReturn(jsonData);
        HttpResponse httpResponse2 = mock(HttpResponse.class);
        when(httpResponse2.isSuccess()).thenReturn(true);
        when(httpResponse2.asBytes()).thenReturn(image);
        HttpClient client = mock(HttpClient.class);
        when(client.execute(any(HttpRequest.class))).thenReturn(httpResponse1)
                        .thenReturn(httpResponse2);

        Agent agent = new FlickrAgent(client, "key");
        AgentResponse response = agent.process(new AgentRequest(query));

        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertNotNull(response.getPayload());
        assertTrue(response.getPayload().startsWith("/9j/4AAQSkZJRgABAQEASABIAAD//gAPTGF2YzU0"));
    }

    @Test
    public void testBadRequest() {
        String query = "Go jump in a lake";
        HttpClient client = mock(HttpClient.class);

        Agent agent = new FlickrAgent(client, "key");
        AgentResponse response = agent.process(new AgentRequest(query));

        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertEquals(ResponseStatus.Code.AGENT_DID_NOT_UNDERSTAND_REQUEST, response.getStatus().getCode());
    }

    @Test
    public void testRegex() {
        HttpClient client = mock(HttpClient.class);
        FlickrAgent agent = new FlickrAgent(client, "key");

        assertEquals("apple", agent.getSearchPhrase("show me an apple"));
        assertEquals("apple", agent.getSearchPhrase("Show me an apple"));
        assertEquals("apple", agent.getSearchPhrase("Show me a apple"));
        assertEquals("apple", agent.getSearchPhrase("Show me the apple"));
        assertEquals("apple", agent.getSearchPhrase("Show me apple"));
        assertEquals("apple", agent.getSearchPhrase("Show me a apple "));
        assertEquals("apple pie", agent.getSearchPhrase("Show me an apple pie"));
    }
}
