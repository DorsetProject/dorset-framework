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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import edu.jhuapl.dorset.Request;
import edu.jhuapl.dorset.agents.Agent;
import edu.jhuapl.dorset.nlp.RuleBasedTokenizer;
import edu.jhuapl.dorset.nlp.Tokenizer;

/**
 * Node for a k-ary tree using keywords for child selection.
 * <p>
 * This returns the first child node that matches a keyword from left to right.
 * If no keywords match, it returns null.
 */
public class GenericKeywordNode implements Node {
    private Map<String, Node> children;
    private Tokenizer tokenizer;

    /**
     * Create a keyword node with support for arbitrary number of children
     */
    public GenericKeywordNode() {
        children = new HashMap<String, Node>();
        tokenizer = new RuleBasedTokenizer();
    }

    /**
     * Add a child node
     *
     * @param keyword  keyword for the child node
     * @param node  node to return if the keyword is matched
     */
    public void addChild(String keyword, Node node) {
        children.put(keyword.toLowerCase(), node);
    }

    /**
     * Add a child node
     *
     * @param keywords  keywords for the child node
     * @param node  node to return if a keyword is matched
     */
    public void addChild(String[] keywords, Node node) {
        for (String keyword : keywords) {
            children.put(keyword.toLowerCase(), node);
        }
    }

    @Override
    public Node selectChild(Request request) {
        String requestText = request.getText().toLowerCase();
        String[] tokens = tokenizer.tokenize(requestText);

        for (String token : tokens) {
            if (children.containsKey(token)) {
                return children.get(token);
            }
        }
        return null;
    }

    @Override
    public Node[] getChildren() {
        Collection<Node> values = children.values();
        return values.toArray(new Node[values.size()]);
    }

    @Override
    public boolean isLeaf() {
        return false;
    }

    @Override
    public Agent[] getValue() {
        return null;
    }

}
