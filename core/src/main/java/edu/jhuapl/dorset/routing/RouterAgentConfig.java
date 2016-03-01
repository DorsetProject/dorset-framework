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

import java.util.ArrayList;
import java.util.Iterator;

import edu.jhuapl.dorset.agent.Agent;
import edu.jhuapl.dorset.config.MultiValuedMap;

/**
 * Configuration for a set of routers
 */
public class RouterAgentConfig implements Iterable<RouterAgentConfigEntry> {
    private ArrayList<RouterAgentConfigEntry> list = new ArrayList<RouterAgentConfigEntry>();

    /**
     * Add an agent to the configuration
     *
     * @param agent  the agent to add
     * @param params  the parameters for the router for that agent
     * @return this
     */
    public RouterAgentConfig add(Agent agent, MultiValuedMap params) {
        list.add(new RouterAgentConfigEntry(agent, params));
        return this;
    }

    @Override
    public Iterator<RouterAgentConfigEntry> iterator() {
        return list.iterator();
    }

    /**
     * Factory for this config class
     *
     * @return a new instance of RouterAgentConfig
     */
    public static RouterAgentConfig create() {
        return new RouterAgentConfig();
    }
}
