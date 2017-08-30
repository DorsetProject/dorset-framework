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
import java.util.UUID;

import com.google.gson.JsonArray;

import edu.jhuapl.dorset.sessions.Session;
import edu.jhuapl.dorset.sessions.SessionObject;
import edu.jhuapl.dorset.sessions.SessionService;


public class SimpleSessionService implements SessionService {
    
    private Map<String, Session> sessions; // key is the SessionID and value is a Session

    // default constructor
    public SimpleSessionService() {
        this.sessions = new HashMap<String, Session>();
      
    }
    
    @Override
    public String create() {
        Session session = new Session();
        String uniqueSessionId = UUID.randomUUID().toString(); // should i validate that it is unique? 
        
        Date timestamp = new Date();
        session.setTimestamp(timestamp);
        
        SessionObject[] sessionHistory = new SessionObject[0];
        session.setSessionHistory(sessionHistory);
        
        this.sessions.put(uniqueSessionId, session);
        
        return uniqueSessionId;
    }

    // thinking that this is for storage? 
    // @Override
    // public String retrieve(Properties properties) {
    // TODO Auto-generated method stub
    //    return null;
    //}

    @Override
    public void update(String sessionId, SessionObject sessionObject) {
        
        // get session by session id from the hash map 
        // get the session history from the session
        // update the session history with the new session object
        
        Session currentSession = this.sessions.get(sessionId);
        SessionObject[] sessionHistory = currentSession.getSessionHistory();
        sessionHistory[sessionHistory.length] = sessionObject;
        currentSession.setSessionHistory(sessionHistory);         
        this.sessions.put(sessionId, currentSession);
    }

    @Override
    public void delete(String sessionId) {
        // TODO Auto-generated method stub
        this.sessions.remove(sessionId);
    }

    @Override
    public Session getSession(String sessionId) {
        // TODO Auto-generated method stub
        return this.sessions.get(sessionId);
    }

    
}
