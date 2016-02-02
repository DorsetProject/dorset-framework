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
import java.util.List;

import edu.jhuapl.dorset.Request;
import edu.jhuapl.dorset.agent.Agent;
import edu.jhuapl.dorset.agent.AgentRegistry;

/**
 * This looks at the first word of the request and gets the corresponding agent 
 * based on the agent name. Agent names must be lower case. 
 */
public class KeywordRouter implements Router {
    private AgentRegistry registry;

    public KeywordRouter() {
   
    }

    @Override
    public void initialize(AgentRegistry registry) {
        this.registry = registry;
    }

    @Override
    public Agent[] getAgents(Request request) {
        String quest = request.getText().toLowerCase();
        String[] wordArray = quest.split(" ", 2);

        List<Agent> agents = new ArrayList<Agent>();
        Agent outAgent = registry.getAgent(wordArray[0]);
        if (outAgent != null) {
            agents.add(outAgent);
        }   
        
        return agents.toArray(new Agent[agents.size()]);
    }

}
