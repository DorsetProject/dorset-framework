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

import edu.jhuapl.dorset.http.AbstractHttpResponse;
import edu.jhuapl.dorset.http.HttpResponse;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.fluent.Response;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;

public class ApacheHttpResponse extends AbstractHttpResponse implements HttpResponse {
    private Response response;
    private Content content;
    private StatusLine status;

    public ApacheHttpResponse(Response response) throws IOException {
        this.response = response;
        handleResponse();
    }

    @Override
    public String asString() {
        return content.asString();
    }

    @Override
    public byte[] asBytes() {
        return content.asBytes();
    }

    @Override
    public int getStatusCode() {
        return status.getStatusCode();
    }

    @Override
    public String getReasonPhrase() {
        return status.getReasonPhrase();
    }

    protected void handleResponse() throws IOException {
        ContentAndStatus cas = response.handleResponse(new ContentAndStatusResponseHandler());
        content = cas.content;
        status = cas.statusLine;
    }

    class ContentAndStatus {
        public Content content;
        public StatusLine statusLine;
    }

    // based on Apache's ContentResponseHandler
    class ContentAndStatusResponseHandler implements ResponseHandler<ContentAndStatus> {

        @Override
        public ContentAndStatus handleResponse(org.apache.http.HttpResponse response)
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
