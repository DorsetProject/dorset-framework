package edu.jhuapl.dorset.rest;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.test.JerseyTest;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.jhuapl.dorset.Application;
import edu.jhuapl.dorset.Request;
import edu.jhuapl.dorset.Response;

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
    public void testProcess() {
        Response resp = new Response("this is a test");
        when(app.process(any(Request.class))).thenReturn(resp);

        javax.ws.rs.core.Response response = target("/process/test").request(MediaType.APPLICATION_JSON_TYPE).get();

        assertEquals(200, response.getStatus());
        String expected = "{\"text\":\"this is a test\"}";
        assertEquals(expected, response.readEntity(String.class));
    }

}