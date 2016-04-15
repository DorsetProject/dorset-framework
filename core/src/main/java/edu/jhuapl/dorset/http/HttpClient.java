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
 * HTTP Client
 * <p>
 * Provides a simple interface for making HTTP requests.
 */
public interface HttpClient {
    /**
     * Execute an http request
     * 
     * @param request  A request object
     * @return  a response object or null if a fatal error occurred with request 
     */
    public HttpResponse execute(HttpRequest request);

    /**
     * Set the user agent
     *
     * @param agent  user agent string
     */
    public void setUserAgent(String agent);

    /**
     * Get the user agent string
     *
     * @return  the user agent string
     */
    public String getUserAgent();

    /**
     * Set the connect timeout (wait for connection to be established)
     *
     * @param timeout  connect timeout in milliseconds (0 equals an infinite timeout)
     */
    public void setConnectTimeout(int timeout);

    /**
     * Get the connect timeout 
     *
     * @return  the connect timeout in milliseconds
     */
    public Integer getConnectTimeout();

    /**
     * Set the read timeout (wait for data during transfer)
     *
     * @param timeout  read timeout in milliseconds (0 equals an infinite timeout)
     */
    public void setReadTimeout(int timeout);

    /**
     * Get the read timeout
     *
     * @return  the read timeout
     */
    public Integer getReadTimeout();

    /**
     * Add a request header that will be used on every request
     *
     * Overrides any previous request header values with the same name.
     *
     * @param name  the name of the header
     * @param value  the value of the header
     */
    public void addDefaultRequestHeader(String name, String value);
}
