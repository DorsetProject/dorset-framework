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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import edu.jhuapl.dorset.Request;
import edu.jhuapl.dorset.agents.Agent;

/**
 * This router uses regular expressions to direct requests.
 * <p>
 * Each agent can have one or more regular expressions associated with it.
 * The regular expressions are treated as case insensitive.
 */
public class RegexRouter implements Router {
    public static final String REGEX = "regex";

    private HashMap<Pattern, Agent> agentMap;
    private HashSet<Agent> agents;

    /**
     * Create the router
     *
     * @param agentsConfig  agents and routing configuration for those agents
     */
    public RegexRouter(RouterAgentConfig agentsConfig) {
        agentMap = new HashMap<Pattern, Agent>();
        agents = new HashSet<Agent>();
        for (RouterAgentConfigEntry entry : agentsConfig) {
            String[] regexes = entry.getParams().getStrings(REGEX);
            if (regexes == null) {
                continue;
            }
            agents.add(entry.getAgent());
            for (String regex : regexes) {
                agentMap.put(Pattern.compile(regex, Pattern.CASE_INSENSITIVE), entry.getAgent());
            }
        }
    }

    @Override
    public Agent[] route(Request request) {
        String text = request.getText();
        Set<Agent> agents = new HashSet<Agent>();
        for (Map.Entry<Pattern, Agent> entry : agentMap.entrySet()) {
            if (entry.getKey().matcher(text).matches()) {
                agents.add(entry.getValue());
            }
        }
        return agents.toArray(new Agent[agents.size()]);
    }

    @Override
    public Agent[] getAgents() {
        return agents.toArray(new Agent[agents.size()]);
    }

}
