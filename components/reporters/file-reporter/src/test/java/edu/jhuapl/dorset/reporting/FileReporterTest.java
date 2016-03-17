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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import edu.jhuapl.dorset.Request;
import edu.jhuapl.dorset.Response;
import edu.jhuapl.dorset.agents.Agent;
import edu.jhuapl.dorset.reporting.FileReporter;
import edu.jhuapl.dorset.reporting.Report;
import edu.jhuapl.dorset.reporting.ReportQuery;
import edu.jhuapl.dorset.reporting.Reporter;

public class FileReporterTest {
    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    private String header = "timestamp,requestId,requestText,agentName,responseText,responseCode,routeTime,agentTime";

    private int fileCounter = 1;
    private File getTempFile() {
        File tmpFolder = testFolder.getRoot();
        String filename = String.format("csv_writer_test%d.csv", fileCounter);
        File file = new File(tmpFolder.getAbsolutePath(), filename);
        fileCounter++;
        return file;
    }

    @Test
    public void testCreatingCSV() throws IOException {
        File file = getTempFile();

        @SuppressWarnings("unused")
        Reporter reporter = new FileReporter(file.toString());

        String actual = Files.readFirstLine(file, Charsets.UTF_8);
        assertEquals(header, actual);
    }

    @Test
    public void testWritingCSV() throws IOException {
        File file = getTempFile();
        Request req = new Request("What is today's date?", "request-1");
        Agent agent = mock(Agent.class);
        when(agent.getName()).thenReturn("date");
        Report r = new Report(req);
        r.setAgent(agent);
        r.setRouteTime(30, 47);
        r.setAgentTime(78, 450000);
        r.setResponse(new Response("yesterday"));

        Reporter reporter = new FileReporter(file.toString());
        reporter.store(r);

        String expected = "request-1,What is today's date?,date,yesterday,0,17,449922";
        List<String> lines = Files.readLines(file, Charsets.UTF_8);
        String actual = lines.get(1);
        // skip timestamp to make check easier
        actual = actual.substring(actual.indexOf(',') + 1);
        assertEquals(expected, actual);
    }

    @Test
    public void testSimpleReadingCSV() throws FileNotFoundException {
        File file = getTempFile();
        PrintWriter out = new PrintWriter(file.toString());
        out.println(header);
        DateFormat df = new SimpleDateFormat(FileReporter.ISO_8601);
        String line = df.format(new Date()) + ",abcdef,Why?,all,because,0,87,123456789";
        out.println(line);
        out.close();

        Reporter fr = new FileReporter(file.toString());
        Report[] reports = fr.retrieve(new ReportQuery());

        assertEquals(1, reports.length);
        assertEquals("abcdef", reports[0].getRequestId());
        assertEquals("Why?", reports[0].getRequestText());
        assertEquals("all", reports[0].getAgentName());
        assertEquals("because", reports[0].getResponseText());
        assertEquals(87, reports[0].getRouteTime());
        assertEquals(123456789, reports[0].getAgentTime());
    }

    @Test
    public void testDateRangeRetrieve() throws FileNotFoundException, ParseException {
        File file = getTempFile();
        PrintWriter out = new PrintWriter(file.toString());
        out.println(header);
        out.println("2016-02-02T15:43:34UTC,req-1,Why?,all,because,0,87,123456789");
        out.println("2016-02-03T15:43:34UTC,req-2,Why?,all,because,0,87,123456789");
        out.println("2016-02-04T15:43:34UTC,req-3,Why?,all,because,0,87,123456789");
        out.println("2016-02-05T15:43:34UTC,req-4,Why?,all,because,0,87,123456789");
        out.println("2016-02-06T15:43:34UTC,req-5,Why?,all,because,0,87,123456789");
        out.close();
        DateFormat df = new SimpleDateFormat(FileReporter.ISO_8601);

        Reporter fr = new FileReporter(file.toString());
        ReportQuery rq = new ReportQuery();
        rq.setStartDate(df.parse("2016-02-04T12:00:00UTC"));
        rq.setEndDate(df.parse("2016-02-06T12:00:00UTC"));
        Report[] reports = fr.retrieve(rq);

        assertEquals(2, reports.length);
        assertEquals(df.parse("2016-02-05T15:43:34UTC"), reports[1].getTimestamp());
    }

