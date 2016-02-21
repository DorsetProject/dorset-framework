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

/**
 * A map which can have one or more values for each key
 */
public class MultiValuedMap {
    private HashMap<String, HashSet<String>> map = new HashMap<String, HashSet<String>>();

    /**
     * Add a string in the map replacing any strings at that key
     * @param key the key
     * @param value the value
     */
    public void putString(String key, String value) {
        map.put(key, new HashSet<String>(Arrays.asList(value)));
    }

    /**
     * Add the array of strings to the map replacing any strings at that key
     * @param key the key
     * @param values array of values
     */
    public void putStrings(String key, String[] values) {
        if (values.length > 0) {
            map.put(key, new HashSet<String>(Arrays.asList(values)));
        }
    }

    /**
     * Add the string to those at that key
     * @param key the key
     * @param value the value
     */
    public void addString(String key, String value) {
        if (!containsKey(key)) {
            map.put(key, new HashSet<String>());
        }
        map.get(key).add(value);
    }

    /**
     * Add strings to those at that key
     * @param key the key
     * @param values array of values
     */
    public void addStrings(String key, String[] values) {
        if (!containsKey(key)) {
            map.put(key, new HashSet<String>());
        }
        map.get(key).addAll(Arrays.asList(values));
    }

    /**
     * Get the string at the specified key
     * @param key the key
     * @return the first value at the key or null if none
     */
    public String getString(String key) {
        Set<String> values = map.get(key);
        if (values == null) {
            return null;
        }
        return values.iterator().next();
    }

    /**
     * Get the strings at the specified key
     * @param key the key
     * @return array of values or null if none
     */
    public String[] getStrings(String key) {
        Set<String> values = map.get(key);
        if (values == null) {
            return null;
        }
        return values.toArray(new String[values.size()]);
    }

    /**
     * Does the map contain this key?
     * @param key the key
     * @return true if it does
     */
    public boolean containsKey(String key) {
        return map.containsKey(key);
    }

    /**
     * How many keys does the map contain?
     * @return the number of keys
     */
    public int size() {
        return map.size();
    }
}
