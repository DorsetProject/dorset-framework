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

import edu.jhuapl.dorset.agent.Agent;
import edu.jhuapl.dorset.config.MultiValuedMap;

public class RouterAgentConfigEntry {
    private Agent agent;
    private MultiValuedMap params;

    /**
     * Construct an entry
     *
     * @param agent  the agent
     * @param params  the parameters for that agent
     */
    public RouterAgentConfigEntry(Agent agent, MultiValuedMap params) {
        this.agent = agent;
        this.params = params;
    }

    /**
     * Get the agent
     *
     * @return the agent
     */
    public Agent getAgent() {
        return agent;
    }

    /**
     * Get the parameters for the agent
     *
     * @return the parameters
     */
    public MultiValuedMap getParams() {
        return params;
    }
}
