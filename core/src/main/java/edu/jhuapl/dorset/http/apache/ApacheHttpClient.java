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
package edu.jhuapl.dorset.http.apache;

import edu.jhuapl.dorset.http.AbstractHttpClient;
import edu.jhuapl.dorset.http.HttpClient;
import edu.jhuapl.dorset.http.HttpParameter;
import edu.jhuapl.dorset.http.HttpRequest;
import edu.jhuapl.dorset.http.HttpResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.entity.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApacheHttpClient extends AbstractHttpClient implements HttpClient {
    private final Logger logger = LoggerFactory.getLogger(ApacheHttpClient.class);

    @Override
    public HttpResponse execute(HttpRequest request) {
        HttpResponse httpResponse = null;
        Response apacheResponse = null;
        try {
            switch (request.getMethod()) {
                case GET:
                    apacheResponse = get(request);
                    break;
                case POST:
                    apacheResponse = post(request);
                    break;
                case PUT:
                    apacheResponse = put(request);
                    break;
                case DELETE:
                    apacheResponse = delete(request);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown http method " + request.getMethod());
            }
        } catch (IOException e) {
            logger.error("Failed to get the http response for " + request.getUrl(), e);
        }

        if (apacheResponse != null) {
            try {
                httpResponse = new ApacheHttpResponse(apacheResponse);
            } catch (IOException e) {
                logger.error("Failed to parse the http response for " + request.getUrl(), e);
            }
        }

        return httpResponse;
    }

    private void prepareRequest(Request apacheRequest) {
        if (getUserAgent() != null) {
            apacheRequest.userAgent(getUserAgent());
        }
        if (getConnectTimeout() != null) {
            apacheRequest.connectTimeout(getConnectTimeout());
        }
        if (getReadTimeout() != null) {
            apacheRequest.socketTimeout(getReadTimeout());
        }

        if (!requestHeaders.isEmpty()) {
            for (Map.Entry<String, String> entry : requestHeaders.entrySet()) {
                apacheRequest.addHeader(entry.getKey(), entry.getValue());
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

    private Response get(HttpRequest request) throws IOException {
        Request apacheRequest = Request.Get(request.getUrl());
        prepareRequest(apacheRequest);
        return apacheRequest.execute();
    }

    private Response post(HttpRequest request) throws IOException {
        Request apacheRequest = Request.Post(request.getUrl());
        if (request.getBody() != null) {
            ContentType ct = ContentType.create(request.getContentType().getMimeType(),
                            request.getContentType().getCharset());
            apacheRequest.bodyString(request.getBody(), ct);
        } else if (request.getBodyForm() != null) {
            apacheRequest.bodyForm(buildFormBody(request.getBodyForm()));
        }
        prepareRequest(apacheRequest);
        return apacheRequest.execute();
    }

    private Response put(HttpRequest request) throws IOException {
        Request apacheRequest = Request.Put(request.getUrl());
        if (request.getBody() != null) {
            ContentType ct = ContentType.create(request.getContentType().getMimeType(),
                            request.getContentType().getCharset());
            apacheRequest.bodyString(request.getBody(), ct);
        } else if (request.getBodyForm() != null) {
            apacheRequest.bodyForm(buildFormBody(request.getBodyForm()));
        }
        prepareRequest(apacheRequest);
        return apacheRequest.execute();
    }

    private Response delete(HttpRequest request) throws IOException {
        Request apacheRequest = Request.Delete(request.getUrl());
        prepareRequest(apacheRequest);
        return apacheRequest.execute();
    }
}
