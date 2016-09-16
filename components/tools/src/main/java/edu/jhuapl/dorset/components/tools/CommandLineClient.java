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

import java.util.Scanner;

import edu.jhuapl.dorset.Application;
import edu.jhuapl.dorset.Request;
import edu.jhuapl.dorset.Response;
import edu.jhuapl.dorset.agents.Agent;
import edu.jhuapl.dorset.routing.Router;
import edu.jhuapl.dorset.routing.SingleAgentRouter;

/**
 * Simple, command line access to a single agent, router, or application. Whatever you enter at the
 * command line is sent, and the text response is provided to System.out.
 * 
 * The default character to exit the system is 'q'.
 * 
 * Example:
 * 
 * <pre>
 * Agent agent = new CalculatorAgent();
 * CommandLineClient client = new CommandLineClient(agent);
 * client.go();
 * </pre>
 * 
 */
public class CommandLineClient {

    private Application app;

    /**
     * Creates a command line client with a SingleAgentRouter for the agent passed in. All command
     * line requests will be sent to this agent, and the text response will be displayed to standard
     * out.
     * 
     * @param agent the agent to handle requests
     */
    public CommandLineClient(Agent agent) {
        this(new SingleAgentRouter(agent));
    }

    /**
     * Creates a command line client for the router passed in. All command line requests will be
     * sent to this router, and the text response will be displayed to standard out.
     * 
     * @param router the router to handle requests
     */
    public CommandLineClient(Router router) {
        this(new Application(router));
    }

    /**
     * Creates a command line client for the application passed in. All command line requests will
     * be sent to this application, and the text response will be displayed to standard out.
     * 
     * @param app the application to handle requests
     */
    public CommandLineClient(Application app) {
        this.app = app;
    }

    /**
     * Starts monitoring standard input for text to send to the application. This method is blocking
     * until the system receives the message to quit from the text in standard in, as defined by the
     * {@link #isQuitString(String)} method.
     * 
     */
    public void go() {
        String input = "";
        Scanner in = new Scanner(System.in);

        while (true) {
            System.out.print("> ");
            input = in.nextLine();

            if (isQuitString(input)) {
                break;
            }

            Request request = new Request(input);
            Response response = app.process(request);

            System.out.println(response.getText());
        }

        System.out.println("\nBye.");
        in.close();
    }

    /**
     * Override-able method for defining what input string will stop the system. The default is a
     * single character 'q'.
     * 
     * @param input the input from standard in
     * @return true if the input represent a quit message
     */
    protected boolean isQuitString(String input) {
        return "q".equals(input);
    }

}
