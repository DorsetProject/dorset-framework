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
package edu.jhuapl.dorset;

import edu.jhuapl.dorset.agent.AgentMessages;

/**
 * Dorset Response
 * <p>
 * Represents the response to a request to the application.
 * <p>
 * If the statusCode is not AgentMessages.SUCCESS, the text field can be left blank.
 */
public class Response {
    private String text;
    private int statusCode;

    /**
     * Create a response
     *
     * @param text  the text of the response
     */
    public Response(String text) {
        this.text = text;
        this.statusCode = AgentMessages.SUCCESS;
    }

    /**
     * Create a response
     *
     * @param text  the text of the response
     * @param code  the status code
     */
    public Response(String text, int code) {
        this.text = text;
        this.statusCode = code;
    }

    /**
     * Create a response
     *
     * @param code  the status code
     */
    public Response(int code) {
        this.statusCode = code;
    }

    /**
     * Get the text of the response
     * <p>
     * Can return a null if an error occurred.
     *
     * @return the text of the response
     */
    public String getText() {
        return text;
    }

    /**
     * Set the text of the response
     *
     * @param text  the text of the response
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Get the status code
     *
     * @return the status code
     * @see AgentMessages
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * Set the status code
     *
     * @param code  the status code
     * @see AgentMessages
     */
    public void setStatusCode(int code) {
        this.statusCode = code;
    }
}
