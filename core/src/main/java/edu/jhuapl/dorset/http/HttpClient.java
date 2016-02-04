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

import java.io.IOException;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HTTP Client
 * 
 * Provides a simple interface for making Get and Post requests.
 * 
 * Additional functionality can be exposed as needed such as http proxy support,
 * additional http header controls, getting the response as a byte array, and
 * authorization.
 */
public class HttpClient {
    private final Logger logger = LoggerFactory.getLogger(HttpClient.class);

    private String userAgent;
    private Integer connectTimeout;
    private Integer readTimeout; 

    /**
     * Get the http response to a GET request
     * @param url The URL to get
     * @return the http response text
     */
    public String get(String url) {
        String text = null;
        Request request = Request.Get(url);
        prepareRequest(request);
        Response response;
        try {
            response = request.execute();
            text = response.returnContent().asString();
        } catch (IOException e) {
            logger.error("Failed to get the http response for getting " + url, e);
        }
        return text;
    }

    /**
     * Get the http response to a POST request
     * @param url The URL to post to
     * @param parameters array of parameters
     * @return the http response text
     */
    public String post(String url, HttpParameter[] parameters) {
        String text = null;
        Request request = Request.Post(url);
        prepareRequest(request);
        if (parameters != null) {
            request.bodyForm(buildFormBody(parameters));
        }
        Response response;
        try {
            response = request.execute();
            text = response.returnContent().asString();
        } catch (IOException e) {
            logger.error("Failed to get the http response for posting " + url, e);
        }
        return text;
    }

    /**
     * Set the user agent
     * @param agent user agent string
     */
    public void setUserAgent(String agent) {
        userAgent = agent;
    }

    /**
     * Set the connect timeout (wait for connection to be established)
     * @param timeout connect timeout in milliseconds (0 equals an infinite timeout)
     */
    public void setConnectTimeout(int timeout) {
        connectTimeout = timeout;
    }

    /**
     * Set the read timeout (wait for data during transfer)
     * @param timeout read timeout in milliseconds (0 equals an infinite timeout)
     */
    public void setReadTimeout(int timeout) {
        readTimeout = timeout;
    }

    private void prepareRequest(Request request) {
        if (userAgent != null) {
            request.userAgent(userAgent);
        }
        if (connectTimeout != null) {
            request.connectTimeout(connectTimeout);
        }
        if (readTimeout != null) {
            request.socketTimeout(readTimeout);
        }
    }

    private List<NameValuePair> buildFormBody(HttpParameter[] parameters) {
        Form form = Form.form();
        for (HttpParameter param : parameters) {
            form.add(param.getName(), param.getValue());
        }
        return form.build();
    }
}
