package edu.jhuapl.dorset.agents.duckduckgo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import edu.jhuapl.dorset.agents.AgentRequest;

public class SmartFormService {

    public int historyThreshold = 20;
    public int numRelatedTopicsThreshold = 5;
    public List<DuckDuckGoSmartForm> smartFormHistory;

    /**
     * 
     * SmartFormService
     * 
     */
    public SmartFormService() {
        this.smartFormHistory = new ArrayList<DuckDuckGoSmartForm>();
    }

    /**
     * Get history threshold
     *
     * @return historyThreshold
     *
     */
    public int getHistoryThreshold() {
        return historyThreshold;
    }

    /**
     * Set history threshold
     *
     * @param historyThreshold  
     *
     */
    public void setHistoryThreshold(int historyThreshold) {
        this.historyThreshold = historyThreshold;
    }
    
    /**
     * Get number of related topics threshold
     *
     * @return numRelatedTopicsThreshold
     *
     */
    public int getNumRelatedTopicsThreshold() {
        return numRelatedTopicsThreshold;
    }

    /**
     * Set number of related topics threshold
     *
     * @param numRelatedTopicsThreshold  
     *
     */
    public void setNumRelatedTopicsThreshold(int numRelatedTopicsThreshold) {
        this.numRelatedTopicsThreshold = numRelatedTopicsThreshold;
    }
    
    // test
    /**
     * Update history
     *
     * @param request  the request
     * @param data  data returned from DuckDuckGo given request
     *
     */
    public void updateHistory(AgentRequest request, String data) {
        List<JsonObject> relatedTopics = formatDdgData(data, this.numRelatedTopicsThreshold);
        
        DuckDuckGoSmartForm ddgSmartForm = new DuckDuckGoSmartForm(request, relatedTopics);

        if (this.smartFormHistory.size() < this.historyThreshold) {
            this.smartFormHistory.add(ddgSmartForm);
        } else if (this.smartFormHistory.size() >= this.historyThreshold) {
            this.smartFormHistory.remove(this.smartFormHistory.size() - 1);
            this.smartFormHistory.add(ddgSmartForm);
        }

    }

    // rename
    /**
     * Query smart form history 
     *
     * @param sessionId  the session id
     * @param entityText  text of interest
     *
     */    
    public String querySmartFormHistory(String sessionId, String entityText) { // rename?
        String smartResponse;

        ArrayList<ArrayList<Double>> distances = new ArrayList<ArrayList<Double>>();
        double distance;
        double maxDistance = 0.0;
        Map<Integer, Integer> maxDistanceIndex = new HashMap<Integer, Integer>();

        // populate distance matrix
        for (int i = 0; i < this.smartFormHistory.size(); i++) {
            ArrayList<Double> innerDistances = new ArrayList<Double>();

            if (this.smartFormHistory.get(i).getSessionId().equals(sessionId)) {

                List<JsonObject> relatedTopics = this.smartFormHistory
                                .get(this.smartFormHistory.size() - 1).getRelatedTopics();

                // iterate over all related topics and find closest match
                for (int j = 0; j < relatedTopics.size(); j++) {
                    String relatedTopic = (relatedTopics.get(j).get("relatedTopic").getAsString());
                    distance = diceCoefficient(entityText.toLowerCase(),
                                    relatedTopic.toLowerCase());

                    if (distance > maxDistance) {
                        maxDistance = distance;
                        maxDistanceIndex.clear();
                        maxDistanceIndex.put(i, j);
                    }

                    innerDistances.add(distance);
                }
            }

            distances.add(innerDistances);

        }

        if (maxDistance == 0.0) {
            smartResponse = null;
        } else {
            int index = maxDistanceIndex.keySet().iterator().next();
            List<JsonObject> relatedTopics = this.smartFormHistory.get(index).getRelatedTopics();
            smartResponse = (relatedTopics.get(maxDistanceIndex.get(index)).get("relatedTopicText").getAsString());

        }

        return smartResponse;

    }

    /**
     * Calculate dice coefficient
     * cite: https://en.wikibooks.org/wiki/Algorithm_Implementation/Strings/Dice%27s_coefficient
     * 
     * @param s1  string one
     * @param s2  string two
     * @return diceCoefficient dice coefficient for s1 and s2
     *
     */
    public static double diceCoefficient(String s1, String s2) {
        Set<String> nx = new HashSet<String>();
        Set<String> ny = new HashSet<String>();

        for (int i = 0; i < s1.length() - 1; i++) {
            char x1 = s1.charAt(i);
            char x2 = s1.charAt(i + 1);
            String tmp = "" + x1 + x2;
            nx.add(tmp);
        }
        for (int j = 0; j < s2.length() - 1; j++) {
            char y1 = s2.charAt(j);
            char y2 = s2.charAt(j + 1);
            String tmp = "" + y1 + y2;
            ny.add(tmp);
        }

        Set<String> intersection = new HashSet<String>(nx);
        intersection.retainAll(ny);
        double totcombigrams = intersection.size();

        return (2 * totcombigrams) / (nx.size() + ny.size());
    }

    /**
     * Get current exchange potential entities
     *
     * @return potentialEntities  
     *
     */
    public List<String> getCurrentExchangePotentialEntities(String data) {
        List<String> potentialEntities = new ArrayList<String>();

        List<JsonObject> relatedTopics = formatDdgData(data, this.numRelatedTopicsThreshold);
        for (int index = 0; index < relatedTopics.size(); index++) {
            potentialEntities.add(relatedTopics.get(index).get("relatedTopic").getAsString());
        }
        return potentialEntities;
    }
    
    /**
     * Format duckduckgo data response 
     *
     * @param data  data returned from DuckDuckGo given request
     * @param numRelatedTopicsThreshold  threshold for number of related topics
     * @return relatedTopic list of objects containing related topic information
     *
     */
    public List<JsonObject> formatDdgData(String data, int numRelatedTopicsThreshold) {
        List<JsonObject> relatedTopics = new ArrayList<JsonObject>();
        
        Gson gson = new Gson();

        JsonObject jsonObj = gson.fromJson(data, JsonObject.class);

        JsonArray relatedTopicsArr = jsonObj.get("RelatedTopics").getAsJsonArray();

        for (int index = 0; index < relatedTopicsArr.size(); index++) {
            if (index < numRelatedTopicsThreshold) {

                if (relatedTopicsArr.get(index).getAsJsonObject().get("Result") != null) {

                    String relatedTopicUrl = relatedTopicsArr.get(index).getAsJsonObject()
                                    .get("FirstURL").getAsString();
                    String relatedTopicText = relatedTopicsArr.get(index).getAsJsonObject()
                                    .get("Text").getAsString();

                    // parse relatedTopicUrl 'https://duckduckgo.com/Donald_Trump'
                    String[] tokenizedUrl = relatedTopicUrl.split("/");
                    String relatedTopic = tokenizedUrl[3].replaceAll("_", " ");
                    relatedTopic = relatedTopic.replaceAll("%2C", ",");

                    JsonObject relatedTopicJsonObj = new JsonObject();
                    relatedTopicJsonObj.addProperty("relatedTopic", relatedTopic);
                    relatedTopicJsonObj.addProperty("relatedTopicText", relatedTopicText);
                    relatedTopicJsonObj.addProperty("relatedTopicURL", relatedTopicUrl);

                    relatedTopics.add(relatedTopicJsonObj);

                }
            }
        }
        
        return relatedTopics;
    }
}
