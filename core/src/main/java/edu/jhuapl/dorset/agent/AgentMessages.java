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
package edu.jhuapl.dorset.agent;

/**
 * Status codes and messages for Agents
 */
public class AgentMessages {
    public static final int SUCCESS = 0;
    // the agent did not respond
    public static final int NO_RESPONSE = 100;
    // the agent did not understand the request
    public static final int BAD_REQUEST = 101;
    // the agent's response was invalid
    public static final int INVALID_RESPONSE = 102;
    // the agent did not know the answer
    public static final int UNKNOWN_ANSWER = 103;
    // the agent needs more information
    public static final int MORE_INFORMATION_NEEDED = 104;
}
