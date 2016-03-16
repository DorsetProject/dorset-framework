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

import edu.jhuapl.dorset.Request;
import edu.jhuapl.dorset.agent.Agent;
import edu.jhuapl.dorset.nlp.RuleBasedTokenizer;
import edu.jhuapl.dorset.nlp.Tokenizer;

/**
 * This router looks at the first word of the request and gets the 
 * corresponding agent based on the agent name.
 * <p>
 * Requests must be of the form: [trigger] [request text]
 */
public class TriggerWordRouter implements Router {
    public static final String TRIGGERS = "triggers";

    private HashMap<String, Agent> agentMap;
    private HashSet<Agent> agents;
    private Tokenizer tokenizer;

    /**
     * Create the router
     *
     * @param agentsConfig  agents and routing configuration for those agents
     */
    public TriggerWordRouter(RouterAgentConfig agentsConfig) {
        agentMap = new HashMap<String, Agent>();
        agents = new HashSet<Agent>();
        tokenizer = new RuleBasedTokenizer();
        for (RouterAgentConfigEntry entry : agentsConfig) {
            String[] triggers = entry.getParams().getStrings(TRIGGERS);
            if (triggers == null) {
                continue;
            }
            agents.add(entry.getAgent());
            for (String trigger : triggers) {
                agentMap.put(trigger.toLowerCase(), entry.getAgent());
            }
        }
    }

    @Override
    public Agent[] route(Request request) {
        String requestText = request.getText().toLowerCase();
        String[] tokens = tokenizer.tokenize(requestText);
        if (tokens.length == 0) {
            return new Agent[0];
        }

        Agent agent = agentMap.get(tokens[0]);
        if (agent != null) {
            return new Agent[]{agent};
        } else {
            return new Agent[0];
        }
    }

    @Override
    public Agent[] getAgents() {
        return agents.toArray(new Agent[agents.size()]);
    }

}
