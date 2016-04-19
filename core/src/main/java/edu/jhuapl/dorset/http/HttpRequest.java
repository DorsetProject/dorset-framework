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

public class HttpRequest {
    private final HttpMethod method;
    private final String url;
    private String body;
    private HttpParameter[] bodyForm;
    private ContentType contentType;

    public HttpRequest(HttpMethod method, String url) {
        this.method = method;
        this.url = url;
    }

    /**
     * Create a GET request
     * @param url  the URL to get
     * @return http request object
     */
    public static HttpRequest get(String url) {
        return new HttpRequest(HttpMethod.GET, url);
    }

    /**
     * Create a POST request
     * @param url  the URL to post to
     * @return http request object
     */
    public static HttpRequest post(String url) {
        return new HttpRequest(HttpMethod.POST, url);
    }

    /**
     * Create a PUT request
     * @param url  the URL 
     * @return http request object
     */
    public static HttpRequest put(String url) {
        return new HttpRequest(HttpMethod.PUT, url);
    }

    /**
     * Create a DELETE request
     * @param url  the URL to delete
     * @return http request object
     */
    public static HttpRequest delete(String url) {
        return new HttpRequest(HttpMethod.DELETE, url);
    }

    /**
     * Get the method of the http request
     * @return the http method
     */
    public HttpMethod getMethod() {
        return method;
    }

    /**
     * Get the URL of the http request
     * @return the URL
     */
    public String getUrl() {
        return url;
    }

    /**
     * Get the body of the http request
     * @return the body as a string
     */
    public String getBody() {
        return body;
    }

    /**
     * Get the content type of the http request
     * @return the content type object
     */
    public ContentType getContentType() {
        return contentType;
    }

    /**
     * Set the body of the http request
     * @param body  the body as a string
     * @param contentType  the content type for the request
     * @return self
     */
    public HttpRequest setBody(String body, ContentType contentType) {
        this.body = body;
        this.contentType = contentType;
        return this;
    }

    /**
     * Get the http parameters for the body of the request
     * @return an array of http parameters
     */
    public HttpParameter[] getBodyForm() {
        return bodyForm;
    }

    /**
     * Set the body form http parameters
     * @param parameters  the http parameters to set in the body
     * @return self
     */
    public HttpRequest setBodyForm(HttpParameter[] parameters) {
        bodyForm = parameters.clone();
        return this;
    }
}
