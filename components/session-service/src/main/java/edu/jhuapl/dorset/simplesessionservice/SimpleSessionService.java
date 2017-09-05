/*
 * Copyright 2017 The Johns Hopkins University Applied Physics Laboratory LLC
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
 
import java.util.HashMap;
import java.util.Map;

import edu.jhuapl.dorset.sessions.Session;
import edu.jhuapl.dorset.sessions.SessionObject;
import edu.jhuapl.dorset.sessions.SessionService;


public class SimpleSessionService implements SessionService {
    
    private Map<String, Session> sessions;

    public SimpleSessionService() {
        this.sessions = new HashMap<String, Session>();
    }
    
    @Override
    public String create() {
        Session session = new Session();
        return session.getId();
    }

    @Override
    public void update(String sessionId, SessionObject sessionObject) {
        Session currentSession = this.sessions.get(sessionId);
        SessionObject[] sessionHistory = currentSession.getSessionHistory();
        sessionHistory[sessionHistory.length] = sessionObject;
        currentSession.setSessionHistory(sessionHistory);         
        this.sessions.put(sessionId, currentSession);
    }

    @Override
    public void delete(String sessionId) {
        this.sessions.remove(sessionId);
    }

    @Override
    public Session getSession(String sessionId) {
        return this.sessions.get(sessionId);
    }
}
