package edu.jhuapl.dorset.agents;

import edu.jhuapl.dorset.agent.AgentBase;
import edu.jhuapl.dorset.agent.AgentMessages;
import edu.jhuapl.dorset.agent.AgentRequest;
import edu.jhuapl.dorset.agent.AgentResponse;
import edu.jhuapl.dorset.agent.Description;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.DateFormatSymbols;

/**
 * Agent for answering questions about the current date or time
 *
 */
public class DateTimeAgent extends AgentBase {
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
        this.setName("date-time");
        this.setDescription(new Description(getName(), SUMMARY, EXAMPLES));
    }

    @Override
    public AgentResponse process(AgentRequest request) {
        Date now = new Date();
        AgentResponse response = new AgentResponse();
        String requestType = getRequestType(request.getText());
        switch (requestType) {
            case DATE:
                response.setText(getDate(now));
                break;
            case DAY:
                response.setText(getDay(now));
                break;
            case TIME:
                response.setText(getTime(now));
                break;
            default:
                response.setStatusCode(AgentMessages.BAD_REQUEST);
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
        String weekdays[] = new DateFormatSymbols(Locale.ENGLISH).getWeekdays();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        return weekdays[dayOfWeek];
    }

    protected String getDate(Date date) {
        DateFormat format = new SimpleDateFormat("MMMMM dd, yyyy");
        return format.format(date);
    }
}
