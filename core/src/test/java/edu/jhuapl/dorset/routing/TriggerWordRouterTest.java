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
import static org.mockito.Mockito.mock;

import org.junit.Test;

import edu.jhuapl.dorset.Request;
import edu.jhuapl.dorset.agent.Agent;
import edu.jhuapl.dorset.config.MultiValuedMap;

public class TriggerWordRouterTest {

    @Test
    public void testRouting() {
        Agent agent = mock(Agent.class);
        MultiValuedMap params = new MultiValuedMap();
        params.addString(TriggerWordRouter.TRIGGERS, "twitter");
        RouterAgentConfig config = RouterAgentConfig.create().add(agent, params);
        Router router = new TriggerWordRouter(config);

        Agent agents[] = router.route(new Request("twitter hello world"));

        Agent expected[] = new Agent[]{agent};
        assertArrayEquals(expected, agents);
    }

    @Test
    public void testRoutingWithNoAgentMatch() {
        Agent agent = mock(Agent.class);
        MultiValuedMap params = new MultiValuedMap();
        params.addString(TriggerWordRouter.TRIGGERS, "twitter");
        RouterAgentConfig config = RouterAgentConfig.create().add(agent, params);
        Router router = new TriggerWordRouter(config);

        Agent agents[] = router.route(new Request("film hello world"));

        Agent expected[] = new Agent[0];
        assertArrayEquals(expected, agents);
    }

    @Test
    public void testRoutingWithEmptyRequest() {
        Router router = new TriggerWordRouter(RouterAgentConfig.create());

        Agent agents[] = router.route(new Request(""));

        Agent expected[] = new Agent[0];
        assertArrayEquals(expected, agents);
    }

    @Test
    public void testMissingTrigger() {
        Agent agent1 = mock(Agent.class);
        MultiValuedMap params1 = new MultiValuedMap();
        params1.addString(TriggerWordRouter.TRIGGERS, "twitter");
        Agent agent2 = mock(Agent.class);
        MultiValuedMap params2 = new MultiValuedMap();
        RouterAgentConfig config = RouterAgentConfig.create().add(agent1, params1).add(agent2, params2);

        Router router = new TriggerWordRouter(config);

        assertEquals(1, router.getAgents().length);
    }

}
