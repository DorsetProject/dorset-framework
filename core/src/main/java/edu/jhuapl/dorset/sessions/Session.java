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

package edu.jhuapl.dorset.sessions;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import edu.jhuapl.dorset.agents.Agent;

public class Session {

    public final String id;
    public Date timestamp; // should have start date, end date, date last updated?
    public Agent primaryAgent;
    public SessionStatus sessionStatus;
    public List<SessionObject> sessionHistory;

    /**
     * Create a Session
     *
     */
    public Session() {
        this.id = UUID.randomUUID().toString();
        this.timestamp = new Date();
        this.sessionHistory = new ArrayList();
    }

    /**
     * Create a Session
     *
     * @param id  the session id
     *
     */
    public Session(String id) {
        this.id = id;
        this.timestamp = new Date();
        this.sessionHistory = new ArrayList();
    }
    
    public String getId() {
        return this.id;
    }

    public Date getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Agent getPrimaryAgent() {
        return this.primaryAgent;
    }

    public void setPrimaryAgent(Agent primaryAgent) {
        this.primaryAgent = primaryAgent;
    }

    public SessionStatus getStatus() {
        return this.sessionStatus;
    }

    public void setStatus(SessionStatus status) {
        this.sessionStatus = status;
    }

    public List<SessionObject> getSessionHistory() {
        return this.sessionHistory;
    }

    public void setSessionHistory(List<SessionObject> sessionHistory) {
        this.sessionHistory = sessionHistory;
    }

    public String sessionToString() {
        return "ID: " + this.id + "\nTimestamp: " + this.timestamp.toString() + "\nPrimaryAgent: "
                        + this.primaryAgent + "\nStatus: " + this.sessionStatus;
    }

    public enum SessionStatus {

        OPEN, CLOSED, TIMED_OUT, ERROR;

    }

}

