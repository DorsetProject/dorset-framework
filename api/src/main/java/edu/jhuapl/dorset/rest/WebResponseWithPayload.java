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

import edu.jhuapl.dorset.Response;

/**
 * Web response with payload data
 * <p>
 * Extends the WebResponse with the optional payload information
 */
@XmlRootElement
public class WebResponseWithPayload extends WebResponse {
    private final String payload;

    /**
     * Create a web response with a payload
     *
     * @param resp  the response from the Dorset application
     */
    public WebResponseWithPayload(Response resp) {
        super(resp);
        this.payload = resp.getPayload();
    }

    /**
     * Get the payload string
     *
     * @return the payload
     */
    public String getPayload() {
        return payload;
    }
}
