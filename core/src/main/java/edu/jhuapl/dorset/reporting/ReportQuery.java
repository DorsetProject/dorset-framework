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

import java.util.Date;

/**
 * Query object for reports
 * <p>
 * Provides methods for setting filters on the stored reports. The filters supported are
 * <ul>
 * <li>Limit on the number of reports returned
 * <li>Agent names
 * <li>Date range
 * </ul>
 */
public class ReportQuery {
    private int limit;
    private String[] agentNames;
    private Date startDate;
    private Date endDate;

    public static final int NO_LIMIT = -1;
    private static final int DEFAULT_LIMIT = 50;

    public ReportQuery() {
        limit = DEFAULT_LIMIT;
    }

    /**
     * Get the maximum number of reports to return
     *
     * @return the limit
     */
    public int getLimit() {
        return limit;
    }

    /**
     * Set the maximum number of reports to return
     *
     * @param limit  The maximum number of reports to return
     */
    public void setLimit(int limit) {
        this.limit = limit;
    }

    /**
     * Turn off the limit
     */
    public void setLimitOff() {
        limit = NO_LIMIT;
    }

    /**
     * Get the names of the agents being filtered for
     *
     * @return the agentNames being filtered for
     */
    public String[] getAgentNames() {
        return agentNames;
    }

    /**
     * Set the names of the agents to filter for
     *
     * @param agentNames  The agents to filter for
     */
    public void setAgentNames(String[] agentNames) {
        this.agentNames = agentNames;
    }

    /**
     * Set the name of the agent to filter for
     *
     * @param agentName  The agent to filter for
     */
    public void setAgentName(String agentName) {
        this.agentNames = new String[1];
        this.agentNames[0] = agentName;
    }

    /**
     * Get the start of the date range
     *
     * @return the start of the date range filter
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * Set the start of the date range
     *
     * @param startDate  the beginning of the date range
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * Get the end of the date range
     *
     * @return the end of the date range filter
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * Set the end of the date range
     *
     * @param endDate  the end of the date range
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * Set the start and end of the date range
     *
     * @param startDate  the start of the date range filter
     * @param endDate  the end of the date range filter
     */
    public void setTimeRange(Date startDate, Date endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

}
