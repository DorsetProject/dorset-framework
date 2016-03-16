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
 * This filter removes a wake-up word from the Request text.
 * A wake-up word is commonly used for speech to text systems that are always listening.
 */
public class WakeupRequestFilter implements RequestFilter {
    private String wakeupWord;
    
    public WakeupRequestFilter(String wakeupWord) {
        this.wakeupWord = wakeupWord;
    }

    @Override
    public Request filter(Request request) {
        if (this.wakeupWord != null) {
            request.setText(request.getText().replace(this.wakeupWord, "").trim());
        }
        return request;
    }

}
