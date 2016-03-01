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

import java.util.Arrays;
import java.util.HashSet;

import edu.jhuapl.dorset.Request;
import edu.jhuapl.dorset.agent.Agent;

/**
 * Chain routers together
 * <p>
 * The routing precedes in the order that the routers are passed to the
 * constructor and stops with the first router to return agents.
 */
public class ChainRouter implements Router {
    private Router[] routers;

    /**
     * Create a chain router
     *
     * @param routers  the routers to use
     */
    public ChainRouter(Router... routers) {
        this.routers = Arrays.copyOf(routers, routers.length);
    }

    @Override
    public Agent[] route(Request request) {
        Agent[] agents = {};
        for (Router router : routers) {
            agents = router.route(request);
            if (agents.length != 0) {
                break;
            }
        }
        return agents;
    }

    @Override
    public Agent[] getAgents() {
        HashSet<Agent> agents = new HashSet<Agent>();
        for (Router router : routers) {
            agents.addAll(Arrays.asList(router.getAgents()));
        }
        return agents.toArray(new Agent[agents.size()]);
    }

}
