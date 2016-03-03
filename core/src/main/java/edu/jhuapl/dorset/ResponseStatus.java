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

public class ResponseStatus {
    private final Code code;
    private final String message;

    public ResponseStatus(Code code) {
        this.code = code;
        message = messageMap.get(code);
    }

    public ResponseStatus(Code code, String message) {
        this.code = code;
        this.message = message;
    }

    public boolean isSuccess() {
        return code == Code.SUCCESS;
    }

    public Code getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    static ResponseStatus createSuccess() {
        return new ResponseStatus(Code.SUCCESS);
    }

    public enum Code {
        SUCCESS(0),
        INTERNAL_ERROR(100),
        NO_AVAILABLE_AGENT(101),
        AGENT_DID_NOT_ANSWER(102);

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

    private static final Map<Code, String> messageMap = new HashMap<Code, String>();
    static {
        messageMap.put(Code.SUCCESS, "Success");
        messageMap.put(Code.INTERNAL_ERROR, "Something failed with this request.");
        messageMap.put(Code.NO_AVAILABLE_AGENT, "No agent was available to handle this request.");
        messageMap.put(Code.AGENT_DID_NOT_ANSWER, "The agent did not provide an answer.");
    }
}
