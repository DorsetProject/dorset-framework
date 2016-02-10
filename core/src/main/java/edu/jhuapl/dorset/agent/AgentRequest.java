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
 * Request sent to an agent
 */
public class AgentRequest {
    private String text;

    public AgentRequest() {
    }

    /**
     * Create an agent request
     * @param text the text of the request
     */
    public AgentRequest(String text) {
        this.text = text;
    }

    /**
     * Set the text of the request
     * @param text the text of the request
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Get the text of the request
     * @return the text of the request
     */
    public String getText() {
        return text;
    }
}
