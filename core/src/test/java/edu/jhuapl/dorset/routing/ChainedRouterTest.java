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
package edu.jhuapl.dorset.routing;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;

import edu.jhuapl.dorset.Request;
import edu.jhuapl.dorset.agent.Agent;

public class ChainedRouterTest {

    @Test
    public void testRouteWithFirstEmptyResponse() {
        Router r1 = mock(Router.class);
        Router r2 = mock(Router.class);
        Agent a = mock(Agent.class);
        Agent[] resp = {a};
        Request request = mock(Request.class);
        when(r1.route(request)).thenReturn(new Agent[0]);
        when(r2.route(request)).thenReturn(resp);
        Router router = new ChainedRouter(r1, r2);

        assertArrayEquals(resp, router.route(request));
    }

    @Test
    public void testRouteWithNoEmptyResponse() {
        Router r1 = mock(Router.class);
        Router r2 = mock(Router.class);
        Agent a1 = mock(Agent.class);
        Agent a2 = mock(Agent.class);
        Agent[] resp1 = {a1};
        Agent[] resp2 = {a2};
        Request request = mock(Request.class);
        when(r1.route(request)).thenReturn(resp1);
        when(r2.route(request)).thenReturn(resp2);
        Router router = new ChainedRouter(r1, r2);

        assertArrayEquals(resp1, router.route(request));
    }

}
