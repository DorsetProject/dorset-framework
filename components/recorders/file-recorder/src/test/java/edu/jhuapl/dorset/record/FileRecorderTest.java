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
import java.io.IOException;
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
    
    @Test
    public void testCreatingCSV() throws IOException {
        File tmpFolder = testFolder.getRoot();
        File file = new File(tmpFolder.getAbsolutePath(), "csv_writer_test.csv");

        Recorder recorder = new FileRecorder(file.toString());

        String expected = "requestText,selectedAgentName";
        String actual = Files.readFirstLine(file, Charsets.UTF_8);
        assertEquals(expected, actual);
    }

    @Test
    public void testWritingCSV() throws IOException {
        File tmpFolder = testFolder.getRoot();
        File file = new File(tmpFolder.getAbsolutePath(), "csv_writer_test2.csv");
        Request req = new Request("What is today's date?");
        Agent agent = mock(Agent.class);
        when(agent.getName()).thenReturn("date");
        Record r = new Record(req);
        r.setSelectedAgent(agent);

        Recorder recorder = new FileRecorder(file.toString());
        recorder.store(r);

        String expected = "What is today's date?,date";
        List<String> lines = Files.readLines(file, Charsets.UTF_8);
        assertEquals(expected, lines.get(1));
    }

}
