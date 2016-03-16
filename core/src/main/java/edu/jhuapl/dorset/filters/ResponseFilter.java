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

import edu.jhuapl.dorset.Response;

/**
 * Dorset Response Filter
 *
 * A ResponseFilter alters the Response object before it is returned by the application.
 * A ResponseFilter example is changing error message text to create a consistent personality.
 */
public interface ResponseFilter {

    /**
     * Filter the response object
     *
     * @param response  current response object
     * @return filtered response object
     */
    public Response filter(Response repsonse);

}
