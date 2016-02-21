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
package edu.jhuapl.dorset.config;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class MultiValuedMap {
    private HashMap<String, HashSet<String>> map = new HashMap<String, HashSet<String>>();

    public void putString(String key, String value) {
        map.put(key, new HashSet<String>(Arrays.asList(value)));
    }

    public void putStrings(String key, String[] values) {
        if (values.length > 0) {
            map.put(key, new HashSet<String>(Arrays.asList(values)));
        }
    }

    public void addString(String key, String value) {
        if (!containsKey(key)) {
            map.put(key, new HashSet<String>());
        }
        map.get(key).add(value);
    }

    public void addStrings(String key, String[] values) {
        if (!containsKey(key)) {
            map.put(key, new HashSet<String>());
        }
        map.get(key).addAll(Arrays.asList(values));
    }

    public String getString(String key) {
        Set<String> values = map.get(key);
        if (values == null) {
            return null;
        }
        return values.iterator().next();
    }

    public String[] getStrings(String key) {
        Set<String> values = map.get(key);
        if (values == null) {
            return null;
        }
        return values.toArray(new String[values.size()]);
    }

    public boolean containsKey(String key) {
        return map.containsKey(key);
    }

    public int size() {
        return map.size();
    }
}
