package edu.jhuapl.dorset.agents.duckduckgo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.Gson;

import edu.jhuapl.dorset.Request;

public class SmartFormService {
    
    public int historyThreshold = 20;
    public Map<String, DuckDuckGoSmartForm> smartFormHistory; //  key is the req ID
    
    /**
     * 
     * SmartFormService
     * 
     */
    public SmartFormService(){
        this.smartFormHistory = new LinkedHashMap<String, DuckDuckGoSmartForm>();
    }
    
    // get history limit
    public int getHistoryThreshold() {
        return historyThreshold;
    }

    // set history limit
    public void setHistoryThreshold(int historyThreshold) {
        this.historyThreshold = historyThreshold;
    }

    // add new smartForm
    public void updateHistory(Request request, String data){ // rename obviously
        DuckDuckGoSmartForm ddgSmartForm = new DuckDuckGoSmartForm(request, data);
        
        // check on size of form
        if (this.smartFormHistory.size() < this.historyThreshold ) {
            this.smartFormHistory.put(request.getId(), ddgSmartForm);
        } else if (this.smartFormHistory.size() >= this.historyThreshold ) { 
            this.smartFormHistory.remove(this.smartFormHistory.keySet().iterator().next());
            this.smartFormHistory.put(request.getId(), ddgSmartForm);
        }
        
        
        // if less than thresh 
            // add
        // if more than thresh
            // remove oldest 
            // add 
    }
    
    // query terms to check if follow-up question after disambiguation
    // also query on Session ID
    // fuzzy matcher - dice coefficient - token level
    public List<DuckDuckGoSmartForm> querySmartFormHistory(String queryTerm){
        List<DuckDuckGoSmartForm> ddgSmartFormList = new ArrayList<DuckDuckGoSmartForm>();
        
        // search the map
        Iterator<Entry<String, DuckDuckGoSmartForm>> iterator = this.smartFormHistory.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, DuckDuckGoSmartForm> pair = iterator.next();
            System.out.println(pair.getKey() + " = " + pair.getValue());
            
            // check the fields of interest
            
            // if detected 
                // append the form to the list
            
            // else 
                // pass
            
            iterator.remove(); // avoids a ConcurrentModificationException
        }
        
        
        return ddgSmartFormList;
        
    }
    
    
    
}
