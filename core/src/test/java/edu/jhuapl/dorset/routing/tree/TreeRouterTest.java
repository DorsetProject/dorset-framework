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

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import edu.jhuapl.dorset.Request;
import edu.jhuapl.dorset.agents.Agent;
import edu.jhuapl.dorset.routing.Router;

public class TreeRouterTest {

    protected Router createBinaryTreeRouter() {
        // binary tree defined as so with keywords:
        //                           root
        //                weather              score
        //      historical     current   baseball    football
        Agent a1 = mock(Agent.class);
        when(a1.getName()).thenReturn("historical");
        Agent a2 = mock(Agent.class);
        when(a2.getName()).thenReturn("current");
        Node weatherNode = new BinaryKeywordNode("historical", new LeafNode(a1), new LeafNode(a2));
        Agent a3 = mock(Agent.class);
        when(a3.getName()).thenReturn("baseball");
        Agent a4 = mock(Agent.class);
        when(a4.getName()).thenReturn("football");
        Node scoreNode = new BinaryKeywordNode("baseball", new LeafNode(a3), new LeafNode(a4));
        Node root = new BinaryKeywordNode("weather", weatherNode, scoreNode);
        return new TreeRouter(root);        
    }

    @Test
    public void testRoutingWithBinaryTree() {
        Router router = createBinaryTreeRouter();
        Request r1 = new Request("What is the historical weather for June");
        Request r2 = new Request("What is the current weather for Seattle");
        Request r3 = new Request("What was score for last night's baseball game");
        Request r4 = new Request("What was the score for last night's football game");

        assertEquals("historical", router.route(r1)[0].getName());
        assertEquals("current", router.route(r2)[0].getName());
        assertEquals("baseball", router.route(r3)[0].getName());
        assertEquals("football", router.route(r4)[0].getName());
    }

    @Test
    public void testGettingAgentsFromRouter() {
        Router router = createBinaryTreeRouter();

        Agent[] agents = router.getAgents();

        assertEquals(4, agents.length);
        List<String> names = new ArrayList<String>();
        for (Agent agent : agents) {
            names.add(agent.getName());
        }
        assertTrue(names.contains("historical"));
        assertTrue(names.contains("current"));
        assertTrue(names.contains("baseball"));
        assertTrue(names.contains("football"));
    }

    @Test
    public void testNoText() {
        Router router = createBinaryTreeRouter();
        assertEquals(0, router.route(new Request("")).length);
    }
}
