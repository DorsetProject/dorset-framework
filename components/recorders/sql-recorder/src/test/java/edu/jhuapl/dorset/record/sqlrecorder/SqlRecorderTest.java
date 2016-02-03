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
package edu.jhuapl.dorset.record.sqlrecorder;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.hibernate.cfg.Configuration;
import org.junit.Test;

import edu.jhuapl.dorset.Request;
import edu.jhuapl.dorset.agent.Agent;
import edu.jhuapl.dorset.record.Record;
import edu.jhuapl.dorset.record.Recorder;

public class SqlRecorderTest {

    @Test
    public void testWritingSQL() {
        Request req = new Request("What is today's date?");
        Agent agent = mock(Agent.class);
        when(agent.getName()).thenReturn("date");

        Record r = new Record(req);
        r.setSelectedAgent(agent);
        r.setRouteTime(30, 47);
        r.setAgentTime(78, 450000);
        r.setResponseText("yesterday");

        Configuration conf = new Configuration();
        conf.configure();

        Recorder recorder = new SqlRecorder(conf);
        recorder.store(r);
    }
}
