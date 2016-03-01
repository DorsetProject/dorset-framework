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

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.supercsv.cellprocessor.FmtDate;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ParseDate;
import org.supercsv.cellprocessor.ParseLong;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import edu.jhuapl.dorset.reporting.Report;
import edu.jhuapl.dorset.reporting.ReportQuery;
import edu.jhuapl.dorset.reporting.Reporter;

/**
 * File Reporter
 * <p>
 * Stores reports of request handling to a csv file.
 * <p>
 * This is not intended for significant production use.
 */
public class FileReporter implements Reporter {
    private static final Logger logger = LoggerFactory.getLogger(FileReporter.class);
    private static final CsvPreference FORMAT = CsvPreference.EXCEL_PREFERENCE;
    private static final String[] FIELDS = {"timestamp", "requestId", "requestText",
            "selectedAgentName", "responseText", "routeTime", "agentTime"};
    public static final String ISO_8601 = "yyyy-MM-dd'T'HH:mm:ssZ";
    // timestamp, request id, request text, and route time are the required fields
    private static final CellProcessor[] WRITE_PROCESSORS = new CellProcessor[] {
            new FmtDate(ISO_8601), new NotNull(), new NotNull(), new Optional(), new Optional(),
            new NotNull(), new Optional()};
    private static final CellProcessor[] READ_PROCESSORS = new CellProcessor[] {
            new ParseDate(ISO_8601), new NotNull(), new NotNull(), new Optional(), new Optional(),
            new ParseLong(), new Optional(new ParseLong())};

    private final String filename;
    private ICsvBeanWriter csvWriter = null;
    private FileWriter fw = null;
    private Object writeLock = new Object();

    /**
     * Create a file reporter
     *
     * @param filename  the filename to write to
     */
    public FileReporter(String filename) {
        this.filename = filename;
        if (Files.exists(Paths.get(filename))) {
            try {
                fw = new FileWriter(filename, true);
                csvWriter = new CsvBeanWriter(fw, FORMAT);
            } catch (IOException e) {
                logger.error("Unable to open " + filename, e);
            }
        } else {
            try {
                fw = new FileWriter(filename);
                csvWriter = new CsvBeanWriter(fw, FORMAT);
                csvWriter.writeHeader(FIELDS);
                csvWriter.flush();
            } catch (IOException e) {
                logger.error("Unable to create " + filename, e);
            }
        }
    }

    @Override
    public void store(Report report) {
        if (csvWriter == null) {
            logger.warn("Unable to store report because the csv writer is not initialized.");
            return;
        }

        try {
            synchronized (writeLock) {
                csvWriter.write(report, FIELDS, WRITE_PROCESSORS);
                csvWriter.flush();
            }
        } catch (IOException e) {
            logger.warn("Unable to store report because of internal csv writer error.", e);
            return;
        }
    }

    @Override
    public Report[] retrieve(ReportQuery query) {
        ICsvBeanReader csvReader = null;
        try {
            csvReader = new CsvBeanReader(new FileReader(filename), FORMAT);
        } catch (FileNotFoundException e) {
            logger.warn("Could not find " + filename, e);
            return new Report[0];
        }

        List<Report> reports = new ArrayList<Report>();
        try {
            DateRangeChecker dateChecker = new DateRangeChecker(query);
            AgentChecker agentChecker = new AgentChecker(query);

            int limit = query.getLimit();
            if (limit == ReportQuery.NO_LIMIT) {
                limit = Integer.MAX_VALUE;
            }
            final String[] header = csvReader.getHeader(true);
            while (reports.size() < limit) {
                Report report = csvReader.read(Report.class, header, READ_PROCESSORS);
                if (report == null) {
                    break;
                }
                if (!dateChecker.isInDateRange(report)) {
                    continue;
                }
                if (!agentChecker.isAgent(report)) {
                    continue;
                }
                reports.add(report);
            }
        } catch (IOException e) {
            logger.warn("Could not parse reports from " + filename, e);
        }
        
        try {
            csvReader.close();
        } catch (IOException e) {
            logger.warn("Unable to close csv file " + filename, e);
        }

        return reports.toArray(new Report[reports.size()]);
    }

    class DateRangeChecker {
        private Date start;
        private Date end;
        public DateRangeChecker(ReportQuery query) {
            this.start = query.getStartDate();
            this.end = query.getEndDate();
        }

        public boolean isInDateRange(Report report) {
            if (start != null) {
                if (start.after(report.getTimestamp())) {
                    return false;
                }
            }
            if (end != null) {
                if (end.before(report.getTimestamp())) {
                    return false;
                }
            }
            return true;
        }
    }

    class AgentChecker {
        private Set<String> names;
        
        public AgentChecker(ReportQuery query) {
            String[] agentNames = query.getAgentNames();
            if (agentNames != null) {
                names = new HashSet<String>();
                for (String name : agentNames) {
                    names.add(name);
                }
            }
        }

        public boolean isAgent(Report report) {
            if (names != null) {
                if (report.getSelectedAgentName() == null) {
                    return false;
                }
                if (!names.contains(report.getSelectedAgentName())) {
                    return false;
                }
            }
            return true;
        }
    }
}
