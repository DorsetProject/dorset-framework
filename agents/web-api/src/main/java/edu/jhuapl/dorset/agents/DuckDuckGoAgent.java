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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import edu.jhuapl.dorset.agent.AbstractAgent;
import edu.jhuapl.dorset.agent.AgentRequest;
import edu.jhuapl.dorset.agent.AgentResponse;
import edu.jhuapl.dorset.agent.Description;
import edu.jhuapl.dorset.http.HttpClient;
import edu.jhuapl.dorset.nlp.BasicTokenizer;
import edu.jhuapl.dorset.nlp.Tokenizer;

/**
 * DuckduckGo agent
 *
 * Duckduckgo has an instant answers API. It scrapes entity information from
 * data sources like Wikipedia and CrunchBase. Documentation on the api here:
 * https://duckduckgo.com/api
 */
public class DuckDuckGoAgent extends AbstractAgent {
    private final Logger logger = LoggerFactory.getLogger(DuckDuckGoAgent.class);

    private static final String SUMMARY =
                    "Get information about famous people, places, organizations.";
    private static final String EXAMPLE = "Who is Brack Obama?";

    private HttpClient client;
    private Set<String> dictionary = new HashSet<String>(
            Arrays.asList("what", "who", "is", "are", "a", "an", "the"));

    /**
     * Create a duckduckgo agent
     * @param client http client
     */
    public DuckDuckGoAgent(HttpClient client) {
        this.client = client;
        this.setDescription(new Description("general answers", SUMMARY, EXAMPLE));
    }

    @Override
    public AgentResponse process(AgentRequest request) {
        logger.debug("Handling the request: " + request.getText());

        String requestText = request.getText();
        String entityText = extractEntity(requestText);
        String data = requestData(entityText);

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

    /**
     * Iterate over the words until we think we get to the name of the entity
     */
    protected String extractEntity(String sentence) {
        Tokenizer tokenizer = new BasicTokenizer();
        String[] words = tokenizer.tokenize(sentence);
        int index = 0;
        for (index = 0; index < words.length; index++) {
            if (!dictionary.contains(words[index].toLowerCase())) {
                break;
            }
        }
        return joinStrings(Arrays.copyOfRange(words, index, words.length), " ");
    }

    protected String joinStrings(String[] strings, String separator) {
        if (strings == null || strings.length == 0) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        sb.append(strings[0]);
        for (int i = 1; i < strings.length; i++) {
            sb.append(separator);
            sb.append(strings[i]);
        }
        return sb.toString();
    }
}
