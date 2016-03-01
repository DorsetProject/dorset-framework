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

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.jhuapl.dorset.Application;
import edu.jhuapl.dorset.Request;
import edu.jhuapl.dorset.Response;
import edu.jhuapl.dorset.agent.Agent;
import edu.jhuapl.dorset.agent.Description;

@Path("/")
public class WebService {
    private static final Logger logger = LoggerFactory.getLogger(WebService.class);

    @Inject
    private Application app;

    /**
     * Process a request
     *
     * @param req  the WebRequest payload
     * @return response
     */
    @POST
    @Path("/request")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public WebResponse process(WebRequest req) {
        Request request = new Request(req.getText());

        if (app == null) {
            return new WebResponse("Dorset application not set");
        }
        Response response = app.process(request);

        return new WebResponse(response);
    }

    /**
     * Get the agents that are available
     *
     * @return array of agent descriptions
     */
    @GET
    @Path("/agents")
    @Produces(MediaType.APPLICATION_JSON)
    public Description[] getAgents() {
        if (app == null) {
            return new Description[0];
        }

        Agent[] agents = app.getAgents();
        Description[] descriptions = new Description[agents.length];
        for (int i = 0; i < agents.length; i++) {
            descriptions[i] = agents[i].getDescription();
            if (descriptions[i] == null) {
                logger.warn("Null description object for agent " + agents[i].getName());
            }
        }

        return descriptions;
    }

    /**
     * Use this to test if the web services are alive
     *
     * @return the json encoded string "pong"
     */
    @GET
    @Path("/ping")
    @Produces(MediaType.APPLICATION_JSON)
    public String ping() {
        return "\"pong\"";
    }
}
