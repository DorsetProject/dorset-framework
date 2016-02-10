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
package edu.jhuapl.dorset.agent;

/**
 * Response from an agent to a request
 */
public class AgentResponse {
    private String text;

    public AgentResponse() {
    }

    /**
     * Create an agent response
     * @param text the text of the response
     */
    public AgentResponse(String text) {
        this.text = text;
    }

    /**
     * Set the text of the response
     * @param text the text of the response
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Get the text of the response
     * @return text of the response
     */
    public String getText() {
        return text;
    }
}
