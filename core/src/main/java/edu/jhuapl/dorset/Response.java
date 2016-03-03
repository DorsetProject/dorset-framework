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

/**
 * Dorset Response
 * <p>
 * Represents the response to a request to the application.
 */
public class Response {
    private String text;
    private ResponseStatus status;

    /**
     * Create a response
     *
     * @param text  the text of the response
     */
    public Response(String text) {
        this.text = text;
        this.status = ResponseStatus.createSuccess();
    }

    /**
     * Create a response
     *
     * @param text  the text of the response
     * @param status  the response status
     */
    public Response(String text, ResponseStatus status) {
        this.text = text;
        this.status = status;
    }

    /**
     * Create a response
     *
     * @param status  the response status (usually an error)
     */
    public Response(ResponseStatus status) {
        this.status = status;
    }

    /**
     * Get the text of the response
     *
     * @return the text of the response or null if error
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
     * Get the response status
     *
     * @return the status
     */
    public ResponseStatus getStatus() {
        return status;
    }

    /**
     * Set the response status
     *
     * @param status  the status
     */
    public void setStatus(ResponseStatus status) {
        this.status = status;
    }

    /**
     * Is this a successful response to the request?
     *
     * @return true for success
     */
    public boolean isSuccess() {
        return status.isSuccess();
    }
}
