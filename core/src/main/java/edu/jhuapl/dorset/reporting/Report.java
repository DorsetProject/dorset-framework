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
import edu.jhuapl.dorset.agents.Agent;

/**
 * A report of handling a request
 */
public class Report {
    public static final int NO_RESPONSE = -1;

    protected Date timestamp;
    protected String requestId;
    protected String requestText;
    protected String agentName;
    protected String responseText;
    protected int responseCode;
    protected long routeTime;
    protected long agentTime;

    /**
     * Default zero argument constructor to support usage as bean
     * <p>
     * For bean usage only
     */
    public Report() {}

    /**
     * Record of handling a request
     *
     * @param request  Request object
     */
    public Report(Request request) {
        timestamp = new Date();
        requestId = request.getId();
        requestText = request.getText();
        routeTime = 0;
        agentTime = 0;
        agentName = "";
        responseText = "";
        responseCode = NO_RESPONSE;
    }

    /**
     * Copy constructor
     *
     * @param report  the report to copy
     */
    public Report(Report report) {
        timestamp = report.timestamp;
        requestId = report.requestId;
        requestText = report.requestText;
        agentName = report.agentName;
        responseText = report.responseText;
        responseCode = report.responseCode;
        routeTime = report.routeTime;
        agentTime = report.agentTime;
    }

    /**
     * Set the request identifier
     * <p>
     * For bean use only
     *
     * @param id  request identifier
     */
    public void setRequestId(String id) {
        requestId = id;
    }

    /**
     * Get the request identifier
     *
     * @return request identifier
     * @see Request#MAX_ID_LENGTH length limitation of identifier
     */
    public String getRequestId() {
        return requestId;
    }

    /**
     * Set the request text
     * <p>
     * For bean use only
     *
     * @param text  request text
     */
    public void setRequestText(String text) {
        requestText = text;
    }

    /**
     * Get the text of the request
     *
     * @return text of the request
     */
    public String getRequestText() {
        return requestText;
    }

    /**
     * Set the length of time that the routing took
     * <p>
     * Use System.nanoTime() to get the start and stop times.
     * 
     * @param start  The time the routing started
     * @param stop  The time the routing stopped
     */
    public void setRouteTime(long start, long stop) {
        routeTime = stop - start;
    }

    /**
     * Set the length of time that the routing took
     *
     * @param time  The length of time for the routing 
     */
    public void setRouteTime(long time) {
        routeTime = time;
    }

    /**
     * Get the length of time that the routing took
     *
     * @return time in nano-seconds
     */
    public long getRouteTime() {
        return routeTime;
    }

    /**
     * Set the length of time the agent took to handle the request
     * <p>
     * Use System.nanoTime() to get the start and stop times.
     *
     * @param start  The time the agent started
     * @param stop  The time the agent stopped
     */
    public void setAgentTime(long start, long stop) {
        agentTime = stop - start;
    }

    /**
     * Set the length of time that the agent took
     *
     * @param time  The length of time for agent processing 
     */
    public void setAgentTime(long time) {
        agentTime = time;
    }

    /**
     * Get the length of time the agent took to handle the request
     *
     * @return time in nano-seconds
     */
    public long getAgentTime() {
        return agentTime;
    }

    /**
     * Set the agent that handled the request
     *
     * @param agent  Agent object
     */
    public void setAgent(Agent agent) {
        agentName = agent.getName();
    }

    /**
     * Set the agent name that handled the request
     *
     * @param name  the name of the agent
     */
    public void setAgentName(String name) {
        agentName = name;
    }

    /**
     * Get the agent that handled the request
     *
     * @return name of the agent
     */
    public String getAgentName() {
        return agentName;
    }

    /**
     * Set the text of the response
     *
     * @param response  Response object
     */
    public void setResponse(Response response) {
        if (response.getText() != null) {
            responseText = response.getText();
        }
        responseCode = response.getStatus().getCode().getValue();
    }

    /**
     * Set the text of the response
     *
     * @param text  Text of the response
     */
    public void setResponseText(String text) {
        responseText = text;
    }

    /**
     * Get the text of the response
     *
     * @return text of the response
     */
    public String getResponseText() {
        return responseText;
    }

    /**
     * Set the response code
     *
     * @param code  response code
     */
    public void setResponseCode(int code) {
        responseCode = code;
    }

    /**
     * Get the response code
     *
     * @return response code or -1 if not set
     */
    public int getResponseCode() {
        return responseCode;
    }

    /**
     * Set the timestamp of the report
     * <p>
     * For bean use only
     *
     * @param ts  time of the request being received
     */
    public void setTimestamp(Date ts) {
        timestamp = new Date(ts.getTime());
    }

    /**
     * Get the timestamp of the report
     *
     * @return Date of the report
     */
    public Date getTimestamp() {
        return new Date(timestamp.getTime());
    }
}
