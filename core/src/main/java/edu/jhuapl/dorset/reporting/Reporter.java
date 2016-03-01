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
 * Stores the request to response cycle for a Dorset Application.
 * <p>
 * The reports support debugging, tuning, and metrics.
 */
public interface Reporter {

    /**
     * Store a report
     *
     * @param report  The report to store
     */
    public void store(Report report);

    /**
     * Retrieve reports
     *
     * @param query  Query object
     * @return Array of reports
     */
    public Report[] retrieve(ReportQuery query);
}
