package edu.jhuapl.dorset.agents.duckduckgo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import edu.jhuapl.dorset.agents.AgentRequest;

public class DuckDuckGoSmartForm {

    public String sessionId;
    public Date timestamp;
    public String requestText;
    public List<JsonObject> relatedTopics;
    public String abstractText;

    /**
     * 
     * DuckDuckGoSmartForm
     * 
     * 
     */
    public DuckDuckGoSmartForm() {

    }

    /**
     * 
     * DuckDuckGoSmartForm
     * 
     * @param request  request
     * @param data  data returned from DuckDuckGo given request
     *
     */
    public DuckDuckGoSmartForm(AgentRequest request, List<JsonObject> relatedTopics) {
        try {
            this.sessionId = request.getSession().getId();
        } catch (NullPointerException e) {
            this.sessionId = null;
        }
        this.requestText = request.getText();
        this.relatedTopics = relatedTopics;

    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Date getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getRequestText() {
        return requestText;
    }

    public void setRequestText(String requestText) {
        this.requestText = requestText;
    }

    public List<JsonObject> getRelatedTopics() {
        return relatedTopics;
    }

    public void setRelatedTopics(List<JsonObject> relatedTopics) {
        this.relatedTopics = relatedTopics;
    }

    public String getAbstractText() {
        return abstractText;
    }

    public void setAbstractText(String abstractText) {
        this.abstractText = abstractText;
    }

}
