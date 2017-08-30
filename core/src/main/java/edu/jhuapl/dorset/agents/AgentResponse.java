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

import edu.jhuapl.dorset.Response;
import edu.jhuapl.dorset.ResponseStatus;
import edu.jhuapl.dorset.sessions.Session;
import edu.jhuapl.dorset.sessions.Session.SessionStatus;

/**
 * Response from an agent to a request
 * <p>
 * This class is part of the public API for remote agent web services.
 */
public class AgentResponse {
    private final Response.Type type;
    private final String text;
    private final String payload;
    private final ResponseStatus status;
    private Session session;
    private SessionStatus sessionStatus;

    /**
     * Create an agent response
     *
     * @param text  the text of the response
     */
    public AgentResponse(String text) {
        this.type = Response.Type.TEXT;
        this.text = text;
        this.payload = null;
        this.status = ResponseStatus.createSuccess();
    }

    /**
     * Create an agent response with payload
     *
     * @param type  the response type
     * @param text  the text of the response
     * @param payload  the payload of the response
     */
    public AgentResponse(Response.Type type, String text, String payload) {
        this.type = type;
        this.text = text;
        this.payload = payload;
        this.status = ResponseStatus.createSuccess();
    }

    /**
     * Create an agent response for an error
     *
     * @param code  the status code
     */
    public AgentResponse(ResponseStatus.Code code) {
        this.type = Response.Type.ERROR;
        this.text = null;
        this.payload = null;
        this.status = new ResponseStatus(code);
    }

    /**
     * Create an agent response for an error with custom message
     *
     * @param status  the status of the response
     */
    public AgentResponse(ResponseStatus status) {
        this.type = Response.Type.ERROR;
        this.text = null;
        this.payload = null;
        this.status = status;
    }

    /**
     * Get the response type
     *
     * @return the response type
     */
    public Response.Type getType() {
        return type;
    }

    /**
     * Get the text of the response
     *
     * @return text of the response
     */
    public String getText() {
        return text;
    }

    /**
     * Get the payload of the response
     * <p>
     * The payload data varies according to the response type
     *
     * @return payload string or null if no payload
     */
    public String getPayload() {
        return payload;
    }

    /**
     * Get the status
     *
     * @return the status code
     */
    public ResponseStatus getStatus() {
        return status;
    }

    /**
     * Is this a successful response?
     *
     * @return true if success
     */
    public boolean isSuccess() {
        return status.isSuccess();
    }

    /**
     * Is this a valid response?
     *
     * @return true if valid
     */
    public boolean isValid() {
        if (type == null || status == null) { 
            return false;
        }
        if (Response.Type.usesPayload(type) && payload == null) {
            return false;
        }
        return !(isSuccess() && text == null);
    }
    
    /**
     * Get the session
     *
     * @return the session of the request
     */
    public Session getSession() {
        return session;
    }

    /**
     * Set the session
     *
     * @param session  the session
     */
    public void setSession(Session session) {
        this.session = session;
    }

    /**
     * Get the session status
     *
     * @return sessionStatus  the session status
     */
    public SessionStatus getSessionStatus() {
        return this.sessionStatus;
    }

    /**
     * Set the session status
     *
     * @param sessionStatus  the session status
     */
    public void setSessionStatus(SessionStatus sessionStatus) {
        this.sessionStatus = sessionStatus;
    }
}
