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
package edu.jhuapl.dorset.http;

/**
 * HTTP Request Status
 */
public class HttpStatus {
    private int statusCode;
    private String reason;

    /**
     * Create an HttpStatus object
     *
     * @param code  HTTP status code
     * @param reason  HTTP status reason
     */
    public HttpStatus(int code, String reason) {
        this.setStatusCode(code);
        this.setReasonPhrase(reason);
    }

    /**
     * Get the status code
     *
     * @return status code
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * Set the status code
     *
     * @param statusCode  the status code
     */
    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    /**
     * Get the HTTP reason phrase
     *
     * @return reason phrase
     */
    public String getReasonPhrase() {
        return reason;
    }

    /**
     * Set the HTTP reason phrase
     *
     * @param reason  the reason phrase
     */
    public void setReasonPhrase(String reason) {
        this.reason = reason;
    }

    /**
     * Was there a client error (such as a 404)?
     *
     * @return boolean
     */
    public boolean isClientError() {
        return statusCode >= 400 && statusCode < 500;
    }

    /**
     * Was there a server error?
     *
     * @return boolean
     */
    public boolean isServerError() {
        return statusCode >= 500;
    }

    /**
     * Was there any kind of error?
     *
     * @return boolean
     */
    public boolean isError() {
        return isClientError() || isServerError();
    }

    /**
     * Was the request a success?
     *
     * @return boolean
     */
    public boolean isSuccess() {
        return statusCode >= 200 && statusCode < 300;
    }
}
