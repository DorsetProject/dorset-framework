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

import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import edu.jhuapl.dorset.Request;
import edu.jhuapl.dorset.agents.Agent;

public class SingleAgentRouterTest {
    @Test
    public void testRoute() {
        Agent agent = mock(Agent.class);
        Router router = new SingleAgentRouter(agent);

        Agent agents[] = router.route(new Request("hello world"));

        Agent expected[] = new Agent[]{agent};
        assertArrayEquals(expected, agents);
    }
}
