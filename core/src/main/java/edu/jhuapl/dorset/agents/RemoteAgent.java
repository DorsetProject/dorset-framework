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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import edu.jhuapl.dorset.Response;
import edu.jhuapl.dorset.ResponseStatus;
import edu.jhuapl.dorset.http.ContentType;
import edu.jhuapl.dorset.http.HttpClient;
import edu.jhuapl.dorset.http.HttpMethod;
import edu.jhuapl.dorset.http.HttpRequest;
import edu.jhuapl.dorset.http.HttpResponse;

/**
 * Agent wrapper for remote web services that implement the agent API
 */
public class RemoteAgent extends AbstractAgent {
    private static final String REQUEST_ENDPOINT = "request";
    private static final String PING_ENDPOINT = "ping";
    private static final String PING_RESPONSE = "pong";

    private final Logger logger = LoggerFactory.getLogger(RemoteAgent.class);

    private String urlBase;
    private String requestUrl;
    private String pingUrl;
    private HttpClient client;
    private Gson gson;

    /**
     * Create a remote agent object
     *
     * @param urlBase  the base URL for the web services
     * @param client  the http client
     */
    public RemoteAgent(String urlBase, HttpClient client) {
        // make sure end of url has slash
        this.urlBase = urlBase.replaceAll("/$", "") + "/";
        this.client = client;
        setUrls();
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Response.Type.class, new ResponseTypeJsonAdapter().nullSafe());
        builder.registerTypeAdapter(ResponseStatus.Code.class, new ResponseCodeJsonAdapter().nullSafe());
        gson = builder.create();
    }

    @Override
    public AgentResponse process(AgentRequest request) {
        AgentResponse response;
        String json = gson.toJson(request);
        HttpRequest httpRequest = new HttpRequest(HttpMethod.POST, requestUrl).setBody(json,
                        ContentType.APPLICATION_JSON);
        HttpResponse httpResponse = client.execute(httpRequest);
        if (httpResponse != null) {
            String text = httpResponse.asString();
            try {
                response = gson.fromJson(text, AgentResponse.class);
                // the gson deserialization code is very permissive so we verify
                if (!response.isValid()) {
                    response = new AgentResponse(ResponseStatus.Code.INVALID_RESPONSE_FROM_AGENT);
                    logger.warn("Invalid json for request: " + text);
                }
            } catch (JsonSyntaxException e) {
                response = new AgentResponse(ResponseStatus.Code.INVALID_RESPONSE_FROM_AGENT);
                logger.warn("Invalid json for request: " + text);
            }
        } else {
            response = new AgentResponse(ResponseStatus.Code.NO_RESPONSE_FROM_AGENT);
        }
        return response;
    }

    /**
     * Is the remote agent available?
     *
     * @return boolean
     */
    public boolean ping() {
        HttpResponse httpResponse = client.execute(HttpRequest.get(pingUrl));
        if (httpResponse.isSuccess()) {
            String resp = httpResponse.asString();
            try {
                String text = gson.fromJson(resp, String.class);
                return text.equals(PING_RESPONSE);
            } catch (JsonSyntaxException e) {
                logger.warn("Invalid json for ping response: " + resp);
            }
        }
        return false;
    }

    /**
     * Get the URL base
     *
     * @return base URL
     */
    public String getUrlBase() {
        return urlBase;
    }

    private void setUrls() {
        requestUrl = urlBase + REQUEST_ENDPOINT;
        pingUrl = urlBase + PING_ENDPOINT;
    }
}
