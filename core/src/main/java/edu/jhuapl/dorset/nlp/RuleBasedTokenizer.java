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

import java.util.ArrayList;

/**
 * Tokenizer that uses some basic rules to handle punctuation
 * <p>
 * Most punctuation becomes its own token.
 * Contractions are kept together.
 * Possessives are kept together except for plural possessives.
 * Does not handle email addresses, URLs, or acronyms.
 */
public class RuleBasedTokenizer implements Tokenizer {
    private boolean keepPunctuation;

    /**
     * Create a rule based tokenizer
     * <p>
     * By default, return punctuation only tokens 
     */
    public RuleBasedTokenizer() {
        this(true);
    }

    /**
     * Create a rule based tokenizer
     *
     * @param keepPunctuation  should this return punctuation only tokens
     */
    public RuleBasedTokenizer(boolean keepPunctuation) {
        this.keepPunctuation = keepPunctuation;
    }

    @Override
    public String[] tokenize(String text) {
        char[] chars = text.trim().toCharArray();
        ArrayList<String> tokens = new ArrayList<String>();
        StringBuilder sb = new StringBuilder();
        for (int index = 0; index < chars.length; index++) {
            char previousChar = (index - 1) > 0 ? chars[index - 1] : ' ';
            char currentChar = chars[index];
            char nextChar = (index + 1) < chars.length ? chars[index + 1] : ' ';

            // keep contractions together (but not plural possessives)
            if (currentChar == '\'') {
                if (Character.isLetter(previousChar) && Character.isLetter(nextChar)) {
                    sb.append(currentChar);
                    continue;
                }
            }

            // keep numbers together
            if (currentChar == '.' || currentChar == ',') {
                if (Character.isDigit(previousChar) && Character.isDigit(nextChar)) {
                    sb.append(currentChar);
                    continue;
                }
            }

            // whitespace break
            if (currentChar == ' ') {
                if (sb.length() > 0) {
                    tokens.add(sb.toString());
                    sb.setLength(0);
                }
                continue;
            }

            // punctuation break
            if (!Character.isLetterOrDigit(currentChar)) {
                tokens.add(sb.toString());
                sb.setLength(0);
                if (keepPunctuation) {
                    tokens.add(String.valueOf(currentChar));
                }
                continue;                
            }

            sb.append(currentChar);
        }

        if (sb.length() > 0) {
            tokens.add(sb.toString());
        }

        return tokens.toArray(new String[tokens.size()]);
    }

}
