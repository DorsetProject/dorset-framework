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
 * Node for a tree router.
 * <p>
 * Parent nodes return child nodes based on a request.
 * Leaf nodes return an array of agents to be returned by TreeRouter.
 */
public interface Node {

    /**
     * Get the child node that matches the request
     *
     * @param request  Request object
     * @return routing node
     */
    public Node selectChild(Request request);

    /**
     * Get children of this node 
     *
     * @return Node array
     */
    public Node[] getChildren();

    /**
     * Is this node a leaf?
     *
     * @return boolean
     */
    public boolean isLeaf();

    /**
     * Get the agents for this leaf node
     *
     * @return Agent array
     */
    public Agent[] getValue();
}
