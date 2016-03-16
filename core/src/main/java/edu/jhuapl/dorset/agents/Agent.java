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

/**
 * An agent answers a question or performs an action.
 * <p>
 * An agent must have its default name and description set in its constructor.
 * Agents should usually extend {@link AbstractAgent}.
 */
public interface Agent {

    /**
     * Get the name of the agent
     *
     * @return name
     */
    public String getName();

    /**
     * Override the default name of the agent
     *
     * @param name  New name for the agent
     */
    public void setName(String name);

    /**
     * Get the description of the agent for human consumption
     *
     * @return Description of the agent
     */
    public Description getDescription();

    /**
     * Override the default description
     *
     * @param description  Description of the agent
     * @see Description
     */
    public void setDescription(Description description);
    
    /**
     * Process a request
     *
     * @param request  Request object
     * @return response to the request
     */
    public AgentResponse process(AgentRequest request);
}
