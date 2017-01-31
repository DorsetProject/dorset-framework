/*
 * Copyright 2017 The Johns Hopkins University Applied Physics Laboratory LLC
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
package edu.jhuapl.dorset.routing.tree;

import edu.jhuapl.dorset.Request;
import edu.jhuapl.dorset.agents.Agent;

/**
 * Terminal node in a tree. It returns an agent.
 */
public class LeafNode implements Node {
    private Agent agent;

    /**
     * Create a leaf node
     *
     * @param agent  Agent returned from routing a request
     */
    public LeafNode(Agent agent) {
        this.agent = agent;
    }

    @Override
    public Node selectChild(Request request) {
        return null;
    }

    @Override
    public Node[] getChildren() {
        return null;
    }

    @Override
    public boolean isLeaf() {
        return true;
    }

    @Override
    public Agent[] getValue() {
        return new Agent[]{agent};
    }

}
