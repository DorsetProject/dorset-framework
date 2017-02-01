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

import java.util.Arrays;

import edu.jhuapl.dorset.Request;
import edu.jhuapl.dorset.agents.Agent;
import edu.jhuapl.dorset.nlp.RuleBasedTokenizer;
import edu.jhuapl.dorset.nlp.Tokenizer;

/**
 * Use one or more keywords to determine which child node is returned.
 */
public class KeywordNode implements Node {
    private String[] keywords;
    private Node matchedNode;
    private Node nonMatchedNode;
    private Tokenizer tokenizer;

    /**
     * Create a keyword matching node
     *
     * @param keyword  keyword string to match to
     * @param matchNode  Node to return if keyword matched
     * @param nonMatchNode  Node to return if keyword not matched
     */
    public KeywordNode(String keyword, Node matchNode, Node nonMatchNode) {
        this(new String[]{keyword}, matchNode, nonMatchNode);
    }

    /**
     * Create a keyword matching node
     *
     * @param keywords  array of keyword strings to match to
     * @param matchNode  Node to return if a keyword matched
     * @param nonMatchNode  Node to return if a keyword not matched
     */
    public KeywordNode(String[] keywords, Node matchNode, Node nonMatchNode) {
        this.keywords = new String[keywords.length];
        for (int i = 0; i < keywords.length; i++) {
            this.keywords[i] = keywords[i].toLowerCase();
        }
        this.matchedNode = matchNode;
        this.nonMatchedNode = nonMatchNode;
        this.tokenizer = new RuleBasedTokenizer();
    }

    @Override
    public Node selectChild(Request request) {
        String requestText = request.getText().toLowerCase();
        String[] tokens = tokenizer.tokenize(requestText);
        for (String keyword : keywords) {
            if (Arrays.asList(tokens).contains(keyword)) {
                return matchedNode;
            }
        }
        return nonMatchedNode;
    }

    @Override
    public Node[] getChildren() {
        return new Node[]{matchedNode, nonMatchedNode};
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
