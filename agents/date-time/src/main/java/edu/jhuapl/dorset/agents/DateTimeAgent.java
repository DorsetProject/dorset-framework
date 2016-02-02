package edu.jhuapl.dorset.agents;

import edu.jhuapl.dorset.agent.AgentBase;
import edu.jhuapl.dorset.agent.AgentRequest;
import edu.jhuapl.dorset.agent.AgentResponse;
import edu.jhuapl.dorset.nlp.BasicTokenizer;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.DateFormatSymbols;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class DateTimeAgent extends AgentBase  {
	private Date date; 
	
	public DateTimeAgent (){
		this(new Date()); 
	}
	
	public DateTimeAgent (Date date){
		super();
		this.date = date;
	}
	

    public AgentResponse process(AgentRequest request) {
    	String functionName = getFunctionName(request.text);  	
    	Method method;
    	String outputResponse = null; 
    	
    	try{
    		method = this.getClass().getMethod(functionName);
    		outputResponse = (String) method.invoke(this);
    	}
    	catch (SecurityException e){
    		
    	}
    	catch (NoSuchMethodException e){
    		
    	} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        return new AgentResponse(outputResponse);
    }
    
    protected String getFunctionName(String requestText){
    	String question = requestText.toLowerCase(); 	
    	String functionName; 
    	
    	if(question.indexOf("date")!=-1)
    		functionName = "getDate";
    	else if (question.indexOf("day")!=-1)
    		functionName = "getDay";
    	else if (question.indexOf("time")!=-1)
    		functionName = "getTime";
    	else
    		functionName = "errorFunc";
    	
    	return(functionName);
    }

    public String getTime(){
    	return (DateFormat.getTimeInstance(DateFormat.SHORT).format(this.date));
    }
    
    public String getDay(){
    	String weekdays[] = new DateFormatSymbols(Locale.ENGLISH).getWeekdays();
    	Calendar c = Calendar.getInstance();
    	c.setTime(this.date);
    	int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
    	return (weekdays[dayOfWeek]);
    }    

    public String getDate(){
    	DateFormat format = new SimpleDateFormat("MMMMM dd, yyyy");
        return(format.format(this.date));
    }
    
    public String errorFunc(){
    	return("Request must contain words time, date or day.");
    }
}
