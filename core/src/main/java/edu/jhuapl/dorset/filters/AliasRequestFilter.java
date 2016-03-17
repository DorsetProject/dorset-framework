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

import java.util.Map;

import edu.jhuapl.dorset.Request;

public class AliasRequestFilter implements RequestFilter {
    private Map<String, String> aliasMap;

    /**
     * Alias Request Filter
     *     
     * Each map key will act as an alias and replace its 
     * corresponding value in the Response text.
     * 
     */
    public AliasRequestFilter(Map<String, String> aliasMap) {
        this.aliasMap = aliasMap;
    }

    public Request filter(Request request) {
        if (this.aliasMap != null) {
            String filteredResponseText = request.getText();
            for (Map.Entry<String, String> entry : this.aliasMap.entrySet()) {
                filteredResponseText = filteredResponseText.replaceAll(entry.getValue(), entry.getKey()).trim();
            }
            request.setText(filteredResponseText);
        }
        return request;
    }

}
