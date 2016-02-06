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
import edu.jhuapl.dorset.nlp.BasicTokenizer;
import edu.jhuapl.dorset.nlp.Tokenizer;

/**
 * This router looks at the first word of the request and gets the 
 * corresponding agent based on the agent name.
 * 
 * Requests must be of the form: [trigger] [request text]
 */
public class TriggerWordRouter implements Router {
    private AgentRegistry registry;
    private Tokenizer tokenizer;

    public TriggerWordRouter() {
        tokenizer = new BasicTokenizer();
    }

    @Override
    public void initialize(AgentRegistry registry) {
        this.registry = registry;
    }

    @Override
    public Agent[] getAgents(Request request) {
        String requestText = request.getText().toLowerCase();
        String[] tokens = tokenizer.tokenize(requestText);
        if (tokens.length == 0) {
            return new Agent[0];
        }

        Agent agent = registry.getAgent(tokens[0]);
        if (agent != null) {
            return new Agent[]{agent};
        } else {
            return new Agent[0];
        }
    }

}
