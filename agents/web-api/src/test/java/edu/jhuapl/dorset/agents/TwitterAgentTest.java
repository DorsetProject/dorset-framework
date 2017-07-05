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
package edu.jhuapl.dorset.agents;

import static org.junit.Assert.*;

import org.junit.Test;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import edu.jhuapl.dorset.Application;
import edu.jhuapl.dorset.Request;
import edu.jhuapl.dorset.Response;
import edu.jhuapl.dorset.routing.Router;
import edu.jhuapl.dorset.routing.SingleAgentRouter;

public class TwitterAgentTest {

    @Test
    public void testValidTweet() {
        Config config = ConfigFactory.load();
        Agent agent = new TwitterAgent(config);
        Router router = new SingleAgentRouter(agent);
        Application app = new Application(router);

        Request request = new Request("This is a valid tweet");
        Response response = app.process(request);

        assertTrue(response.getText().equals("Tweet successful"));
    }

    @Test
    public void testIncorrectLength() {
        Config config = ConfigFactory.load();
        Agent agent = new TwitterAgent(config);
        Router router = new SingleAgentRouter(agent);
        Application app = new Application(router);

        Request request = new Request("This is an invalid tweet. This tweet is going to be too long,"
                        + " which means that this text needs to be longer than one hundred and"
                        + " forty characters...");
        Response response = app.process(request);
        assertTrue(response.getText().equals("Too many characters"));
    }

    @Test
    public void testDuplicateTweet() {
        Config config = ConfigFactory.load();
        Agent agent = new TwitterAgent(config);
        Router router = new SingleAgentRouter(agent);
        Application app = new Application(router);
        
        Request request = new Request("First Tweet");
        Response response = app.process(request);
        assertTrue(response.getText().equals("Could not post Tweet. Check your connection."
                        + "This tweet may have been a duplicate."));
    }
}
