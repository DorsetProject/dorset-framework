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

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;

import edu.jhuapl.dorset.Request;
import edu.jhuapl.dorset.agents.Agent;
import edu.jhuapl.dorset.routing.Router;

/**
 * Create a tree of routers
 * <p>
 * The tree must be constructed before passed into the constructor.
 * TreeRouter and Node supports k-ary trees though some implementations may be binary only.
 * If the router reaches a dead end (a node that is not a leaf and does not return a child),
 * it returns an empty array.
 */
public class TreeRouter implements Router {
    private Node root;

    /**
     * Create a tree router
     *
     * @param root  Root node of the constructed tree
     */
    public TreeRouter(Node root) {
        this.root = root;
    }

    @Override
    public Agent[] route(Request request) {
        Node node = root;
        while (node != null && !node.isLeaf()) {
            node = node.selectChild(request);
        }
        if (node != null) {
            return node.getValue();
        } else {
            return new Agent[0];
        }
    }

    @Override
    public Agent[] getAgents() {
        Set<Agent> agents = new HashSet<Agent>();
        Deque<Node> stack = new ArrayDeque<Node>();
        stack.addLast(root);
        while (!stack.isEmpty()) {
            Node node = stack.pollLast();
            if (node.isLeaf()) {
                agents.addAll(Arrays.asList(node.getValue()));
            } else {
                stack.addAll(Arrays.asList(node.getChildren()));
            }
        }
        return agents.toArray(new Agent[agents.size()]);
    }

}
