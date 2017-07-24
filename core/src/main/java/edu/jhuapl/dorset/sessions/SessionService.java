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

package edu.jhuapl.dorset.sessions;

import edu.jhuapl.dorset.Request;

/**
 * Services the session information for a Dorset Application.
 * <p>
 * The sessions support dialog for the Dorset Application and for Agents.
 */
public interface SessionService {

    public String create(Request request); // to create session pass in a request? agent request?

    //public String retrieve(Properties properties);
    
    public void update(String id, Session session); // what to pass in to update?
   
    public void delete(String id);
    
    public Session getSession(String id);
    
}
