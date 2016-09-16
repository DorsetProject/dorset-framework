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
package edu.jhuapl.dorset.components.tools;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.jhuapl.dorset.Application;
import edu.jhuapl.dorset.agents.AbstractAgent;
import edu.jhuapl.dorset.agents.AgentRequest;
import edu.jhuapl.dorset.agents.AgentResponse;
import edu.jhuapl.dorset.routing.SingleAgentRouter;

/**
 *
 */
public class CommandLineClientTest {

    protected MockAgent mockAgent;
    protected String query;
    protected String expectedOutput;
    protected ByteArrayInputStream inContent;
    protected ByteArrayOutputStream outContent;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        query = "This is a test";
        expectedOutput = "> " + query + "\n> \nBye.\n";
        mockAgent = new MockAgent();
        inContent = new ByteArrayInputStream((query + "\nq\n").getBytes());
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }

    /**
     * Reset standard in and out.
     * 
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
        System.setIn(System.in);
        System.setOut(System.out);
    }

    /**
     * Test method for
     * {@link edu.jhuapl.dorset.components.tools.CommandLineClient#CommandLineClient(edu.jhuapl.dorset.agents.AbstractAgent)}.
     */
    @Test
    public void testCommandLineClientAbstractAgent() {
        CommandLineClient c = new CommandLineClient(mockAgent);
        testGo(c);
    }

    /**
     * Test method for
     * {@link edu.jhuapl.dorset.components.tools.CommandLineClient#CommandLineClient(edu.jhuapl.dorset.routing.Router)}.
     */
    @Test
    public void testCommandLineClientRouter() {
        CommandLineClient c = new CommandLineClient(new SingleAgentRouter(mockAgent));
        testGo(c);
    }

    /**
     * Test method for
     * {@link edu.jhuapl.dorset.components.tools.CommandLineClient#CommandLineClient(edu.jhuapl.dorset.Application)}.
     */
    @Test
    public void testCommandLineClientApplication() {
        Application app = new Application(new SingleAgentRouter(mockAgent));
        CommandLineClient c = new CommandLineClient(app);
        testGo(c);
    }

    protected void testGo(final CommandLineClient c) {
        Thread goThread = new Thread() {
            @Override
            public void run() {
                c.go();
            }
        };
        goThread.start();

        System.setIn(inContent); // send message and 'q' to quit.

        try {
            goThread.join(1000L);
        } catch (InterruptedException e) {
            fail("Quit signal did not stop the CommandLineClient.");
        }
        assertEquals(query, mockAgent.getRequestReceived().getText());
        assertEquals(expectedOutput, outContent.toString());
    }


    /**
     * Stores and returns the response received to support testing.
     *
     */
    protected class MockAgent extends AbstractAgent {
        protected AgentRequest request;

        @Override
        public AgentResponse process(AgentRequest request) {
            this.request = request;
            return new AgentResponse(request.getText());
        }

        public AgentRequest getRequestReceived() {
            return request;
        }
    }
}
