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
package edu.jhuapl.dorset.reporting;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.hibernate.cfg.Configuration;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.jhuapl.dorset.Request;
import edu.jhuapl.dorset.agent.Agent;
import edu.jhuapl.dorset.reporting.Report;
import edu.jhuapl.dorset.reporting.Reporter;
import edu.jhuapl.dorset.reporting.SqlReporter;

public class SqlReporterTest {

    @BeforeClass
    public static void setUpBeforeClass() {
        // force slf4j logging because jboss logger won't pick it up when we 
        // use slf4j simple logger
        System.setProperty("org.jboss.logging.provider", "slf4j");
    }

    @Test
    public void testWritingSQL() {
        Request req = new Request("What is today's date?");
        Agent agent = mock(Agent.class);
        when(agent.getName()).thenReturn("date");

        Report r = new Report(req);
        r.setSelectedAgent(agent);
        r.setRouteTime(30, 47);
        r.setAgentTime(78, 450000);
        r.setResponseText("yesterday");

        Configuration conf = new Configuration();
        conf.configure();

        Reporter reporter = new SqlReporter(conf);
        reporter.store(r);
    }
}
