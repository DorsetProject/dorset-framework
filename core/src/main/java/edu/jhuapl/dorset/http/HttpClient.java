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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HTTP Client
 * 
 * Provides a simple interface for making HTTP requests.
 * 
 * This client does not return the content body when there has been a client or
 * server error (status code 4xx or 5xx). It does handle redirects automatically.
 * 
 * Additional functionality can be added as needed such as http proxy support,
 * getting the response as a byte array, and authorization.
 */
public class HttpClient {
    private final Logger logger = LoggerFactory.getLogger(HttpClient.class);

    public static final String APPLICATION_JSON = "application/json";
    public static final String APPLICATION_XML = "application/xml";
    public static final String MULTIPART_FORM_DATA = "multipart/form-data";
    public static final String APPLICATION_FORM_URLENCODED = "application/x-www-form-urlencoded";

    private String userAgent;
    private Integer connectTimeout;
    private Integer readTimeout;
    private HttpStatus status;
    private final Map<String, String> requestHeaders = new HashMap<String, String>();

    /**
     * Get the http response to a GET request
     * @param url The URL to get
     * @return the http response text or null if error
     */
    public String get(String url) {
        String text = null;
        Request request = Request.Get(url);
        try {
            text = execute(request);
        } catch (IOException e) {
            logger.error("Failed to get the http response for getting " + url, e);
        }
        return text;
    }

    /**
     * Get the http response to a POST request
     * @param url The URL to post to
     * @param parameters array of parameters to encode as a form
     * @return the http response text or null if error
     */
    public String post(String url, HttpParameter[] parameters) {
        String text = null;
        Request request = Request.Post(url);
        if (parameters != null) {
            request.bodyForm(buildFormBody(parameters));
        }
        try {
            text = execute(request);
        } catch (IOException e) {
            logger.error("Failed to get the http response for posting " + url, e);
        }
        return text;
    }

    /**
     * Get the http response to a POST request
     * @param url The URL to post to
     * @param body The body of the POST request
     * @param contentType The content type string
     * @return the http response text or null if error
     */
    public String post(String url, String body, String contentType) {
        String text = null;
        Request request = Request.Post(url).bodyString(body, ContentType.create(contentType));
        try {
            text = execute(request);
        } catch (IOException e) {
            logger.error("Failed to get the http response for posting " + url, e);
        }
        return text;
    }

    /**
     * Get the http response to a PUT request
     * @param url The URL to put to
     * @param parameters array of parameters to encode as a form
     * @return the http response text or null if error
     */
    public String put(String url, HttpParameter[] parameters) {
        String text = null;
        Request request = Request.Put(url);
        if (parameters != null) {
            request.bodyForm(buildFormBody(parameters));
        }
        try {
            text = execute(request);
        } catch (IOException e) {
            logger.error("Failed to get the http response for putting " + url, e);
        }
        return text;
    }

    /**
     * Get the http response to a PUT request
     * @param url The URL to put to
     * @param body The body of the PUT request
     * @param contentType The content type string
     * @return the http response text or null if error
     */
    public String put(String url, String body, String contentType) {
        String text = null;
        Request request = Request.Put(url).bodyString(body, ContentType.create(contentType));
        try {
            text = execute(request);
        } catch (IOException e) {
            logger.error("Failed to get the http response for putting " + url, e);
        }
        return text;
    }

    /**
     * Get the http response to a DELETE request
     * @param url The URL for the delete
     * @return the http response text or null if error
     */
    public String delete(String url) {
        String text = null;
        Request request = Request.Delete(url);
        try {
            text = execute(request);
        } catch (IOException e) {
            logger.error("Failed to get the http response for deleting " + url, e);
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

    /**
     * Add a request header that will be used on every request
     * @param name the name of the header
     * @param value the value of the header
     */
    public void addDefaultRequestHeader(String name, String value) {
        requestHeaders.put(name, value);
    }

    /**
     * Get the most recent status
     * @return status object
     */
    public HttpStatus getHttpStatus() {
        return status;
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
        if (!requestHeaders.isEmpty()) {
            for (Map.Entry<String, String> entry : requestHeaders.entrySet()) {
                request.addHeader(entry.getKey(), entry.getValue());
            }
        }
    }

    private List<NameValuePair> buildFormBody(HttpParameter[] parameters) {
        Form form = Form.form();
        for (HttpParameter param : parameters) {
            form.add(param.getName(), param.getValue());
        }
        return form.build();
    }

    private String execute(Request request) throws IOException {
        prepareRequest(request);
        Response response = request.execute();
        ContentAndStatus cas = response.handleResponse(new ContentAndStatusResponseHandler());
        status = new HttpStatus(cas.statusLine.getStatusCode(), cas.statusLine.getReasonPhrase());
        // redirects seem to be handled by Apache's HttpClient internally so not sure why
        // this check includes the 3xx range
        if (status.getStatusCode() >= 300) {
            throw new HttpResponseException(cas.statusLine.getStatusCode(),
                    cas.statusLine.getReasonPhrase());
        }

        return cas.content.asString();
    }

    class ContentAndStatus {
        public Content content;
        public StatusLine statusLine;
    }

    // based on Apache's ContentResponseHandler
    class ContentAndStatusResponseHandler implements ResponseHandler<ContentAndStatus> {

        @Override
        public ContentAndStatus handleResponse(HttpResponse response)
                throws HttpResponseException, IOException {
            ContentAndStatus cas = new ContentAndStatus();
            cas.statusLine = response.getStatusLine();
            HttpEntity entity = response.getEntity();
            if (cas.statusLine.getStatusCode() >= 300) {
                EntityUtils.consume(entity);
                return cas;
            }
            cas.content = entity == null ? null : handleEntity(entity);
            return cas;
        }

        private Content handleEntity(final HttpEntity entity) throws IOException {
            Content content = null;
            if (entity != null) {
                content = new Content(EntityUtils.toByteArray(entity),
                        ContentType.getOrDefault(entity));
            } else {
                content = Content.NO_CONTENT;
            }
            return content;
        }
    }
}
