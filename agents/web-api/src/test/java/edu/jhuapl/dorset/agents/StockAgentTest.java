/**
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
package edu.jhuapl.dorset.agents;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import org.junit.Test;

import edu.jhuapl.dorset.agents.Agent;
import edu.jhuapl.dorset.agents.AgentRequest;
import edu.jhuapl.dorset.agents.AgentResponse;
import edu.jhuapl.dorset.http.HttpClient;
import edu.jhuapl.dorset.http.HttpRequest;
import edu.jhuapl.dorset.http.HttpResponse;

public class StockAgentTest {

    private String apikey = "default_apikey";
    
    protected String getJsonData(String filename) {
        ClassLoader classLoader = StockAgent.class.getClassLoader();
        URL url = classLoader.getResource(filename);
        try {
            Path path = Paths.get(url.toURI());
            try (Scanner scanner = new Scanner(new File(path.toString()))) {
                return scanner.useDelimiter("\\Z").next();
            }
        } catch (URISyntaxException | FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testStockAgentExactMatch() {
        String keyword = "facebook, Inc.";
        String jsonData = getJsonData("stockagent/MockJson_Facebook.json");
        HttpClient client = new FakeHttpClient(new FakeHttpResponse(jsonData));
        
        Agent stocks = new StockAgent(client, apikey);
        AgentRequest request = new AgentRequest("Stocks " + keyword);
        AgentResponse response = stocks.process(request);

        assertEquals("Here is the longitudinal stock market data from the last 30 days for Facebook, Inc.",
                response.getText());
    }

    @Test
    public void testStockAgentCloseMatch() {
        String keyword = "facebook";
        String jsonData = getJsonData("stockagent/MockJson_Facebook.json");
        HttpClient client = new FakeHttpClient(new FakeHttpResponse(jsonData));

        Agent stocks = new StockAgent(client, apikey);
        AgentRequest request = new AgentRequest(keyword);
        AgentResponse response = stocks.process(request);

        assertEquals("Here is the longitudinal stock market data from the last 30 days for Facebook, Inc.",
                response.getText());
    }

    @Test
    public void testStockAgentExactMatch2() {
        String keyword = "Apple inc.";
        String jsonData = getJsonData("stockagent/MockJson_Apple.json");
        HttpClient client = new FakeHttpClient(new FakeHttpResponse(jsonData));

        Agent stocks = new StockAgent(client, apikey);
        AgentRequest request = new AgentRequest(keyword);
        AgentResponse response = stocks.process(request);
        assertEquals("Here is the longitudinal stock market data from the last 30 days for Apple Inc.",
                response.getText());
    }

    @Test
    public void testStockAgentCloseMatch2() {
        String keyword = "apple";
        String jsonData = getJsonData("stockagent/MockJson_Apple.json");
        HttpClient client = new FakeHttpClient(new FakeHttpResponse(jsonData));

        Agent stocks = new StockAgent(client, apikey);
        AgentRequest request = new AgentRequest(keyword);
        AgentResponse response = stocks.process(request);

        assertEquals("Here is the longitudinal stock market data from the last 30 days for Apple Inc.",
                response.getText());
    }

    @Test
    public void testStockAgentNoQuandlData() {
        String keyword = "First Bank"; // maps to FRBA in NASDAQ
        String jsonData = getJsonData("stockagent/MockJson_404.json");
        HttpResponse httpResponse = mock(HttpResponse.class);
        when(httpResponse.isSuccess()).thenReturn(false);
        when(httpResponse.asString()).thenReturn(jsonData);
        HttpClient client = mock(HttpClient.class);
        when(client.execute(any(HttpRequest.class))).thenReturn(httpResponse);

        Agent stocks = new StockAgent(client, apikey);
        AgentRequest request = new AgentRequest(keyword);
        AgentResponse response = stocks.process(request);

        assertEquals("I am sorry, I can't find the proper stock data for the company "
                        + keyword + ".", response.getStatus().getMessage());
    }

    @Test
    public void testStockAgentFailure() {
        String keyword = "company that does not exist";
        HttpClient client = mock(HttpClient.class);

        Agent stocks = new StockAgent(client, apikey);
        AgentRequest request = new AgentRequest(keyword);
        AgentResponse response = stocks.process(request);

        assertEquals("I am sorry, I don't understand which company you are asking about.",
                response.getStatus().getMessage());
    }

}
