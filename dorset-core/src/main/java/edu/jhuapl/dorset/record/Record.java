/**
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
package edu.jhuapl.dorset.record;

import java.util.Date;

import edu.jhuapl.dorset.Request;
import edu.jhuapl.dorset.Response;
import edu.jhuapl.dorset.agent.Agent;

/**
 * A record of handling a request
 */
public class Record {
    private Date timestamp;
    private long routeTime;
    private long agentTime;
    private String requestText;
    private String[] agentNames;
    private String selectedAgentName;
    private String responseText;

    public Record(Request request) {
        timestamp = new Date();
        requestText = request.getText();
    }
    
    public void setRouteTime(long start, long stop) {
        routeTime = stop - start;
    }

    public void setAgentTime(long start, long stop) {
        agentTime = stop - start;
    }

    public void setAgents(Agent[] agents) {
        agentNames = new String[agents.length];
        for (int i=0; i<agents.length; i++) {
            agentNames[i] = agents[i].getName();
        }
    }

    public void setSelectedAgent(Agent agent) {
        selectedAgentName = agent.getName();
    }

    public void setResponse(Response response) {
        responseText = response.getText();
    }

    public Date getTimestamp() {
        return timestamp;
    }
}
