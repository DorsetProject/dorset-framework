package edu.jhuapl.dorset.agents.duckduckgo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import edu.jhuapl.dorset.Request;

public class DuckDuckGoSmartForm {
   
    public String sessionId;
    public String requestId;
    public Date timestamp;
    public String requestText;
    public List<JsonObject> relatedTopics;
    public String abstractText;       
    public int numRelatedTopicsThreshold = 5; 
   
    public DuckDuckGoSmartForm () {
        
    }
    
    public DuckDuckGoSmartForm(Request request, String data) {
        this.requestId = request.getId();
        this.sessionId = request.getSession().getId();
        this.requestText = request.getText();
        
        Gson gson = new Gson();
        JsonObject jsonObj = gson.fromJson(data, JsonObject.class);
        this.abstractText = jsonObj.get("AbstractText").getAsString();
        JsonArray relatedTopicsArr = jsonObj.get("RelatedTopics").getAsJsonArray();
        
        this.relatedTopics = new ArrayList<JsonObject>();
        
        for (int index = 0; index < relatedTopicsArr.size(); index++) {
            if ( index < this.numRelatedTopicsThreshold ) {
                String relatedTopicURL = relatedTopicsArr.get(index).getAsJsonObject().
                                get("FirstURL").getAsString();
                String relatedTopicText = relatedTopicsArr.get(index).getAsJsonObject().
                                get("Text").getAsString();
                
                String[] tokenizedURL = relatedTopicURL.split("/");
                String relatedText = tokenizedURL[3].replaceAll("_","");

                // parse relatedTopicURL
                // https://duckduckgo.com/Donald_Trump,
                
                JsonObject relatedTopicJsonObj = new JsonObject();
                relatedTopicJsonObj.addProperty("relatedTopicURL", relatedTopicURL);
                relatedTopicJsonObj.addProperty("relatedTopicText", relatedTopicText);
                relatedTopicJsonObj.addProperty("relatedText", relatedText);
                
                this.relatedTopics.add(relatedTopicJsonObj);
                
            }
        }
        
    }
    
    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
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
    
    public int getNumRelatedTopicsThreshold() {
        return numRelatedTopicsThreshold;
    }

    public void setNumRelatedTopicsThreshold(int numRelatedTopicsThreshold) {
        this.numRelatedTopicsThreshold = numRelatedTopicsThreshold;
    }

}
