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

package edu.jhuapl.dorset.filters;

import edu.jhuapl.dorset.Request;

/**
 * Wakeup Request Filter
 *
 * The Wake-up Request Filter allows you to alter the 
 * Request object before it goes through an Agent to be 
 * processed. It removes the wake-up word from the Request 
 * text before passing the Request to an Agent.
 * 
 */

public class WakeupRequestFilter implements RequestFilter {
    protected String wakeupWord;
    protected String requestText;
    
    public WakeupRequestFilter(String wakeupWord) {
        this.wakeupWord = wakeupWord;

    }

    @Override
    public String filterRequest(Request request) {
        // TODO Auto-generated method stub
        if (this.wakeupWord != null) {
            this.requestText = request.getText().replace(this.wakeupWord, "").trim();
        } else {
            this.requestText = request.getText();
        }
        return requestText;
    }

}
