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
import java.util.Set;

import edu.jhuapl.dorset.Request;
import edu.jhuapl.dorset.agent.Agent;
import edu.jhuapl.dorset.nlp.RuleBasedTokenizer;
import edu.jhuapl.dorset.nlp.Tokenizer;

/**
 * This router uses keywords to direct requests.
 * <p>
 * Each agent can have one or more keywords associated with it. Each request is
 * tokenized into words and any agent that has one of the words as a keyword
 * is returned.
 * <p>
 * TODO how to handle duplicate keywords across agents
 */
public class KeywordRouter implements Router {
    public static final String KEYWORDS = "keywords";

    private HashMap<String, Agent> agentMap;
    private HashSet<Agent> agents;
    private Tokenizer tokenizer;

    /**
     * Create the router
     *
     * @param agentsConfig  agents and routing configuration for those agents
     */
    public KeywordRouter(RouterAgentConfig agentsConfig) {
        agentMap = new HashMap<String, Agent>();
        agents = new HashSet<Agent>();
        tokenizer = new RuleBasedTokenizer();
        for (RouterAgentConfigEntry entry : agentsConfig) {
            String[] keywords = entry.getParams().getStrings(KEYWORDS);
            if (keywords == null) {
                continue;
            }
            agents.add(entry.getAgent());
            for (String keyword : keywords) {
                agentMap.put(keyword.toLowerCase(), entry.getAgent());
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

        Set<Agent> agents = new HashSet<Agent>();
        for (String token : tokens) {
            if (agentMap.containsKey(token)) {
                agents.add(agentMap.get(token));
            }
        }
        return agents.toArray(new Agent[agents.size()]);
    }

    @Override
    public Agent[] getAgents() {
        return agents.toArray(new Agent[agents.size()]);
    }

}
