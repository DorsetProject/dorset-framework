/**
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
package edu.jhuapl.dorset.agent;

import java.util.Arrays;

/**
 * Agent description for public consumption
 */
public class Description {
    private String name;
    private String summary;
    private String[] examples;

    public Description() {
        
    }

    /**
     * Constructor
     * 
     * @param summary A user-facing description of the agent's capabilities
     * @param example An example question or command
     */
    public Description(String name, String summary, String example) {
        this.setName(name);
        this.setSummary(summary);
        this.setExample(example);
    }

    /**
     * Constructor
     * 
     * @param summary A user-facing description of the agent's capabilities
     * @param example An example question or command
     */
    public Description(String name, String summary, String[] examples) {
        this.setName(name);
        this.setSummary(summary);
        this.setExamples(examples);
    }

    /**
     * @return the summary
     */
    public String getSummary() {
        return summary;
    }

    /**
     * @param summary A description of the agent's capabilities
     */
    public void setSummary(String summary) {
        this.summary = summary;
    }

    /**
     * @return examples of using the agent
     */
    public String[] getExamples() {
        return examples;
    }

    /**
     * @param examples Array of example requests that the agent can handle
     */
    public void setExamples(String[] examples) {
        this.examples = Arrays.copyOf(examples, examples.length);
    }

    /**
     * @param example A single example request that the agent can handle
     */
    public void setExample(String example) {
        this.examples = new String[1];
        this.examples[0] = example;
    }

    /**
     * @return name of the agent
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name of the agent
     */
    public void setName(String name) {
        this.name = name.toLowerCase();
    }

    /**
     * Get a default description
     * @param name The name of the agent
     * @return
     */
    public static Description getUninitializedDescription(String name) {
        return new Description(name, "Summary is not set.", "Example is not set.");
    }
}
