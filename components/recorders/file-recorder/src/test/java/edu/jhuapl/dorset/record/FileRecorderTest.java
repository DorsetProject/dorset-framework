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
package edu.jhuapl.dorset.record;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import edu.jhuapl.dorset.Request;
import edu.jhuapl.dorset.agent.Agent;

public class FileRecorderTest {
    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    private String header = "timestamp,requestText,selectedAgentName,responseText,routeTime,agentTime";

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

        Recorder recorder = new FileRecorder(file.toString());

        String actual = Files.readFirstLine(file, Charsets.UTF_8);
        assertEquals(header, actual);
    }

    @Test
    public void testWritingCSV() throws IOException {
        File file = getTempFile();
        Request req = new Request("What is today's date?");
        Agent agent = mock(Agent.class);
        when(agent.getName()).thenReturn("date");
        Record r = new Record(req);
        r.setSelectedAgent(agent);
        r.setRouteTime(30, 47);
        r.setAgentTime(78, 450000);
        r.setResponseText("yesterday");

        Recorder recorder = new FileRecorder(file.toString());
        recorder.store(r);

        String expected = "What is today's date?,date,yesterday,17,449922";
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
        DateFormat df = new SimpleDateFormat(FileRecorder.ISO_8601);
        String line = df.format(new Date()) + ",Why?,all,because,87,123456789";
        out.println(line);
        out.close();
 
        Recorder fr = new FileRecorder(file.toString());
        Record[] records = fr.retrieve(new RecordQuery());
        assertEquals(1, records.length);
        assertEquals("Why?", records[0].getRequestText());
        assertEquals("all", records[0].getSelectedAgentName());
        assertEquals("because", records[0].getResponseText());
        assertEquals(87, records[0].getRouteTime());
        assertEquals(123456789, records[0].getAgentTime());
    }
}
