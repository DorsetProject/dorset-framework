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

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractHttpClient implements HttpClient {
    protected String userAgent;
    protected Integer connectTimeout;
    protected Integer readTimeout;
    protected Map<String, String> requestHeaders = new HashMap<String, String>();

    @Override
    public void setUserAgent(String agent) {
        userAgent = agent;
    }

    @Override
    public String getUserAgent() {
        return userAgent;
    }

    @Override
    public void setConnectTimeout(int timeout) {
        connectTimeout = timeout;
    }

    @Override
    public Integer getConnectTimeout() {
        return connectTimeout;
    }

    @Override
    public void setReadTimeout(int timeout) {
        readTimeout = timeout;
    }

    @Override
    public Integer getReadTimeout() {
        return readTimeout;
    }

    @Override
    public void addDefaultRequestHeader(String name, String value) {
        requestHeaders.put(name, value);
    }

}
