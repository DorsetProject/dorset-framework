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
package edu.jhuapl.dorset.http;

public interface HttpResponse {
    public String asString();
    
    public byte[] asBytes();

    public int getStatusCode();

    public String getReasonPhrase();

    /**
     * Was there a client error (such as a 404)?
     *
     * @return boolean
     */
    public boolean isClientError();

    /**
     * Was there a server error?
     *
     * @return boolean
     */
    public boolean isServerError();

    /**
     * Was there any kind of error?
     *
     * @return boolean
     */
    public boolean isError();

    /**
     * Was the request a success?
     *
     * @return boolean
     */
    public boolean isSuccess();
}
