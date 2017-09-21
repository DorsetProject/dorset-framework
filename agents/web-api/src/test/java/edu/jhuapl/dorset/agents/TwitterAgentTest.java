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
package edu.jhuapl.dorset.agents;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import org.junit.Test;

import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.oauth.OAuth10aService;

import edu.jhuapl.dorset.ResponseStatus;


public class TwitterAgentTest {
    private OAuth10aService getServiceMock(int code, String message) {
        OAuth10aService service = mock(OAuth10aService.class);
        Response resp = new Response(code, message, new HashMap<String, String>(), "");
        try {
            when(service.execute(any(OAuthRequest.class))).thenReturn(resp);
        } catch (InterruptedException | ExecutionException | IOException e) {
            e.printStackTrace();
            fail();
        }
        return service;
    }

    @Test
    public void testPostGood() {
        OAuth10aService service = getServiceMock(200, "OK");
        Agent agent = new TwitterAgent(service, null);
        String tweet = "This is a valid tweet";
        AgentRequest request = new AgentRequest("tweet" + tweet);

        AgentResponse response = agent.process(request);

        assertEquals("Tweeted " + tweet, response.getText());
    }

    @Test
    public void testInvalidCredentials() {
        OAuth10aService service = getServiceMock(401, "Authorization Required");
        Agent agent = new TwitterAgent(service, null);
        String tweet = "This is a valid tweet";
        AgentRequest request = new AgentRequest("tweet" + tweet);

        AgentResponse response = agent.process(request);

        assertEquals(ResponseStatus.Code.AGENT_INTERNAL_ERROR, response.getStatus().getCode());
        assertTrue(response.getStatus().getMessage().indexOf("Failed to authenticate") != -1);
    }

    @Test
    public void testPostLengthBad() {
        OAuth10aService service = getServiceMock(200, "OK");
        Agent agent = new TwitterAgent(service, null);
        String tweet = "This is an invalid tweet. This tweet is going to be too long, which means that this text needs to be longer than one hundred and forty characters...";
        AgentRequest request = new AgentRequest("tweet" + tweet);

        AgentResponse response = agent.process(request);

        assertEquals(ResponseStatus.Code.AGENT_CANNOT_COMPLETE_ACTION, response.getStatus().getCode());
        assertTrue(response.getStatus().getMessage().indexOf("too many characters") != -1);
    }
}
