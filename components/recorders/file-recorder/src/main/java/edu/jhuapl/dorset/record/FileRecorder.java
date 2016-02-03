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

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.supercsv.cellprocessor.FmtDate;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

/**
 * File Recorder
 *
 * Stores records of request handling to a csv file.
 */
public class FileRecorder implements Recorder {
    private static final Logger logger = LoggerFactory.getLogger(FileRecorder.class);
    private static final String[] FIELDS = {"timestamp", "requestText", "selectedAgentName", 
            "responseText", "routeTime", "agentTime"};
    private static final String ISO_8601 = "yyyy-MM-dd'T'HH:mmZ";
    // timestamp, request text, and route time are the required fields
    private static final CellProcessor[] PROCESSORS = new CellProcessor[] {
            new FmtDate(ISO_8601), new NotNull(), new Optional(), new Optional(),
            new NotNull(), new Optional()};
    
    private ICsvBeanWriter csvWriter = null;
    private FileWriter fw = null;

    /**
     * Create a file recorder
     * @param filename Filename to write to
     */
    public FileRecorder(String filename) {
        if (Files.exists(Paths.get(filename))) {
            try {
                fw = new FileWriter(filename, true);
                csvWriter = new CsvBeanWriter(fw, CsvPreference.EXCEL_PREFERENCE);
            } catch (IOException e) {
                logger.error("Unable to open " + filename, e);
            }
        } else {
            try {
                fw = new FileWriter(filename);
                csvWriter = new CsvBeanWriter(fw, CsvPreference.EXCEL_PREFERENCE);
                csvWriter.writeHeader(FIELDS);
                csvWriter.flush();
            } catch (IOException e) {
                logger.error("Unable to create " + filename, e);
            }
        }
    }

    @Override
    public void store(Record record) {
        if (csvWriter == null) {
            logger.warn("Unable to store record because the csv writer is not initialized.");
            return;
        }

        try {
            csvWriter.write(record, FIELDS, PROCESSORS);
        } catch (IOException e) {
            logger.warn("Unable to store record because of internal csv writer error.", e);
            return;
        }

        try {
            csvWriter.flush();
        } catch (IOException e) {
            logger.info("Unable to flush the file for recording requests", e);
        }
    }

    @Override
    public Record[] retrieve(RecordQuery query) {
        throw new UnsupportedOperationException();
    }
}
