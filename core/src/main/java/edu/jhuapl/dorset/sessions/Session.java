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

import java.util.Date;

import com.google.gson.JsonArray;

public class Session {

    public String id;
    public Date timestamp; // should have start date, end date, date last updated?
    public String primaryAgent;
    public SessionStatus sessionStatus;
    public SessionObject[] sessionHistory;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getPrimaryAgent() {
        return this.primaryAgent;
    }

    public void setPrimaryAgent(String primaryAgent) {
        this.primaryAgent = primaryAgent;
    }

    public SessionStatus getStatus() {
        return this.sessionStatus;
    }

    public void setStatus(SessionStatus status) {
        this.sessionStatus = status;
    }

    public SessionObject[] getSessionHistory() {
        return this.sessionHistory;
    }

    public void setSessionHistory(SessionObject[] sessionHistory) {
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

