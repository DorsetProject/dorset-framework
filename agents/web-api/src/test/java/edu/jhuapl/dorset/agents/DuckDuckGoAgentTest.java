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

public class DuckDuckGoAgentTest {

    @Test
    public void testGetGoodResponse() {
        String query = "Barack Obama";
        String jsonData = FileReader.getFileAsString("duckduckgo/barack_obama.json");
        HttpClient client = mock(HttpClient.class);
        when(client.execute(DuckDuckGoAgent.createUrl(query))).thenReturn(jsonData);

        Agent agent = new DuckDuckGoAgent(client);
        AgentResponse response = agent.process(new AgentRequest(query));

        assertTrue(response.isSuccess());
        assertTrue(response.getText().startsWith("Barack Hussein Obama II is an American politician"));
    }

    @Test
    public void testWithFullSentence() {
        String jsonData = FileReader.getFileAsString("duckduckgo/barack_obama.json");
        HttpClient client = mock(HttpClient.class);
        when(client.execute(any(HttpRequest.class))).thenReturn(jsonData);

        Agent agent = new DuckDuckGoAgent(client);
        AgentResponse response = agent.process(new AgentRequest("Who is Barack Obama?"));

        assertTrue(response.isSuccess());
        assertTrue(response.getText().startsWith("Barack Hussein Obama II is an American politician"));
    }

    @Test
    public void testGetDisambiguationResponse() {
        String query = "Obama";
        String jsonData = FileReader.getFileAsString("duckduckgo/obama.json");
        HttpClient client = mock(HttpClient.class);
        when(client.get(DuckDuckGoAgent.createUrl(query))).thenReturn(jsonData);

        Agent agent = new DuckDuckGoAgent(client);
        AgentResponse response = agent.process(new AgentRequest(query));

        assertEquals(ResponseStatus.Code.AGENT_DID_NOT_KNOW_ANSWER, response.getStatus().getCode());
    }

    @Test
    public void testGetEmptyResponse() {
        String query = "zergblah";
        String jsonData = FileReader.getFileAsString("duckduckgo/zergblah.json");
        HttpClient client = mock(HttpClient.class);
        when(client.get(DuckDuckGoAgent.createUrl(query))).thenReturn(jsonData);

        Agent agent = new DuckDuckGoAgent(client);
        AgentResponse response = agent.process(new AgentRequest(query));

        assertEquals(ResponseStatus.Code.AGENT_DID_NOT_KNOW_ANSWER, response.getStatus().getCode());
    }

    @Test
    public void testUrlEncoding() {
        String urlBase = "http://api.duckduckgo.com/?format=json&q=";
        assertEquals(urlBase + "Barack+Obama", DuckDuckGoAgent.createUrl("Barack Obama"));
    }

}
