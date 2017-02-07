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

import org.junit.Test;

import edu.jhuapl.dorset.Request;
import edu.jhuapl.dorset.agents.Agent;

public class BinaryKeywordNodeTest {

    @Test
    public void testBothSides() {
        Request request1 = new Request("What is the weather like?");
        Request request2 = new Request("Where is the door?");
        Agent a1 = mock(Agent.class);
        Agent a2 = mock(Agent.class);
        Node node = new BinaryKeywordNode("weather", new LeafNode(a1), new LeafNode(a2));

        assertEquals(a1, node.selectChild(request1).getValue()[0]);
        assertEquals(a2, node.selectChild(request2).getValue()[0]);
    }

    @Test
    public void testNoText() {
        Agent a1 = mock(Agent.class);
        Agent a2 = mock(Agent.class);
        Node node = new BinaryKeywordNode("weather", new LeafNode(a1), new LeafNode(a2));

        assertNull(node.selectChild(new Request("")));
    }
}
