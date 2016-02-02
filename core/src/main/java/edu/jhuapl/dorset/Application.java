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
import edu.jhuapl.dorset.record.NullRecorder;
import edu.jhuapl.dorset.record.Record;
import edu.jhuapl.dorset.record.Recorder;
import edu.jhuapl.dorset.routing.Router;

/**
 * Dorset Application
 *
 */
public class Application {
    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    protected AgentRegistry agentRegistry;
    protected Router router;
    protected Recorder recorder;

    private static Application app;

    public Application(AgentRegistry agentRegistry, Router router) {
        this(agentRegistry, router, new NullRecorder());
    }

    public Application(AgentRegistry agentRegistry, Router router, Recorder recorder) {
        this.agentRegistry = agentRegistry;
        this.router = router;
        this.recorder = recorder;
        router.initialize(agentRegistry);
    }

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

    public Response process(Request request) {
        logger.info("Processing request: " + request.getText());
        Response response = new Response("no response");
        Record record = new Record(request);

        long startTime = System.nanoTime();
        Agent[] agents = router.getAgents(request);
        record.setRouteTime(startTime, System.nanoTime());
        record.setAgents(agents);
        if (agents.length == 0) {
            return response;
        }

        startTime = System.nanoTime();
        for (Agent agent : agents) {
            AgentResponse agentResponse = agent.process(new AgentRequest(request.getText()));
            if (agentResponse != null) {
                // take first answer
                response.setText(agentResponse.text);
                record.setSelectedAgent(agent);
                record.setResponse(response);
                break;
            }
        }
        record.setAgentTime(startTime, System.nanoTime());

        return response;
    }

    public static void setApplication(Application app) {
        logger.info("Registering dorset application");
        Application.app = app;
    }

    public static Application getApplication() {
        return Application.app;
    }
}
