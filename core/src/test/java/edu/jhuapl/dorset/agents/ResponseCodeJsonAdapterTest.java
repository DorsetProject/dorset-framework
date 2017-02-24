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

import edu.jhuapl.dorset.ResponseStatus;

public class ResponseCodeJsonAdapterTest {

    private Gson getGson() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(ResponseStatus.Code.class, new ResponseCodeJsonAdapter().nullSafe());
        return builder.create();
    }

    @Test
    public void testDeserialization() {
        String text1 = "{\"code\":0,\"message\":\"Success\"}";
        String text2 = "{\"code\":100,\"message\":\"Error\"}";
        Gson gson = getGson();

        ResponseStatus status1 = gson.fromJson(text1, ResponseStatus.class);
        assertEquals(ResponseStatus.Code.SUCCESS, status1.getCode());
        ResponseStatus status2 = gson.fromJson(text2, ResponseStatus.class);
        assertEquals(ResponseStatus.Code.INTERNAL_ERROR, status2.getCode());
    }

    @Test
    public void testDeserializationWithNullCode() {
        String text = "{\"code\":null,\"message\":\"Success\"}";
        Gson gson = getGson();

        ResponseStatus status = gson.fromJson(text, ResponseStatus.class);
        assertNull(status.getCode());
    }

    @Test
    public void testSerialization() {
        String expected = "{\"code\":0,\"message\":\"Success\"}";
        ResponseStatus status = new ResponseStatus(ResponseStatus.Code.SUCCESS);
        Gson gson = getGson();

        assertEquals(expected, gson.toJson(status));
    }

}
