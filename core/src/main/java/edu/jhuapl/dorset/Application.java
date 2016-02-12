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
package edu.jhuapl.dorset;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.jhuapl.dorset.agent.Agent;
import edu.jhuapl.dorset.agent.AgentRegistry;
import edu.jhuapl.dorset.agent.AgentRequest;
import edu.jhuapl.dorset.agent.AgentResponse;
import edu.jhuapl.dorset.agent.RegistryEntry;
import edu.jhuapl.dorset.reporting.NullReporter;
import edu.jhuapl.dorset.reporting.Report;
import edu.jhuapl.dorset.reporting.Reporter;
import edu.jhuapl.dorset.routing.Router;

/**
 * Dorset Application
 *
 */
public class Application {
    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    protected AgentRegistry agentRegistry;
    protected Router router;
    protected Reporter reporter;

    private static Application app;

    /**
     * Create a Dorset application
     *
     * Uses a null reporter that ignores new reports.
     *
     * @param agentRegistry registry of the agents available to the app
     * @param router a router that finds the appropriate agent for a request
     */
    public Application(AgentRegistry agentRegistry, Router router) {
        this(agentRegistry, router, new NullReporter());
    }

    /**
     * Create a Dorset application
     * @param agentRegistry registry of the agents available to the app
     * @param router a router that finds the appropriate agent for a request
     * @param reporter a reporter which logs request handling
     */
    public Application(AgentRegistry agentRegistry, Router router, Reporter reporter) {
        this.agentRegistry = agentRegistry;
        this.router = router;
        this.reporter = reporter;
        router.initialize(agentRegistry);
    }

    /**
     * Get the active agents in the registry
     * @return array of Agent objects
     */
    public Agent[] getAgents() {
        Collection<RegistryEntry> entries = agentRegistry.asMap().values();
        Agent[] agents = new Agent[entries.size()];
        int index = 0;
        for (RegistryEntry entry : entries) {
            agents[index] = entry.getAgent();
            index++;
        }
        return agents;
    }

    /**
     * Process a request
     * @param request Request object
     * @return Response object
     */
    public Response process(Request request) {
        logger.info("Processing request: " + request.getText());
        Response response = new Response("no response");
        Report report = new Report(request);

        long startTime = System.nanoTime();
        Agent[] agents = router.getAgents(request);
        report.setRouteTime(startTime, System.nanoTime());
        report.setAgents(agents);
        if (agents.length == 0) {
            return response;
        }

        startTime = System.nanoTime();
        for (Agent agent : agents) {
            AgentResponse agentResponse = agent.process(new AgentRequest(request.getText()));
            if (agentResponse != null) {
                // take first answer
                response.setText(agentResponse.getText());
                report.setSelectedAgent(agent);
                report.setResponse(response);
                break;
            }
        }
        report.setAgentTime(startTime, System.nanoTime());
        reporter.store(report);

        return response;
    }

    /**
     * Set the shared application object
     * 
     * This is useful for web-based use cases.
     * 
     * @param app Application object
     */
    public static void setApplication(Application app) {
        logger.info("Registering dorset application");
        Application.app = app;
    }

    /**
     * Get the shared application object
     * @return Application object
     */
    public static Application getApplication() {
        return Application.app;
    }
}
