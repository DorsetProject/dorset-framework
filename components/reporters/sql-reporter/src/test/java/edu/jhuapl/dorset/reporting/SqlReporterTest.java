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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.jhuapl.dorset.Request;
import edu.jhuapl.dorset.Response;
import edu.jhuapl.dorset.ResponseStatus;
import edu.jhuapl.dorset.agents.Agent;
import edu.jhuapl.dorset.reporting.Report;
import edu.jhuapl.dorset.reporting.Reporter;
import edu.jhuapl.dorset.reporting.SqlReporter;

public class SqlReporterTest {
    private SessionFactory sessionFactory;

    @BeforeClass
    public static void setUpBeforeClass() {
        // force slf4j logging because jboss logger won't pick it up when we 
        // use slf4j simple logger
        System.setProperty("org.jboss.logging.provider", "slf4j");
    }

    @Before
    public void setup() {
        Configuration conf = new Configuration();
        conf.setProperty("hibernate.connection.driver_class", "org.h2.Driver");
        conf.setProperty("hibernate.connection.url", "jdbc:h2:mem:sql_reporter");
        conf.setProperty("hibernate.connection.pool_size", "1");
        conf.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        conf.setProperty("hibernate.hbm2ddl.auto", "create-drop");
        conf.addResource("report.hbm.xml");
        sessionFactory = conf.buildSessionFactory();
    }

    @After
    public void cleanup() {
        sessionFactory.close();
    }

    private void loadData(SqlReporter reporter) throws ParseException {
        String[] data1 = new String[] {"2016-02-02T15:43:34UTC", "all"};
        String[] data2 = new String[] {"2016-02-03T15:43:34UTC", "time"};
        String[] data3 = new String[] {"2016-02-04T15:43:34UTC", "date"};
        String[] data4 = new String[] {"2016-02-05T15:43:34UTC", "all"};
        String[] data5 = new String[] {"2016-02-06T15:43:34UTC", "all"};
        String[][] data = new String[][] {data1, data2, data3, data4, data5};

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        Agent agent = mock(Agent.class);

        for (String[] dateAndAgent : data) {
            Request req = new Request("What is 1 + 1?");
            Report r = new Report(req);
            r.setRouteTime(123);
            r.setAgentTime(9876);
            r.setResponse(new Response("2"));
            when(agent.getName()).thenReturn(dateAndAgent[1]);
            r.setSelectedAgent(agent);
            r.setTimestamp(df.parse(dateAndAgent[0]));
            reporter.store(r);
        }
    }

    @Test
    public void testStore() throws ClassNotFoundException, SQLException {
        Request req = new Request("What is today's date?", "abcdef");
        Agent agent = mock(Agent.class);
        when(agent.getName()).thenReturn("date");

        Report r = new Report(req);
        Date d = new Date();
        r.setTimestamp(d);
        r.setSelectedAgent(agent);
        r.setRouteTime(30, 47);
        r.setAgentTime(78, 450000);
        r.setResponse(new Response("yesterday"));

        Reporter reporter = new SqlReporter(sessionFactory);
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
        assertEquals("abcdef", rs.getString("requestId"));
        assertEquals("What is today's date?", rs.getString("requestText"));
        assertEquals("yesterday", rs.getString("responseText"));
        assertEquals(ResponseStatus.Code.SUCCESS.getValue(), rs.getInt("responseCode"));
        assertEquals(17, rs.getLong("routeTime"));
        assertEquals(449922, rs.getLong("agentTime"));
        assertFalse(rs.next());
        stmt.close();
        conn.close();
    }

    @Test
    public void testSimpleRetrieve() {
        Request req = new Request("What is tomorrow's date?");
        Agent agent = mock(Agent.class);
        when(agent.getName()).thenReturn("date");

        Report r = new Report(req);
        Date d = new Date();
        r.setTimestamp(d);
        r.setSelectedAgent(agent);
        r.setRouteTime(123);
        r.setAgentTime(987654321);
        r.setResponse(new Response("tuesday"));
        Reporter reporter = new SqlReporter(sessionFactory);
        reporter.store(r);

        Report[] reports = reporter.retrieve(new ReportQuery());
        assertEquals(1, reports.length);
        assertEquals(req.getText(), reports[0].getRequestText());
        assertEquals(r.getResponseText(), reports[0].getResponseText());
        assertEquals(r.getTimestamp(), reports[0].getTimestamp());
        assertEquals(r.getAgentTime(), reports[0].getAgentTime());
        assertEquals(r.getRouteTime(), reports[0].getRouteTime());
        assertEquals(r.getSelectedAgentName(), reports[0].getSelectedAgentName());
    }

    @Test
    public void testDateRangeRetrieve() throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        SqlReporter reporter = new SqlReporter(sessionFactory);
        loadData(reporter);

        ReportQuery rq = new ReportQuery();
        rq.setStartDate(df.parse("2016-02-04T12:00:00UTC"));
        rq.setEndDate(df.parse("2016-02-06T12:00:00UTC"));
        Report[] reports = reporter.retrieve(rq);

        assertEquals(2, reports.length);
        assertEquals(df.parse("2016-02-05T15:43:34UTC"), reports[1].getTimestamp());
    }

    @Test
    public void testLimitRetrieve() throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        SqlReporter reporter = new SqlReporter(sessionFactory);
        loadData(reporter);

        ReportQuery rq = new ReportQuery();
        rq.setLimit(3);
        Report[] reports = reporter.retrieve(rq);

        assertEquals(3, reports.length);
        assertEquals(df.parse("2016-02-02T15:43:34UTC"), reports[0].getTimestamp());
        assertEquals(df.parse("2016-02-03T15:43:34UTC"), reports[1].getTimestamp());
        assertEquals(df.parse("2016-02-04T15:43:34UTC"), reports[2].getTimestamp());
    }

    @Test
    public void testAgentFilterRetrieve() throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        SqlReporter reporter = new SqlReporter(sessionFactory);
        loadData(reporter);

        ReportQuery rq = new ReportQuery();
        rq.setAgentName("all");
        Report[] reports = reporter.retrieve(rq);

        assertEquals(3, reports.length);
        assertEquals(df.parse("2016-02-02T15:43:34UTC"), reports[0].getTimestamp());
        assertEquals(df.parse("2016-02-05T15:43:34UTC"), reports[1].getTimestamp());
        assertEquals(df.parse("2016-02-06T15:43:34UTC"), reports[2].getTimestamp());
    }

    @Test
    public void testAgentArrayFilterRetrieve() throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        SqlReporter reporter = new SqlReporter(sessionFactory);
        loadData(reporter);

        ReportQuery rq = new ReportQuery();
        rq.setAgentNames(new String[]{"time", "date"});
        Report[] reports = reporter.retrieve(rq);

        assertEquals(2, reports.length);
        assertEquals(df.parse("2016-02-03T15:43:34UTC"), reports[0].getTimestamp());
        assertEquals(df.parse("2016-02-04T15:43:34UTC"), reports[1].getTimestamp());
    }
}
