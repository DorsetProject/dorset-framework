package edu.jhuapl.dorset.sessions;

import java.util.Date;

import edu.jhuapl.dorset.Request;
import edu.jhuapl.dorset.Response;
import edu.jhuapl.dorset.sessions.Session.SessionStatus;

public class SessionObject {
    
    public String requestId;
    public Date timestamp;
    public SessionStatus sessionStatus;
    public Request request; 
    public Response response;
    
    public String getRequestId() {
        return requestId;
    }
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
    public Date getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
    public SessionStatus getSessionStatus() {
        return sessionStatus;
    }
    public void setSessionStatus(SessionStatus sessionStatus) {
        this.sessionStatus = sessionStatus;
    }
    public Request getRequest() {
        return request;
    }
    public void setRequest(Request request) {
        this.request = request;
    }
    public Response getResponse() {
        return response;
    }
    public void setResponse(Response response) {
        this.response = response;
    }

}
