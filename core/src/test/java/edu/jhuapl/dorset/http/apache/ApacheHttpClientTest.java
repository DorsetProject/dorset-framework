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

import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import edu.jhuapl.dorset.http.ContentType;
import edu.jhuapl.dorset.http.HttpClient;
import edu.jhuapl.dorset.http.HttpParameter;
import edu.jhuapl.dorset.http.HttpRequest;
import edu.jhuapl.dorset.http.HttpResponse;

// ignore because of external dependencies and they take several seconds to run
@Ignore
public class ApacheHttpClientTest {
    private Gson gson = new Gson();

    private JsonObject getJsonObject(String response) {
        return gson.fromJson(response, JsonObject.class);
    }

    @Test
    public void testSimpleGet() {
        HttpClient client = new ApacheHttpClient();

        HttpResponse response = client.execute(HttpRequest.get("http://httpbin.org/get?test=32"));

        assertNotNull(response);
        assertFalse(response.isError());
        assertTrue(response.isSuccess());
        JsonObject jsonObj = getJsonObject(response.asString());
        JsonObject args = jsonObj.get("args").getAsJsonObject();
        assertEquals(32, args.get("test").getAsInt());
    }

    @Test
    public void testSimpleDelete() {
        HttpClient client = new ApacheHttpClient();

        HttpResponse response = client.execute(HttpRequest.delete("http://httpbin.org/delete"));

        assertNotNull(response);
        assertTrue(response.isSuccess());
    }

    @Test
    public void testSimplePost() {
        HttpClient client = new ApacheHttpClient();
        HttpParameter[] p = new HttpParameter[]{new HttpParameter("a", "b"), new HttpParameter("c", "d")};

        HttpRequest request = HttpRequest.post("http://httpbin.org/post").setBodyForm(p);
        HttpResponse response = client.execute(request);

        assertNotNull(response);
        assertTrue(response.isSuccess());
        JsonObject jsonObj = getJsonObject(response.asString());
        JsonObject formData = jsonObj.get("form").getAsJsonObject();
        assertEquals("b", formData.get("a").getAsString());
        assertEquals("d", formData.get("c").getAsString());
    }

    @Test
    public void testPostWithJsonBody() {
        HttpClient client = new ApacheHttpClient();

        HttpRequest request = HttpRequest.post("http://httpbin.org/post").setBody("{\"value\":\"bar\"}", ContentType.APPLICATION_JSON);
        HttpResponse response = client.execute(request);

        assertNotNull(response);
        assertTrue(response.isSuccess());
        JsonObject jsonObj = getJsonObject(response.asString());
        JsonObject headers = jsonObj.get("headers").getAsJsonObject();
        assertEquals("application/json; charset=UTF-8", headers.get("Content-Type").getAsString());
        JsonObject jsonData = jsonObj.get("json").getAsJsonObject();
        assertEquals("bar", jsonData.get("value").getAsString());
    }

    @Test
    public void testSimplePut() {
        HttpClient client = new ApacheHttpClient();
        HttpParameter[] p = new HttpParameter[]{new HttpParameter("a", "b"), new HttpParameter("c", "d")};

        HttpRequest request = HttpRequest.put("http://httpbin.org/put").setBodyForm(p);
        HttpResponse response = client.execute(request);

        assertNotNull(response);
        assertTrue(response.isSuccess());
        JsonObject jsonObj = getJsonObject(response.asString());
        JsonObject formData = jsonObj.get("form").getAsJsonObject();
        assertEquals("b", formData.get("a").getAsString());
        assertEquals("d", formData.get("c").getAsString());
    }

    @Test
    public void testSetUserAgent() {
        HttpClient client = new ApacheHttpClient();

        client.setUserAgent("Dorset HttpClient Test");
        HttpResponse response = client.execute(HttpRequest.get("http://httpbin.org/user-agent"));

        assertNotNull(response);
        assertTrue(response.isSuccess());
        JsonObject jsonObj = getJsonObject(response.asString());
        assertEquals("Dorset HttpClient Test", jsonObj.get("user-agent").getAsString());
    }

    @Test
    public void testSetConnectTimeout() {
        HttpClient client = new ApacheHttpClient();

        client.setConnectTimeout(1);
        // non-routable ip address that should cause a connection timeout
        // this does not test the setTimeout method as much as it tests that
        // a null is returned for a timeout
        HttpResponse response = client.execute(HttpRequest.get("http://10.255.255.1/"));

        assertNull(response);
    }

    @Test
    public void testSetReadTimeout() {
        HttpClient client = new ApacheHttpClient();
        client.setReadTimeout(2000);
        HttpResponse response = client.execute(HttpRequest.get("http://httpbin.org/delay/4"));
        assertNull(response);

        client.setReadTimeout(4000);
        response = client.execute(HttpRequest.get("http://httpbin.org/delay/3"));
        assertNotNull(response);
    }

    @Test
    public void test404Status() {
        HttpClient client = new ApacheHttpClient();
        HttpResponse response = client.execute(HttpRequest.get("https://httpbin.org/status/404"));
        assertNotNull(response);
        assertTrue(response.isClientError());
        assertFalse(response.isServerError());
        assertTrue(response.isError());
        assertFalse(response.isSuccess());
        assertEquals("NOT FOUND", response.getReasonPhrase());
    }

    @Test
    public void test500Status() {
        HttpClient client = new ApacheHttpClient();
        HttpResponse response = client.execute(HttpRequest.get("https://httpbin.org/status/500"));
        assertNotNull(response);
        assertFalse(response.isClientError());
        assertTrue(response.isServerError());
        assertTrue(response.isError());
        assertFalse(response.isSuccess());
        assertEquals("INTERNAL SERVER ERROR", response.getReasonPhrase());
    }

    @Test
    public void testRedirect() {
        HttpClient client = new ApacheHttpClient();
        // 302 redirect to a get request
        HttpResponse response = client.execute(HttpRequest.get("http://httpbin.org/redirect-to?url=http%3A%2F%2Fhttpbin.org%2Fget%3Ftest%3D32"));
        assertNotNull(response);
        assertTrue(response.isSuccess());
        JsonObject jsonObj = getJsonObject(response.asString());
        JsonObject args = jsonObj.get("args").getAsJsonObject();
        assertEquals(32, args.get("test").getAsInt());
    }

    @Test
    public void testHttpHeaders() {
        HttpClient client = new ApacheHttpClient();
        client.addDefaultRequestHeader("Testing", "dorset");
        HttpResponse response = client.execute(HttpRequest.get("https://httpbin.org/headers"));
        assertNotNull(response);
        JsonObject jsonObj = getJsonObject(response.asString());
        JsonObject headers = jsonObj.get("headers").getAsJsonObject();
        assertEquals("dorset", headers.get("Testing").getAsString());
    }

    @Test
    public void testGetImage() {
        HttpClient client = new ApacheHttpClient();

        HttpResponse response = client.execute(HttpRequest.get("https://httpbin.org/image/jpeg"));

        assertNotNull(response);
        assertTrue(response.isSuccess());
        byte[] jpeg = response.asBytes();
        assertEquals((byte)0xFF, jpeg[0]);
        assertEquals((byte)0xd8, jpeg[1]);
        assertEquals(35588, jpeg.length);
    }
}
