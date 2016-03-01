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

/**
 * Web request
 * <p>
 * This class defines the json object expected as the body of a request
 */
@XmlRootElement
public class WebRequest {
    private String text;

    /**
     * Empty constructor required by Jersey
     */
    public WebRequest() {}

    /**
     * Create a web request
     *
     * @param text  the text of the request
     */
    public WebRequest(String text) {
        this.text = text;
    }

    /**
     * Get the text of the request
     *
     * @return the text of the request
     */
    public String getText() {
        return text;
    }

    /**
     * Set the text of the request
     *
     * @param text  the text of the request
     */
    public void setText(String text) {
        this.text = text;
    }
}
