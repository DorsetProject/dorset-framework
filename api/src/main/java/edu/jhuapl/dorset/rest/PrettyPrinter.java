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

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.jaxrs.cfg.EndpointConfigBase;
import com.fasterxml.jackson.jaxrs.cfg.ObjectWriterInjector;
import com.fasterxml.jackson.jaxrs.cfg.ObjectWriterModifier;

/**
 * Pretty print the json response if pretty=1 as a query parameter
 */
@Provider
public class PrettyPrinter implements ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext requestContext,
                    ContainerResponseContext responseContext) throws IOException {
        UriInfo uriInfo = requestContext.getUriInfo();
        MultivaluedMap<String, String> params = uriInfo.getQueryParameters();
        if (params.containsKey("pretty") && !params.getFirst("pretty").equals("0")) {
            ObjectWriterInjector.set(new PrettyPrintModifier());
        }

    }

    public static class PrettyPrintModifier extends ObjectWriterModifier {

        @Override
        public ObjectWriter modify(EndpointConfigBase<?> endpoint, MultivaluedMap<String, Object> responseHeaders,
                        Object valueToWrite, ObjectWriter ow, JsonGenerator jg) throws IOException {
            jg.useDefaultPrettyPrinter();
            return ow;
        }
        
    }

}
