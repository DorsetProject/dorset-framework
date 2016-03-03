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
package edu.jhuapl.dorset.rest;

import javax.xml.bind.annotation.XmlRootElement;

import edu.jhuapl.dorset.ResponseStatus;

@XmlRootElement
public class Error {
    private final int code;
    private final String message;

    /**
     * Create an error
     *
     * @param code  the error code
     * @param message  the error message
     */
    public Error(int code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * Create an error from a response status
     *
     * @param status  the response status with an error code
     */
    public Error(ResponseStatus status) {
        this.code = status.getCode().getValue();
        this.message = status.getMessage();
    }

    /**
     * Get the error code
     *
     * @return the error code
     */
    public int getCode() {
        return code;
    }

    /**
     * Get the error message
     *
     * @return the error message
     */
    public String getMessage() {
        return message;
    }
}