    @Test
    public void testLimitRetrieve() throws FileNotFoundException, ParseException {
        File file = getTempFile();
        PrintWriter out = new PrintWriter(file.toString());
        out.println(header);
        out.println("2016-02-02T15:43:34UTC,req-1,Why?,all,because,0,87,123456789");
        out.println("2016-02-03T15:43:34UTC,req-2,Why?,all,because,0,87,123456789");
        out.println("2016-02-04T15:43:34UTC,req-3,Why?,all,because,0,87,123456789");
        out.println("2016-02-05T15:43:34UTC,req-4,Why?,all,because,0,87,123456789");
        out.println("2016-02-06T15:43:34UTC,req-5,Why?,all,because,0,87,123456789");
        out.close();
        DateFormat df = new SimpleDateFormat(FileReporter.ISO_8601);

        Reporter fr = new FileReporter(file.toString());
        ReportQuery rq = new ReportQuery();
        rq.setLimit(3);
        Report[] reports = fr.retrieve(rq);

        assertEquals(3, reports.length);
        assertEquals(df.parse("2016-02-02T15:43:34UTC"), reports[0].getTimestamp());
        assertEquals(df.parse("2016-02-03T15:43:34UTC"), reports[1].getTimestamp());
        assertEquals(df.parse("2016-02-04T15:43:34UTC"), reports[2].getTimestamp());
    }

    @Test
    public void testAgentFilterRetrieve() throws FileNotFoundException, ParseException {
        File file = getTempFile();
        PrintWriter out = new PrintWriter(file.toString());
        out.println(header);
        out.println("2016-02-02T15:43:34UTC,req-1,Why?,all,because,0,87,123456789");
        out.println("2016-02-03T15:43:34UTC,req-2,Why?,time,because,0,87,123456789");
        out.println("2016-02-04T15:43:34UTC,req-3,Why?,date,because,0,87,123456789");
        out.println("2016-02-05T15:43:34UTC,req-4,Why?,all,because,0,87,123456789");
        out.println("2016-02-06T15:43:34UTC,req-5,Why?,all,because,0,87,123456789");
        out.close();
        DateFormat df = new SimpleDateFormat(FileReporter.ISO_8601);

        Reporter fr = new FileReporter(file.toString());
        ReportQuery rq = new ReportQuery();
        rq.setAgentName("all");
        Report[] reports = fr.retrieve(rq);

        assertEquals(3, reports.length);
        assertEquals(df.parse("2016-02-02T15:43:34UTC"), reports[0].getTimestamp());
        assertEquals(df.parse("2016-02-05T15:43:34UTC"), reports[1].getTimestamp());
        assertEquals(df.parse("2016-02-06T15:43:34UTC"), reports[2].getTimestamp());
    }

    @Test
    public void testAgentArrayFilterRetrieve() throws FileNotFoundException, ParseException {
        File file = getTempFile();
        PrintWriter out = new PrintWriter(file.toString());
        out.println(header);
        out.println("2016-02-02T15:43:34UTC,req-1,Why?,all,because,0,87,123456789");
        out.println("2016-02-03T15:43:34UTC,req-2,Why?,time,because,0,87,123456789");
        out.println("2016-02-04T15:43:34UTC,req-3,Why?,date,because,0,87,123456789");
        out.println("2016-02-05T15:43:34UTC,req-4,Why?,all,because,0,87,123456789");
        out.println("2016-02-06T15:43:34UTC,req-5,Why?,all,because,0,87,123456789");
        out.close();
        DateFormat df = new SimpleDateFormat(FileReporter.ISO_8601);

        Reporter fr = new FileReporter(file.toString());
        ReportQuery rq = new ReportQuery();
        rq.setAgentNames(new String[]{"time", "date"});
        Report[] reports = fr.retrieve(rq);

        assertEquals(2, reports.length);
        assertEquals(df.parse("2016-02-03T15:43:34UTC"), reports[0].getTimestamp());
        assertEquals(df.parse("2016-02-04T15:43:34UTC"), reports[1].getTimestamp());
    }

}
