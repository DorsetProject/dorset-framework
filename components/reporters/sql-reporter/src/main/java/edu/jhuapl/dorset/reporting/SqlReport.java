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

/**
 * Wraps reports for storage in order to get a simple id that sorts well
 */
public class SqlReport extends Report {
    private long id;

    public SqlReport() {}

    /**
     * Copy constructor
     *
     * @param report  report object
     */
    public SqlReport(Report report) {
        super(report);
    }

    /**
     * Get the database id
     *
     * @return id
     */
    public long getId() {
        return id;
    }

    /**
     * Set the database id
     *
     * @param id  the id
     */
    public void setId(long id) {
        this.id = id;
    }
}
