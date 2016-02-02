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

import java.util.Properties;

/**
 * Agent registry entry
 */
public class RegistryEntry {
    private Agent agent;
    private Properties details;
    private Boolean isActive;

    /**
     * Constructor an entry for the agent registry
     * @param agent The agent
     * @param details Details about the agent that could be used for routing
     */
    public RegistryEntry(Agent agent, Properties details) {
        this.agent = agent;
        this.details = details;
        this.isActive = true;
    }

    /**
     * @return the agent
     */
    public Agent getAgent() {
        return agent;
    }

    /**
     * @return the details
     */
    public Properties getDetails() {
        return details;
    }
}
