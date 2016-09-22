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
package edu.jhuapl.dorset.filters;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import edu.jhuapl.dorset.Request;

/**
 * Replace words or phrases in a request with another string
 *
 * For example, when the string "jhu" is in the request, we may want to expand it
 * to "Johns Hopkins University".
 */
public class AliasRequestFilter implements RequestFilter {
    private Map<String, String> aliasMap;
    private Map<Pattern, String> patternAliasMap;

    /**
     * Alias Request Filter
     * 
     * Each map key will be replaced by its corresponding value.
     * This value will act as an alias in the Request text.
     */
    public AliasRequestFilter(Map<String, String> aliasMap) {
        this.aliasMap = aliasMap;
        if (this.aliasMap != null) {
            this.patternAliasMap = new HashMap<Pattern, String>();
            for (Map.Entry<String, String> entry : this.aliasMap.entrySet()) {
                String regex = "\\b" + entry.getKey() + "\\b";
                this.patternAliasMap.put(Pattern.compile(regex, Pattern.CASE_INSENSITIVE), entry.getValue());
            }
        }
    }

    @Override
    public Request filter(Request request) {
        if (this.patternAliasMap != null) {
            String filteredResponseText = request.getText();
            for (Entry<Pattern, String> entry : this.patternAliasMap.entrySet()) {
                filteredResponseText = entry.getKey().matcher(filteredResponseText)
                        .replaceAll(entry.getValue());
            }
            request.setText(filteredResponseText);
        }
        return request;
    }

}
