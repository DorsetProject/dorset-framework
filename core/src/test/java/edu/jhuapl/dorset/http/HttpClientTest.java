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

import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

// ignore because of external dependencies and they take several seconds to run
@Ignore
public class HttpClientTest {
    private Gson gson = new Gson();

    private JsonObject getJsonObject(String response) {
        return gson.fromJson(response, JsonObject.class);
    }

    @Test
    public void testSimpleGet() {
        HttpClient client = new HttpClient();

        String response = client.get("http://httpbin.org/get?test=32");

        assertNotNull(response);
        assertNotNull(client.getHttpStatus());
        assertFalse(client.getHttpStatus().isError());
        assertTrue(client.getHttpStatus().isSuccess());
        JsonObject jsonObj = getJsonObject(response);
        JsonObject args = jsonObj.get("args").getAsJsonObject();
        assertEquals(32, args.get("test").getAsInt());
    }

    @Test
    public void testSimpleDelete() {
        HttpClient client = new HttpClient();

        String response = client.delete("http://httpbin.org/delete");

        assertNotNull(response);
    }

    @Test
    public void testSimplePost() {
        HttpClient client = new HttpClient();
        HttpParameter[] p = new HttpParameter[]{new HttpParameter("a", "b"), new HttpParameter("c", "d")};

        String response = client.post("http://httpbin.org/post", p);

        assertNotNull(response);
        JsonObject jsonObj = getJsonObject(response);
        JsonObject formData = jsonObj.get("form").getAsJsonObject();
        assertEquals("b", formData.get("a").getAsString());
        assertEquals("d", formData.get("c").getAsString());
    }

    @Test
    public void testSimplePut() {
        HttpClient client = new HttpClient();
        HttpParameter[] p = new HttpParameter[]{new HttpParameter("a", "b"), new HttpParameter("c", "d")};

        String response = client.put("http://httpbin.org/put", p);

        assertNotNull(response);
        JsonObject jsonObj = getJsonObject(response);
        JsonObject formData = jsonObj.get("form").getAsJsonObject();
        assertEquals("b", formData.get("a").getAsString());
        assertEquals("d", formData.get("c").getAsString());
    }

    @Test
    public void testSetUserAgent() {
        HttpClient client = new HttpClient();
        client.setUserAgent("Dorset HttpClient Test");
        String response = client.get("http://httpbin.org/user-agent");
        assertNotNull(response);
        JsonObject jsonObj = getJsonObject(response);
        assertEquals("Dorset HttpClient Test", jsonObj.get("user-agent").getAsString());
    }

    @Test
    public void testSetConnectTimeout() {
        HttpClient client = new HttpClient();
        client.setConnectTimeout(1);
        // non-routable ip address that should cause a connection timeout
        // this does not test the setTimeout method as much as it tests that
        // a null is returned for a timeout
        String response = client.get("http://10.255.255.1/");
        assertNull(response);
    }

    @Test
    public void testSetReadTimeout() {
        HttpClient client = new HttpClient();
        client.setReadTimeout(2000);
        String response = client.get("http://httpbin.org/delay/4");
        assertNull(response);

        client.setReadTimeout(4000);
        response = client.get("http://httpbin.org/delay/3");
        assertNotNull(response);
    }

    @Test
    public void test404Status() {
        HttpClient client = new HttpClient();
        String response = client.get("https://httpbin.org/status/404");
        assertNull(response);
        assertNotNull(client.getHttpStatus());
        assertTrue(client.getHttpStatus().isClientError());
        assertFalse(client.getHttpStatus().isServerError());
        assertTrue(client.getHttpStatus().isError());
        assertFalse(client.getHttpStatus().isSuccess());
        assertEquals("NOT FOUND", client.getHttpStatus().getReasonPhrase());
    }

    @Test
    public void test500Status() {
        HttpClient client = new HttpClient();
        String response = client.get("https://httpbin.org/status/500");
        assertNull(response);
        assertNotNull(client.getHttpStatus());
        assertFalse(client.getHttpStatus().isClientError());
        assertTrue(client.getHttpStatus().isServerError());
        assertTrue(client.getHttpStatus().isError());
        assertFalse(client.getHttpStatus().isSuccess());
        assertEquals("INTERNAL SERVER ERROR", client.getHttpStatus().getReasonPhrase());
    }

    @Test
    public void testRedirect() {
        HttpClient client = new HttpClient();
        // 302 redirect to a get request
        String response = client.get("http://httpbin.org/redirect-to?url=http%3A%2F%2Fhttpbin.org%2Fget%3Ftest%3D32");
        assertNotNull(response);
        assertNotNull(client.getHttpStatus());
        assertTrue(client.getHttpStatus().isSuccess());
        JsonObject jsonObj = getJsonObject(response);
        JsonObject args = jsonObj.get("args").getAsJsonObject();
        assertEquals(32, args.get("test").getAsInt());
    }

    @Test
    public void testHttpHeaders() {
        HttpClient client = new HttpClient();
        client.addDefaultRequestHeader("Testing", "dorset");
        String response = client.get("https://httpbin.org/headers");
        assertNotNull(response);
        System.out.println(response);
        JsonObject jsonObj = getJsonObject(response);
        JsonObject headers = jsonObj.get("headers").getAsJsonObject();
        assertEquals("dorset", headers.get("Testing").getAsString());
    }
}
