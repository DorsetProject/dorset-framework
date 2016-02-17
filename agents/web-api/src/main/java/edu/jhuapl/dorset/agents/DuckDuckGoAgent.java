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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import edu.jhuapl.dorset.agent.AbstractAgent;
import edu.jhuapl.dorset.agent.AgentRequest;
import edu.jhuapl.dorset.agent.AgentResponse;
import edu.jhuapl.dorset.agent.Description;
import edu.jhuapl.dorset.http.HttpClient;

public class DuckDuckGoAgent extends AbstractAgent {
    private final Logger logger = LoggerFactory.getLogger(DuckDuckGoAgent.class);
    
    private static final String SUMMARY = "Get information about famous people, places, organizations.";
    private static final String EXAMPLE = "Who is Brack Obama?";

    private HttpClient client;

    public DuckDuckGoAgent(HttpClient client) {
        this.client = client;
        this.setDescription(new Description("movies", SUMMARY, EXAMPLE));
    }

    @Override
    public AgentResponse process(AgentRequest request) {
        logger.debug("Handling the request: " + request.getText());

        String agentRequest = request.getText();

        // TODO parse entity out of request text
        
        String data = requestData(agentRequest);
        
        return new AgentResponse(formatResponse(data));
    }

    protected String requestData(String entity) {
        try {
            entity = URLEncoder.encode(entity, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // this isn't going to happen
            logger.error("Unexpected exception when encoding url", e);
        }
        String url = "http://api.duckduckgo.com/?format=json&q=" + entity;
        return client.get(url);
    }

    protected String formatResponse(String json) {
        Gson gson = new Gson();
        JsonObject jsonObj = gson.fromJson(json, JsonObject.class);
        // TODO need more logic to handle cases where abstract is empty
        return jsonObj.get("AbstractText").getAsString();
    }
}
