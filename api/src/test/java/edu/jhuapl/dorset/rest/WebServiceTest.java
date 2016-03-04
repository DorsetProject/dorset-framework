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
package edu.jhuapl.dorset.rest;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.test.JerseyTest;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.jhuapl.dorset.Application;
import edu.jhuapl.dorset.Request;
import edu.jhuapl.dorset.Response;
import edu.jhuapl.dorset.ResponseStatus;

public class WebServiceTest extends JerseyTest {

    private Application app;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        LogManager.getLogManager().reset();
        System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s: %5$s [%1$tc]%n");
        Handler handler = new FileHandler("target/unit-tests.log");
        handler.setFormatter(new SimpleFormatter());
        Logger.getLogger("").addHandler(handler);
        Logger.getLogger("com.sun.jersey").setLevel(Level.INFO);
    }

    @Override
    protected javax.ws.rs.core.Application configure() {
        app = mock(Application.class);
        return new TestJaxrsApplication(app);
    }

    @Override
    @After
    public void tearDown() throws Exception {
        super.tearDown();
        reset(app);
    }

    @Test
    public void testRequest() {
        Response resp = new Response("this is a test");
        when(app.process(any(Request.class))).thenReturn(resp);

        WebRequest wr = new WebRequest("why?");
        Entity<WebRequest> body = Entity.entity(wr, MediaType.APPLICATION_JSON_TYPE);
        javax.ws.rs.core.Response response = target("/request").request(MediaType.APPLICATION_JSON_TYPE).post(body);

        assertEquals(200, response.getStatus());
        String expected = "{\"type\":\"text\",\"text\":\"this is a test\"}";
        assertEquals(expected, response.readEntity(String.class));
    }

    @Test
    public void testRequestWithError() {
        Response resp = new Response(new ResponseStatus(ResponseStatus.Code.AGENT_DID_NOT_KNOW_ANSWER, "Huh?"));
        when(app.process(any(Request.class))).thenReturn(resp);

        WebRequest wr = new WebRequest("why?");
        Entity<WebRequest> body = Entity.entity(wr, MediaType.APPLICATION_JSON_TYPE);
        javax.ws.rs.core.Response response = target("/request").request(MediaType.APPLICATION_JSON_TYPE).post(body);

        assertEquals(200, response.getStatus());
        String expected = "{\"type\":\"error\",\"text\":null,\"error\":{\"code\":201,\"message\":\"Huh?\"}}";
        assertEquals(expected, response.readEntity(String.class));
    }

    @Test
    public void testPing() {
        javax.ws.rs.core.Response response = target("/ping").request(MediaType.APPLICATION_JSON_TYPE).get();

        assertEquals(200, response.getStatus());
        String expected = "\"pong\"";
        assertEquals(expected, response.readEntity(String.class));        
    }

}
