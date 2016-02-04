package edu.jhuapl.dorset.http;

import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

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
        JsonObject jsonObj = getJsonObject(response);
        JsonObject args = jsonObj.get("args").getAsJsonObject();
        assertEquals(32, args.get("test").getAsInt());
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
        String response = client.get("http://httpbin.org/get");
        assertNull(response);
    }

    @Test
    public void testSetReadTimeout() {
        HttpClient client = new HttpClient();
        client.setReadTimeout(2000);
        String response = client.get("http://httpbin.org/delay/3");
        assertNull(response);

        client.setReadTimeout(4000);
        response = client.get("http://httpbin.org/delay/3");
        assertNotNull(response);
    }

}
