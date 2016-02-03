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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.impl.client.HttpClients;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpClient {
    private final Logger logger = LoggerFactory.getLogger(HttpClient.class);

    private CloseableHttpClient client;

    public HttpClient() {
        client = HttpClients.createDefault();
    }

    /**
     * Get the http response using a GET request
     * @param url The URL to get
     * @return the http response text
     */
    public String get(String url) {
        StringBuffer buffer = new StringBuffer();
        HttpGet httpget = new HttpGet(url);
        try {
            CloseableHttpResponse response = client.execute(httpget);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream in = entity.getContent();
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String inputLine;
                while ((inputLine = br.readLine()) != null) {
                    buffer.append(inputLine);
                }
                br.close();
            }
            response.close();
        } catch (IOException e) {
            logger.error("Failed to get the http response for " + url, e);
        }

        return buffer.toString();
    }
}
