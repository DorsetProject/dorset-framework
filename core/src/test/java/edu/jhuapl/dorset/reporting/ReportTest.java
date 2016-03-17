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
package edu.jhuapl.dorset.reporting;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.jhuapl.dorset.Request;
import edu.jhuapl.dorset.Response;
import edu.jhuapl.dorset.ResponseStatus;
import edu.jhuapl.dorset.ResponseStatus.Code;

public class ReportTest {

    @Test
    public void testNullResponseText() {
        Response response = new Response(new ResponseStatus(Code.NO_RESPONSE_FROM_AGENT));
        Request request = new Request("hello world");
        Report report = new Report(request);

        report.setResponse(response);

        assertEquals("", report.getResponseText());
    }

}
