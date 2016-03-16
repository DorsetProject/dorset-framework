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

public class WhiteSpaceTokenizerTest {

    @Test
    public void testTokenize() {
        String testString = "Today is the first day of the rest of your life.";
        Tokenizer tokenizer = new WhiteSpaceTokenizer();
        
        String[] tokens = tokenizer.tokenize(testString);
        
        assertEquals("Today", tokens[0]);
        assertEquals("rest", tokens[7]);
        assertEquals("life.", tokens[10]);
    }

}
