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

    public static HttpRequest Get(String url) {
        return new HttpRequest(HttpMethod.GET, url);
    }

    public static HttpRequest Post(String url) {
        return new HttpRequest(HttpMethod.POST, url);
    }

    public static HttpRequest Put(String url) {
        return new HttpRequest(HttpMethod.PUT, url);
    }

    public static HttpRequest Delete(String url) {
        return new HttpRequest(HttpMethod.DELETE, url);
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public String getBody() {
        return body;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public HttpRequest setBody(String body, ContentType contentType) {
        this.body = body;
        this.contentType = contentType;
        return this;
    }

    public HttpParameter[] getBodyForm() {
        return bodyForm;
    }

    public HttpRequest setBodyForm(HttpParameter[] parameters) {
        bodyForm = parameters.clone();
        return this;
    }
}
