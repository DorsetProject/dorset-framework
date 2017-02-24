/*
 * Copyright 2017 The Johns Hopkins University Applied Physics Laboratory LLC
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
package edu.jhuapl.dorset.agents;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import edu.jhuapl.dorset.Response;
import edu.jhuapl.dorset.ResponseStatus;

public class ResponseTypeJsonAdapterTest {

    private Gson getGson() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Response.Type.class, new ResponseTypeJsonAdapter().nullSafe());
        builder.registerTypeAdapter(ResponseStatus.Code.class, new ResponseCodeJsonAdapter().nullSafe());
        return builder.create();
    }

    @Test
    public void testDeserialization() {
        String text = "{\"text\":\"test\",\"type\":\"text\",\"status\":{\"message\":\"Success\",\"code\":0}}";
        Gson gson = getGson();

        Response response = gson.fromJson(text, Response.class);

        assertEquals(Response.Type.TEXT, response.getType());
    }

    @Test
    public void testDeserializationWithUnknownType() {
        String text = "{\"text\":\"test\",\"type\":\"unknown\",\"status\":{\"message\":\"Success\",\"code\":0}}";
        Gson gson = getGson();

        Response response = gson.fromJson(text, Response.class);

        assertNull(response.getType());
    }

    @Test
    public void testSerialization() {
        String expected = "{\"type\":\"text\",\"text\":\"test\",\"status\":{\"code\":0,\"message\":\"Success\"}}";
        Response response = new Response("test");
        Gson gson = getGson();

        assertEquals(expected, gson.toJson(response));
    }
}
