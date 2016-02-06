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
package edu.jhuapl.dorset.routing;

import edu.jhuapl.dorset.Request;
import edu.jhuapl.dorset.agent.Agent;
import edu.jhuapl.dorset.agent.AgentRegistry;

/**
 * Routes all requests to a single agent
 */
public class SingleAgentRouter implements Router {
    private String agentName;
    private Agent agent;

    /**
     * Create the router
     * @param name the name of the agent to route requests to
     */
    public SingleAgentRouter(String name) {
        agentName = name;
    }

    @Override
    public void initialize(AgentRegistry registry) {
        agent = registry.getAgent(agentName);
    }

    @Override
    public Agent[] getAgents(Request request) {
        if (agent != null) {
            return new Agent[]{agent};
        } else {
            return new Agent[0];
        }
    }

}
