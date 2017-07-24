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

package edu.jhuapl.dorset.simplesessionservice;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import edu.jhuapl.dorset.Request;
import edu.jhuapl.dorset.sessions.Session;
import edu.jhuapl.dorset.sessions.SessionService;


public class SimpleSessionService implements SessionService {
    
    private Map<String, Session> sessions; // key is the SessionID and value is a Session

    // default constructor
    public SimpleSessionService(){
        this.sessions = new HashMap<String, Session>();
        
    }
    
    @Override
    public String create(Request request) {
        
        String uniqueSessionID = UUID.randomUUID().toString();
        Date timestamp = new Date();
        Session session = new Session();
        session.setTimestamp(timestamp);
        
        JsonArray sessionHistory = new JsonArray();
        JsonObject odsfkjasdf = new JsonObject();
        odsfkjasdf.addProperty("request", "temp"); // maybe JsonObject is the wrong data type to use
        
        sessionHistory.add(odsfkjasdf);
        session.setSessionHistory(sessionHistory);
        
        this.sessions.put(uniqueSessionID, session);
        return uniqueSessionID;
    }

    //@Override
    //public String retrieve(Properties properties) {
        // TODO Auto-generated method stub
    //    return null;
    //}

    @Override
    public void update(String id, Session session) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void delete(String id) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Session getSession(String id) {
        // TODO Auto-generated method stub
        return null;
    }

    
}
