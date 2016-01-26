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
package edu.jhuapl.dorset.agent;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 *
 */
public class AgentRegistry {
    private Map<String, AgentRegistryEntry> registry = new HashMap<String, AgentRegistryEntry>();

    public void register(String name, Agent agent, Properties details) {
        registry.put(name, new AgentRegistryEntry(agent, details));
    }

    public Map<String, AgentRegistryEntry> asMap() {
        return new HashMap<String, AgentRegistryEntry>(registry);
    }

    public AgentRegistryEntry getAgentEntry(String name) {
        return registry.get(name);
    }

    public Agent getAgent(String name) {
        return registry.get(name).agent;
    }

}
