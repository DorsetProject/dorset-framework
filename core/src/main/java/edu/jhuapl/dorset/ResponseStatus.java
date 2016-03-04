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

import java.util.HashMap;
import java.util.Map;

/**
 * Response status
 * <p>
 * The codes are available as ResponseStatus.Code.
 * The messages can be customized or the default messages can be used.
 */
public class ResponseStatus {
    private final Code code;
    private final String message;

    /**
     * Create a response status based on a code
     * <p>
     * This uses the default message for the code.
     *
     * @param code  ResponseStatus.Code enum value
     */
    public ResponseStatus(Code code) {
        this.code = code;
        message = messageMap.get(code);
    }

    /**
     * Create a response status
     *
     * @param code  ResponseStatus.Code enum value
     * @param message  custom status message
     */
    public ResponseStatus(Code code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * Is this a successful response?
     *
     * @return true if success
     */
    public boolean isSuccess() {
        return code == Code.SUCCESS;
    }

    /**
     * Get the status code
     *
     * @return ResponseStatus.Code enum value
     */
    public Code getCode() {
        return code;
    }

    /**
     * Get the status message
     *
     * @return the status message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Factory method to create a default success status
     *
     * @return a success status
     */
    public static ResponseStatus createSuccess() {
        return new ResponseStatus(Code.SUCCESS);
    }

    public enum Code {
        SUCCESS(0),
        INTERNAL_ERROR(100),
        NO_AVAILABLE_AGENT(101),
        NO_RESPONSE_FROM_AGENT(102),
        INVALID_RESPONSE_FROM_AGENT(103),
        AGENT_DID_NOT_UNDERSTAND_REQUEST(200),
        AGENT_DID_NOT_KNOW_ANSWER(201),
        AGENT_CANNOT_COMPLETE_ACTION(202),
        AGENT_INTERNAL_ERROR(203);

        private final int code;
        private Code(int code) {
            this.code = code;
        }

        /**
         * Get the value of the code
         *
         * @return the value
         */
        public int getValue() {
            return code;
        }
    }

    // default messages
    private static final Map<Code, String> messageMap = new HashMap<Code, String>();
    static {
        messageMap.put(Code.SUCCESS, "Success");
        messageMap.put(Code.INTERNAL_ERROR, "Something failed with this request.");
        messageMap.put(Code.NO_AVAILABLE_AGENT, "No agent was available to handle this request.");
        messageMap.put(Code.NO_RESPONSE_FROM_AGENT, "The agent did not provide a response.");
        messageMap.put(Code.INVALID_RESPONSE_FROM_AGENT, "An error occurred getting response from agent.");
        messageMap.put(Code.AGENT_DID_NOT_UNDERSTAND_REQUEST, "The agent did not understand the request.");
        messageMap.put(Code.AGENT_DID_NOT_KNOW_ANSWER, "The agent did not know the answer.");
        messageMap.put(Code.AGENT_CANNOT_COMPLETE_ACTION, "The agent could not complete the requested action.");
        messageMap.put(Code.AGENT_INTERNAL_ERROR, "Something failed when handling this request.");
    }
}
