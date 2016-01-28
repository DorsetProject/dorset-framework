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
package edu.jhuapl.dorset.agent;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;

public class AgentRegistryTest {

    @Test
    public void testRegisterNameCase() {
        AgentRegistry registry = new AgentRegistry();
        Agent agent = mock(Agent.class);

        registry.register("test", agent, null);

        assertEquals(agent, registry.getAgent("Test"));
        assertNull(registry.getAgent("unknown"));
    }

    @Test
    public void testRegisterOverwriteAgent() {
        AgentRegistry registry = new AgentRegistry();
        Agent agent1 = mock(Agent.class);
        Agent agent2 = mock(Agent.class);

        registry.register("test", agent1, null);
        registry.register("test", agent2, null);

        assertEquals(agent2, registry.getAgent("test"));
    }

    @Test
    public void testNonexistantAgent() {
        AgentRegistry registry = new AgentRegistry();

        assertNull(registry.getAgent("unknown"));
    }

}
