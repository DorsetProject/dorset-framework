/*
 * Copyright 2017 The Johns Hopkins University Applied Physics Laboratory LLC
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
package edu.jhuapl.dorset.agents.duckduckgo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import edu.jhuapl.dorset.ResponseStatus;
import edu.jhuapl.dorset.agents.Agent;
import edu.jhuapl.dorset.agents.AgentRequest;
import edu.jhuapl.dorset.agents.AgentResponse;
import edu.jhuapl.dorset.agents.FakeHttpClient;
import edu.jhuapl.dorset.agents.FakeHttpResponse;
import edu.jhuapl.dorset.agents.FileReader;
import edu.jhuapl.dorset.http.HttpClient;
import edu.jhuapl.dorset.sessions.Session;
import edu.jhuapl.dorset.sessions.Session.SessionStatus;

public class DuckDuckGoAgentTest {

    @Test
    public void testGetGoodResponse() {
        String query = "Barack Obama";
        String jsonData = FileReader.getFileAsString("duckduckgo/barack_obama.json");
        HttpClient client = new FakeHttpClient(new FakeHttpResponse(jsonData));

        Agent agent = new DuckDuckGoAgent(client);
        AgentResponse response = agent.process(new AgentRequest(query));

        assertTrue(response.isSuccess());
        assertTrue(response.getText()
                        .startsWith("Barack Hussein Obama II is an American politician"));
    }

    @Test
    public void testWithFullSentence() {
        String jsonData = FileReader.getFileAsString("duckduckgo/barack_obama.json");
        HttpClient client = new FakeHttpClient(new FakeHttpResponse(jsonData));

        Agent agent = new DuckDuckGoAgent(client);
        AgentResponse response = agent.process(new AgentRequest("Who is Barack Obama?"));

        assertTrue(response.isSuccess());
        assertTrue(response.getText()
                        .startsWith("Barack Hussein Obama II is an American politician"));
    }

    @Test
    public void testGetDisambiguationResponse() {
        String query = "Obama";
        String jsonData = FileReader.getFileAsString("duckduckgo/obama.json");
        HttpClient client = new FakeHttpClient(new FakeHttpResponse(jsonData));

        Agent agent = new DuckDuckGoAgent(client);
        AgentResponse response = agent.process(new AgentRequest(query));

        assertEquals(ResponseStatus.Code.NEEDS_REFINEMENT, response.getStatus().getCode());
        assertEquals("Multiple answers for this question. Did you mean 'Barack Obama', 'Obama, Fukui', "
                        + " or 'Obama Day'?", response.getStatus().getMessage());
        assertEquals(SessionStatus.OPEN, response.getSessionStatus());

    }

    @Test
    public void testGetEmptyResponse() {
        String query = "zergblah";
        String jsonData = FileReader.getFileAsString("duckduckgo/zergblah.json");
        HttpClient client = new FakeHttpClient(new FakeHttpResponse(jsonData));

        Agent agent = new DuckDuckGoAgent(client);
        AgentResponse response = agent.process(new AgentRequest(query));

        assertEquals(ResponseStatus.Code.AGENT_DID_NOT_KNOW_ANSWER, response.getStatus().getCode());
    }

    @Test
    public void testUrlEncoding() {
        String urlBase = "http://api.duckduckgo.com/?format=json&q=";
        assertEquals(urlBase + "Barack+Obama", DuckDuckGoAgent.createUrl("Barack Obama"));
    }

    @Test
    public void testFollowOnResponseWithSession() {
        String firstQuery = "Obama";
        String secondQuery = "Barack Obama";
        String jsonData = FileReader.getFileAsString("duckduckgo/obama.json");
        HttpClient client = new FakeHttpClient(new FakeHttpResponse(jsonData));

        Session session = new Session("1");
        session.setSessionStatus(SessionStatus.NEW);

        Agent agent = new DuckDuckGoAgent(client);
        AgentRequest agentRequest = new AgentRequest(firstQuery);
        agentRequest.setSession(session);
        AgentResponse response = agent.process(agentRequest);

        assertEquals(ResponseStatus.Code.NEEDS_REFINEMENT, response.getStatus().getCode());
        assertEquals("Multiple answers for this question. Did you mean 'Barack Obama', "
                        + "'Obama, Fukui',  or 'Obama Day'?", response.getStatus().getMessage());
        assertEquals(SessionStatus.OPEN, response.getSessionStatus());

        session.setSessionStatus(SessionStatus.OPEN);

        AgentRequest secondAgentRequest = new AgentRequest(secondQuery);
        secondAgentRequest.setSession(session);

        response = agent.process(secondAgentRequest);

        assertEquals("Barack Obama The 44th and current President of the United States, "
                        + "as well as the first African American to...", response.getText());
        assertEquals(SessionStatus.CLOSED, response.getSessionStatus());
    }

    @Test
    public void testMultipleFollowOnResponseWithSession() {
        String firstQuery = "Obama";
        String followUpQuery = "off-topic request";
        String jsonData = FileReader.getFileAsString("duckduckgo/obama.json");
        HttpClient client = new FakeHttpClient(new FakeHttpResponse(jsonData));

        Session session = new Session("1");
        session.setSessionStatus(SessionStatus.NEW);

        DuckDuckGoAgent agent = new DuckDuckGoAgent(client);
        int numFollowUpAttemptsThreshold = 2;
        agent.setNumFollowUpAttemptsThreshold(numFollowUpAttemptsThreshold);
        
        AgentRequest agentRequest = new AgentRequest(firstQuery);
        agentRequest.setSession(session);
        AgentResponse response = agent.process(agentRequest);

        assertEquals(ResponseStatus.Code.NEEDS_REFINEMENT, response.getStatus().getCode());
        assertEquals("Multiple answers for this question. Did you mean 'Barack Obama', "
                        + "'Obama, Fukui',  or 'Obama Day'?", response.getStatus().getMessage());
        assertEquals(SessionStatus.OPEN, response.getSessionStatus());

        session.setSessionStatus(SessionStatus.OPEN);
        for (int i = 0; i < numFollowUpAttemptsThreshold - 1; i++){
            AgentRequest followUpAgentRequest = new AgentRequest(followUpQuery);
            followUpAgentRequest.setSession(session);
            response = agent.process(followUpAgentRequest);
            
            assertEquals(ResponseStatus.Code.NEEDS_REFINEMENT, response.getStatus().getCode());
            assertEquals("I am sorry, I am still unsure what you are asking about. The options are: "
                            + "Did you mean 'Barack Obama', 'Obama, Fukui',  or 'Obama Day'?", response.getStatus().getMessage());
            assertEquals(SessionStatus.OPEN, response.getSessionStatus());
        }

        AgentRequest followUpAgentRequest = new AgentRequest(followUpQuery);
        followUpAgentRequest.setSession(session);
        response = agent.process(followUpAgentRequest);
        
        assertEquals(ResponseStatus.Code.AGENT_DID_NOT_KNOW_ANSWER, response.getStatus().getCode());
        assertEquals("The agent did not know the answer. Session is now closed, "
                        + "please try again.", response.getStatus().getMessage());
        assertEquals(SessionStatus.CLOSED, response.getSessionStatus());
    }
}
