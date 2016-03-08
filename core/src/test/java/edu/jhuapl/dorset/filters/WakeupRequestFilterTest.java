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

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import edu.jhuapl.dorset.Request;

public class WakeupRequestFilterTest {

    @Test
    public void wakeupFilterSimple() {
        String strRequest = "Dorset 2 + 2";
        String strFilteredRequest = " 2 + 2";

        RequestFilter requestFilter = new WakeupRequestFilter("Dorset");

        Request request = requestFilter.filterRequest(new Request(strRequest));
        assertEquals(strFilteredRequest, request.getText());

    }

    @Test
    public void wakeupFilterNull() {
        String strRequest = "Dorset 2 + 2";

        RequestFilter requestFilter = new WakeupRequestFilter(null);

        Request request = requestFilter.filterRequest(new Request(strRequest));
        assertEquals(strRequest, request.getText());

    }

}
