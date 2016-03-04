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

import edu.jhuapl.dorset.ResponseStatus;

/**
 * Response from an agent to a request
 * <p>
 * This class is part of the public API for remote agent web services.
 */
public class AgentResponse {
    private String text;
    private ResponseStatus status;

    public AgentResponse() {}

    /**
     * Create an agent response
     *
     * @param text  the text of the response
     */
    public AgentResponse(String text) {
        this.text = text;
        this.status = ResponseStatus.createSuccess();
    }

    /**
     * Create an agent response for an error
     *
     * @param code  the status code
     */
    public AgentResponse(ResponseStatus.Code code) {
        this.status = new ResponseStatus(code);
    }

    /**
     * Create an agent response for an error with custom message
     *
     * @param status  the status of the response
     */
    public AgentResponse(ResponseStatus status) {
        this.status = status;
    }

    /**
     * Set the text of the response (for deserialization)
     *
     * @param text  the text of the response
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Get the text of the response
     *
     * @return text of the response
     */
    public String getText() {
        return text;
    }

    /**
     * Get the status
     *
     * @return the status code
     */
    public ResponseStatus getStatus() {
        return status;
    }

    /**
     * Set the status (for deserialization)
     *
     * @param status  the status
     */
    public void setStatus(ResponseStatus status) {
        this.status = status;
    }

    /**
     * Is this a successful response?
     *
     * @return true if success
     */
    public boolean isSuccess() {
        return status.isSuccess();
    }

    /**
     * Is this a valid response?
     *
     * @return true if valid
     */
    public boolean isValid() {
        if (status == null) {
            return false;
        }
        return !(isSuccess() && text == null);
    }
}
