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

import edu.jhuapl.dorset.Response;

public class WebResponseWithError extends WebResponse {
    private Error error;

    /**
     * Empty constructor required by Jersey
     */
    public WebResponseWithError() {}

    /**
     * Create a web response
     *
     * @param resp  the response from the Dorset application
     */
    public WebResponseWithError(Response resp) {
        error = new Error(resp.getStatus());
    }

    /**
     * Create a web response
     *
     * @param text  the text of the response
     */
    public WebResponseWithError(Error error) {
        this.error = error;
    }

    /**
     * Get the error for the response
     *
     * @return the error
     */
    public Error getError() {
        return error;
    }

    /**
     * Set the error for the response
     *
     * @param error  the error
     */
    public void setError(Error error) {
        this.error = error;
    }

}
