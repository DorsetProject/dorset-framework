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
package edu.jhuapl.dorset.nlp;

import static org.junit.Assert.*;

import org.junit.Test;

public class RuleBasedTokenizerTest {

    @Test
    public void testTokenize() {
        String testString = "Today is the first day of the rest of your life.";
        Tokenizer tokenizer = new RuleBasedTokenizer();

        String[] tokens = tokenizer.tokenize(testString);

        assertEquals("Today", tokens[0]);
        assertEquals("rest", tokens[7]);
        assertEquals("life", tokens[10]);
        assertEquals(".", tokens[11]);
    }

    @Test
    public void testTokenizeWithContraction() {
        String testString = "I can't believe this";
        Tokenizer tokenizer = new RuleBasedTokenizer();

        String[] tokens = tokenizer.tokenize(testString);

        assertEquals("I", tokens[0]);
        assertEquals("can't", tokens[1]);
        assertEquals("believe", tokens[2]);
        assertEquals("this", tokens[3]);
    }

    @Test
    public void testTokenizeWithDecimalNumbers() {
        String testString = "Add 2.3 to 4,305.7";
        Tokenizer tokenizer = new RuleBasedTokenizer();

        String[] tokens = tokenizer.tokenize(testString);

        assertEquals("Add", tokens[0]);
        assertEquals("2.3", tokens[1]);
        assertEquals("to", tokens[2]);
        assertEquals("4,305.7", tokens[3]);
    }

}
