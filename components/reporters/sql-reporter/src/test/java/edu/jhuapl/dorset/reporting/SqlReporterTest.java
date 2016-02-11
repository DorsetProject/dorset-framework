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

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

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

    private Configuration getConf() {
        Configuration conf = new Configuration();
        conf.setProperty("hibernate.connection.driver_class", "org.h2.Driver");
        conf.setProperty("hibernate.connection.url", "jdbc:h2:mem:sql_reporter");
        conf.setProperty("hibernate.connection.pool_size", "1");
        conf.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        conf.setProperty("hibernate.hbm2ddl.auto", "create");
        return conf;
    }

    @Test
    public void testWritingSQL() throws ClassNotFoundException, SQLException {
        Request req = new Request("What is today's date?");
        Agent agent = mock(Agent.class);
        when(agent.getName()).thenReturn("date");

        Report r = new Report(req);
        Date d = new Date();
        r.setTimestamp(d);
        r.setSelectedAgent(agent);
        r.setRouteTime(30, 47);
        r.setAgentTime(78, 450000);
        r.setResponseText("yesterday");

        Reporter reporter = new SqlReporter(getConf());
        reporter.store(r);

        // talk directly to h2 and test if the data was stored correctly
        Class.forName("org.h2.Driver");
        Connection conn = DriverManager.getConnection("jdbc:h2:mem:sql_reporter");
        String selectQuery = "SELECT * FROM report";
        PreparedStatement stmt = conn.prepareStatement(selectQuery);
        ResultSet rs = stmt.executeQuery();
        rs.next();
        assertEquals(d, rs.getTimestamp("timestamp"));
        assertEquals("date", rs.getString("selectedAgentName"));
        assertEquals("What is today's date?", rs.getString("requestText"));
        assertEquals("yesterday", rs.getString("responseText"));
        assertEquals(17, rs.getLong("routeTime"));
        assertEquals(449922, rs.getLong("agentTime"));
        assertFalse(rs.next());
        stmt.close();
        conn.close();
    }
}
