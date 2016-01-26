/**
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

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import edu.jhuapl.dorset.Application;
import edu.jhuapl.dorset.Request;
import edu.jhuapl.dorset.Response;

@Path("/")
public class WebService {
	@GET
	@Path("/process/{text}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response process(@PathParam("text") String text) {
		Request request = new Request(text);
		
		Application app = Application.getApplication();
		if (app == null) {
		    return new Response("Dorset application not set");
		}
		Response response = app.process(request);
		
		return response;
	}

	@GET
	@Path("/echo/{message}")
	@Produces(MediaType.TEXT_PLAIN)
	public String echo(@PathParam("message") String message) {
		return message;
	}
}
