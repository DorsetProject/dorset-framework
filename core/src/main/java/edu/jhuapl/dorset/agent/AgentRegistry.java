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
package edu.jhuapl.dorset.agent;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Agent registry
 * 
 * The registry is case insensitive. All names are stored as lowercase.
 */
public class AgentRegistry {
    private final Logger logger = LoggerFactory.getLogger(AgentRegistry.class);

    private Map<String, RegistryEntry> registry = new HashMap<String, RegistryEntry>();

    /**
     * Register an agent
     * 
     * If an agent already exists with that name, it is replaced.
     * 
     * @param agent The agent object
     * @param details Any additional information about the agent. This could be
     *                information that the router needs to know.
     */
    public void register(Agent agent, Properties details) {
        String name = agent.getName().toLowerCase();
        if (registry.containsKey(name)) {
            logger.warn("Replacing agent registry entry with name: " + name);
        }
        registry.put(name, new RegistryEntry(agent, details));
    }

    /**
     * Get the registry as a map
     * 
     * This returns a shallow copy of the registry.
     * 
     * @return map of name : registry entry
     * @see RegistryEntry
     */
    public Map<String, RegistryEntry> asMap() {
        return new HashMap<String, RegistryEntry>(registry);
    }

    /**
     * Get the entry for a particular agent
     * 
     * @param name Name of the agent
     * @return agent registry entry or null if it does not exist
     */
    public RegistryEntry getAgentEntry(String name) {
        name = name.toLowerCase();
        return registry.get(name);
    }

    /**
     * Get an agent
     * 
     * @param name Name of the agent
     * @return agent or null if it does not exist
     */
    public Agent getAgent(String name) {
        RegistryEntry entry = getAgentEntry(name);
        if (entry != null) {
            return entry.getAgent();
        } else {
            return null;
        }
    }

}
