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
    public void testPostGood() {
        Config config = ConfigFactory.load();
        Agent agent = new TwitterAgent(config);
        Router router = new SingleAgentRouter(agent);
        Application app = new Application(router);

        Request request = new Request("POST This is a valid tweet");
        Response response = app.process(request);
        System.out.println(response.getText());

        assertTrue(response.getText().equals("Tweet successful"));
    }

    @Test
    public void testPostLengthBad() {
        Config config = ConfigFactory.load();
        Agent agent = new TwitterAgent(config);
        Router router = new SingleAgentRouter(agent);
        Application app = new Application(router);

        Request request = new Request("POST This is an invalid tweet. This tweet is going to be too long,"
                        + " which means that this text needs to be longer than one hundred and"
                        + " forty characters...");
        Response response = app.process(request);
        System.out.println(response.getStatus().getMessage());

        assertTrue(response.getStatus().getMessage().contains("too many characters"));
    }

    @Test
    public void testPostDuplicateBad() {
        Config config = ConfigFactory.load();
        Agent agent = new TwitterAgent(config);
        Router router = new SingleAgentRouter(agent);
        Application app = new Application(router);
        
        Request request = new Request("POST First Tweet");
        Response response = app.process(request);
        System.out.println(response.getStatus().getMessage());

        request = new Request("POST First Tweet");
        response = app.process(request);
        
        assertTrue(response.getStatus().getMessage().contains("duplicate"));
    }
    
    @Test
    public void testGet2HomeGood() {
        Config config = ConfigFactory.load();
        Agent agent = new TwitterAgent(config);
        Router router = new SingleAgentRouter(agent);
        Application app = new Application(router);
        
        Request request = new Request("GET 2 HOME");
        Response response = app.process(request);
        System.out.println(response.getText());

        assertTrue(response.getText().contains("Showing 2 tweets"));
        assertTrue(response.getText().contains("home timeline"));
        }
    
    @Test
    public void testGet2MINEGood() {
        Config config = ConfigFactory.load();
        Agent agent = new TwitterAgent(config);
        Router router = new SingleAgentRouter(agent);
        Application app = new Application(router);
        
        Request request = new Request("GET 2 MINE");
        Response response = app.process(request);
        System.out.println(response.getText());

        assertTrue(response.getText().contains("Showing 2 tweets"));
        assertTrue(response.getText().contains("my timeline"));
    }

    @Test
    public void testGet1FavoritesGood() {
        Config config = ConfigFactory.load();
        Agent agent = new TwitterAgent(config);
        Router router = new SingleAgentRouter(agent);
        Application app = new Application(router);
        
        Request request = new Request("GET 1 FAVORITES");
        Response response = app.process(request);
        System.out.println(response.getText());

        assertTrue(response.getText().contains("Showing 1 tweet"));
        assertTrue(response.getText().contains("favorites"));
    }
    
    @Test
    public void testGet1SearchGood() {
        Config config = ConfigFactory.load();
        Agent agent = new TwitterAgent(config);
        Router router = new SingleAgentRouter(agent);
        Application app = new Application(router);
        
        Request request = new Request("GET 1 puppies");
        Response response = app.process(request);
        System.out.println(response.getText());

        assertTrue(response.getText().contains("Showing 1 tweet"));
        assertTrue(response.getText().contains("search for"));
    }
    
    @Test
    public void testGet2Bad() {
        Config config = ConfigFactory.load();
        Agent agent = new TwitterAgent(config);
        Router router = new SingleAgentRouter(agent);
        Application app = new Application(router);
        
        Request request = new Request("GET 2");
        Response response = app.process(request);
        System.out.println(response.getStatus().getMessage());

        assertTrue(response.getStatus().getMessage().contains("No tweets could be found in"));
    }
    
    @Test
    public void testGetBad() {
        Config config = ConfigFactory.load();
        Agent agent = new TwitterAgent(config);
        Router router = new SingleAgentRouter(agent);
        Application app = new Application(router);
        
        Request request = new Request("GET");
        Response response = app.process(request);
        System.out.println(response.getStatus().getMessage());

        assertTrue(response.getStatus().getMessage().contains("No tweets could be found in"));
    }
    
    @Test
    public void testGetHomeGood() {
        Config config = ConfigFactory.load();
        Agent agent = new TwitterAgent(config);
        Router router = new SingleAgentRouter(agent);
        Application app = new Application(router);
        
        Request request = new Request("GET HOME");
        Response response = app.process(request);
        System.out.println(response.getText());

        assertTrue(response.getText().contains("Showing 1 tweet"));
        assertTrue(response.getText().contains("home timeline"));
    }
    
    @Test
    public void testGetMINEGood() {
        Config config = ConfigFactory.load();
        Agent agent = new TwitterAgent(config);
        Router router = new SingleAgentRouter(agent);
        Application app = new Application(router);
        
        Request request = new Request("GET MINE");
        Response response = app.process(request);
        System.out.println(response.getText());

        assertTrue(response.getText().contains("Showing 1 tweet"));
        assertTrue(response.getText().contains("my timeline"));
    }
    
    @Test
    public void testGetFavoritesGood() {
        Config config = ConfigFactory.load();
        Agent agent = new TwitterAgent(config);
        Router router = new SingleAgentRouter(agent);
        Application app = new Application(router);
        
        Request request = new Request("GET FAVORITES");
        Response response = app.process(request);
        System.out.println(response.getText());

        assertTrue(response.getText().contains("Showing 1 tweet"));
        assertTrue(response.getText().contains("favorites"));
    }
    
    @Test
    public void testGetSearchGood() {
        Config config = ConfigFactory.load();
        Agent agent = new TwitterAgent(config);
        Router router = new SingleAgentRouter(agent);
        Application app = new Application(router);
        
        Request request = new Request("GET puppies");
        Response response = app.process(request);
        System.out.println(response.getText());

        assertTrue(response.getText().contains("Showing 1 tweet"));
        assertTrue(response.getText().contains("search for"));
    }
    
    @Test
    public void testBad() {
        Config config = ConfigFactory.load();
        Agent agent = new TwitterAgent(config);
        Router router = new SingleAgentRouter(agent);
        Application app = new Application(router);
        
        Request request = new Request("this will not go through");
        Response response = app.process(request);
        System.out.println(response.getStatus().getMessage());
        
        assertTrue(response.getStatus().getMessage().contains("Your request could not be understood."));
    } 
    
    @Test
    public void testInvalidConfig() {
        //TODO create invald config and pass it into Agent constructor
        Config config = ConfigFactory.load();
        Agent agent = new TwitterAgent(config);
        Router router = new SingleAgentRouter(agent);
        Application app = new Application(router);
        
        Request request = new Request("POST credntials are wrong");
        Response response = app.process(request);
        System.out.println(response.getStatus().getMessage());
        
        assertTrue(response.getStatus().getMessage().contains("Could not process your request due to an invalid configuration"));
    }
}
