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

import edu.jhuapl.dorset.agent.AgentResponse;

/**
 * Dorset Response
 * <p>
 * Represents the response to a request to the application.
 * The type field determines what fields are used.
 * The text field can be null if an error occurred.
 */
public class Response {
    private final Type type;
    private final String text;
    private final String payload;
    private final ResponseStatus status;

    /**
     * Create a response
     *
     * @param text  the text of the response
     */
    public Response(String text) {
        this.type = Type.TEXT;
        this.text = text;
        this.payload = null;
        this.status = ResponseStatus.createSuccess();
    }

    /**
     * Create a response with a payload
     *
     * @param type  the type of the response
     * @param text  the text of the response
     * @param payload  the payload of the response
     */
    public Response(Type type, String text, String payload) {
        this.type = type;
        this.text = text;
        this.payload = payload;
        this.status = ResponseStatus.createSuccess();
    }

    /**
     * Create a response
     *
     * @param response  response from an agent
     */
    public Response(AgentResponse response) {
        this.type = response.getType();
        this.text = response.getText();
        this.payload = response.getPayload();
        this.status = response.getStatus();
    }

    /**
     * Create a response
     *
     * @param text  the text of the response
     * @param status  the response status
     */
    public Response(String text, ResponseStatus status) {
        if (status.isSuccess()) {
            this.type = Type.TEXT;
        } else {
            this.type = Type.ERROR;
        }
        this.text = text;
        this.payload = null;
        this.status = status;
    }

    /**
     * Create a response
     *
     * @param status  the response status (an error)
     */
    public Response(ResponseStatus status) {
        this.type = Type.ERROR;
        this.text = null;
        this.payload = null;
        this.status = status;
    }

    /**
     * Get the response type
     *
     * @return the response type
     */
    public Type getType() {
        return type;
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
     * Get the payload of the response
     * <p>
     * The payload data varies according to the response type
     *
     * @return payload string or null if no payload
     */
    public String getPayload() {
        return payload;
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
     * Is this a successful response to the request?
     *
     * @return true for success
     */
    public boolean isSuccess() {
        return status.isSuccess();
    }

    /**
     * Is this a simple text response?
     *
     * @return true if text response
     */
    public boolean isTextResponse() {
        return type == Type.TEXT;
    }

    /**
     * Does this response have a payload?
     *
     * @return true if there is a payload
     */
    public boolean hasPayload() {
        return Type.usesPayload(type);
    }

    /**
     * Enumeration for response types
     */
    public enum Type {
        ERROR("error"),
        TEXT("text"),
        EMBEDDED_IMAGE("embedded_image");

        private final String type;
        private Type(String type) {
            this.type = type;
        }

        /**
         * Get the value of the type
         *
         * @return the value
         */
        public String getValue() {
            return type;
        }

        /**
         * Get a Type enum member from a string
         *
         * @param value  the type as a string
         * @return a Type enum member or null if no match
         */
        public static Type fromValue(String value) {
            for (Type type : Type.values()) {
                if (type.getValue().equals(value)) {
                    return type;
                }
            }
            return null;
        }

        /**
         * Does this response type use the payload field?
         *
         * @param type the response type
         * @return true if it uses the payload field
         */
        public static boolean usesPayload(Type type) {
            switch (type) {
                case EMBEDDED_IMAGE:
                    return true;
                default:
                    return false;
            }
        }
    }
}
