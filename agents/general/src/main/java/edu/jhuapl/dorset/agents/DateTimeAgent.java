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
package edu.jhuapl.dorset.agents;

import edu.jhuapl.dorset.ResponseStatus;
import edu.jhuapl.dorset.agents.AbstractAgent;
import edu.jhuapl.dorset.agents.AgentRequest;
import edu.jhuapl.dorset.agents.AgentResponse;
import edu.jhuapl.dorset.agents.Description;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.DateFormatSymbols;

/**
 * Agent for answering questions about the current date or time
 *
 */
public class DateTimeAgent extends AbstractAgent {
    private final Logger logger = LoggerFactory.getLogger(DateTimeAgent.class);

    private static final String DATE = "date";
    private static final String DAY = "day";
    private static final String TIME = "time";
    private static final String UNKNOWN = "unknown";

    private static final String SUMMARY = "Provide information on the current date and time";
    private static final String[] EXAMPLES =
                    new String[] {"What day is it?", "What time is it?", "What is today's date?"};

    /**
     * Create a date-time agent
     */
    public DateTimeAgent() {
        this.setDescription(new Description("date-time", SUMMARY, EXAMPLES));
    }

    @Override
    public AgentResponse process(AgentRequest request) {
        logger.debug("Handling the request: " + request.getText());
        Date now = new Date();
        AgentResponse response;
        String requestType = getRequestType(request.getText());
        switch (requestType) {
            case DATE:
                response = new AgentResponse(getDate(now));
                break;
            case DAY:
                response = new AgentResponse(getDay(now));
                break;
            case TIME:
                response = new AgentResponse(getTime(now));
                break;
            default:
                response = new AgentResponse(ResponseStatus.Code.AGENT_DID_NOT_UNDERSTAND_REQUEST);
                break;
        }

        return response;
    }

    protected String getRequestType(String text) {
        text = text.toLowerCase();
        String type = UNKNOWN;
        if (text.contains("date")) {
            type = DATE;
        } else if (text.contains("day")) {
            type = DAY;
        } else if (text.contains("time")) {
            type = TIME;
        }
        return type;
    }

    protected String getTime(Date date) {
        return DateFormat.getTimeInstance(DateFormat.SHORT).format(date);
    }

    protected String getDay(Date date) {
        String[] weekdays = new DateFormatSymbols(Locale.ENGLISH).getWeekdays();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        return weekdays[dayOfWeek];
    }

    protected String getDate(Date date) {
        DateFormat format = new SimpleDateFormat("MMMMM dd, yyyy");
        return format.format(date);
    }
}
