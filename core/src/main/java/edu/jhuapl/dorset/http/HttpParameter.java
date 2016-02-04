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
package edu.jhuapl.dorset.http;

public class HttpParameter {
    private String name;
    private String value;

    /**
     * Create a name-value pair
     * @param name the name string
     * @param value the value string
     */
    public HttpParameter(String name, String value) {
        this.name = name;
        this.value = value;
    }
 
    /**
     * Get the name
     * @return name string
     */
    public String getName() {
        return name;
    }

    /**
     * Get the value
     * @return value string
     */
    public String getValue() {
        return value;
    }
}
