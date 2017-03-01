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
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

import edu.jhuapl.dorset.Request;
import edu.jhuapl.dorset.agents.Agent;

public class GenericKeywordNodeTest {
    private GenericKeywordNode node;
    private Agent red = mock(Agent.class);
    private Agent green = mock(Agent.class);
    private Agent blue = mock(Agent.class);
    
    @Before
    public void setupNodes() {
        node = new GenericKeywordNode();
        node.addChild("red", new LeafNode(red));
        node.addChild("green", new LeafNode(green));
        node.addChild("blue", new LeafNode(blue));
    }

    @Test
    public void testSingleKeywordPerNode() {
        Request request1 = new Request("Red roses");
        Request request2 = new Request("The moon is blue");

        assertEquals(red, node.selectChild(request1).getValue()[0]);
        assertEquals(blue, node.selectChild(request2).getValue()[0]);
    }

    @Test
    public void testNoMatch() {
        Request request = new Request("Where is the door?");
        assertNull(node.selectChild(request));
    }

    @Test
    public void testNoText() {
        Request request = new Request("");
        assertNull(node.selectChild(request));
    }

    @Test
    public void testLeftToRightMatch() {
        Request request = new Request("Green is better than red");
        assertEquals(green, node.selectChild(request).getValue()[0]);
    }

    @Test
    public void testMultipleKeywords() {
        GenericKeywordNode node2 = new GenericKeywordNode();
        String[] redKeywords = {"red", "pink", "ruby", "rose"};
        node2.addChild(redKeywords, new LeafNode(red));
        String[] blueKeywords = {"blue", "azure", "cyan"};
        node2.addChild(blueKeywords, new LeafNode(blue));

        assertEquals(red, node2.selectChild(new Request("this is a ruby")).getValue()[0]);
    }

}
