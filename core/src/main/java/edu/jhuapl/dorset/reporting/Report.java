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
package edu.jhuapl.dorset.reporting;

import java.util.Date;

import edu.jhuapl.dorset.Request;
import edu.jhuapl.dorset.Response;
import edu.jhuapl.dorset.agent.Agent;

/**
 * A report of handling a request
 */
public class Report {
    private Date timestamp;
    private long routeTime;
    private long agentTime;
    private String requestText;
    private String[] agentNames;
    private String selectedAgentName;
    private String responseText;

    /**
     * Default zero argument constructor to support usage as bean
     *
     * For bean usage only
     */
    public Report() {}

    /**
     * Record of handling a request
     * @param request Request object
     */
    public Report(Request request) {
        timestamp = new Date();
        requestText = request.getText();
        routeTime = 0;
        agentTime = 0;
        agentNames = new String[0];
        selectedAgentName = "";
        responseText = "";
    }

    /**
     * Set the text of the request
     *
     * For bean usage only
     *
     * @param text request text
     */
    public void setRequestText(String text) {
        requestText = text;
    }

    /**
     * Get the text of the request
     * @return text of the request
     */
    public String getRequestText() {
        return requestText;
    }

    /**
     * Set the length of time that the routing took
     * 
     * Use System.nanoTime() to get the start and stop times.
     * 
     * @param start The time the routing started
     * @param stop The time the routing stopped
     */
    public void setRouteTime(long start, long stop) {
        routeTime = stop - start;
    }

    /**
     * Set the length of time that the routing took
     * @param time The length of time for the routing 
     */
    public void setRouteTime(long time) {
        routeTime = time;
    }

    /**
     * Get the length of time that the routing took
     * @return time in nano-seconds
     */
    public long getRouteTime() {
        return routeTime;
    }

    /**
     * Set the length of time the agent took to handle the request
     * 
     * Use System.nanoTime() to get the start and stop times.
     * 
     * @param start The time the agent started
     * @param stop The time the agent stopped
     */
    public void setAgentTime(long start, long stop) {
        agentTime = stop - start;
    }

    /**
     * Set the length of time that the agent took
     * @param time The length of time for agent processing 
     */
    public void setAgentTime(long time) {
        agentTime = time;
    }

    /**
     * Get the length of time the agent took to handle the request
     * @return time in nano-seconds
     */
    public long getAgentTime() {
        return agentTime;
    }

    /**
     * Set the agents the router nominated to handle the request
     * @param agents Array of Agent objects
     */
    public void setAgents(Agent[] agents) {
        agentNames = new String[agents.length];
        for (int i = 0; i < agents.length; i++) {
            agentNames[i] = agents[i].getName();
        }
    }

    /**
     * Set the agent that handled the request
     * @param agent Agent object
     */
    public void setSelectedAgent(Agent agent) {
        selectedAgentName = agent.getName();
    }

    /**
     * Set the agent name that handled the request
     * @param name the name of the agent
     */
    public void setSelectedAgentName(String name) {
        selectedAgentName = name;
    }

    /**
     * Get the agent that handled the request
     * @return name of the agent
     */
    public String getSelectedAgentName() {
        return selectedAgentName;
    }

    /**
     * Set the text of the response
     * @param response Response object
     */
    public void setResponse(Response response) {
        responseText = response.getText();
    }

    /**
     * Set the text of the response
     * @param text Text of the response
     */
    public void setResponseText(String text) {
        responseText = text;
    }

    /**
     * Get the text of the response
     * @return text of the response
     */
    public String getResponseText() {
        return responseText;
    }

    /**
     * Set the timestamp of the report
     *
     * For bean usage only
     *
     * @param ts time of the request being received
     */
    public void setTimestamp(Date ts) {
        timestamp = ts;
    }

    /**
     * Get the timestamp of the report
     * @return Date of the report
     */
    public Date getTimestamp() {
        return timestamp;
    }
}
