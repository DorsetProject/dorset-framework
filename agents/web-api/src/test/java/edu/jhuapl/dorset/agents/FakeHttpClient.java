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
package edu.jhuapl.dorset.agents;

import edu.jhuapl.dorset.http.HttpClient;
import edu.jhuapl.dorset.http.HttpRequest;
import edu.jhuapl.dorset.http.HttpResponse;

public class FakeHttpClient implements HttpClient {
    private HttpResponse response;

    public FakeHttpClient(HttpResponse response) {
        this.response = response;
    }

    @Override
    public HttpResponse execute(HttpRequest request) {
        return response;
    }

    @Override
    public void setUserAgent(String agent) {}

    @Override
    public String getUserAgent() {
        return "FakeHttpClient";
    }

    @Override
    public void setConnectTimeout(int timeout) {}

    @Override
    public Integer getConnectTimeout() {
        return 0;
    }

    @Override
    public void setReadTimeout(int timeout) {}

    @Override
    public Integer getReadTimeout() {
        return 0;
    }

    @Override
    public void addDefaultRequestHeader(String name, String value) {}

}
