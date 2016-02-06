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
 * Route requests to agents for request handling
 */
public interface Router {

    /**
     * Initialize the router with the agent registry
     * 
     * This is called automatically by the framework.
     * 
     * @param registry the agent registry
     */
    public void initialize(AgentRegistry registry);

    /**
     * Get an array of agents to send the request to
     * @param request the request object
     * @return array of agents (empty array if no agent found)
     */
    public Agent[] getAgents(Request request);
}
