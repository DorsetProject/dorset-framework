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

public class StockAgentTest {

    private String apikey = "default_apikey";

    @Test
    public void testStockAgentExactMatch() {
        String keyword = "facebook, Inc.";
        String keywordSymbol = "FB";

        ClassLoader classLoader = StockAgent.class.getClassLoader();
        URL url = classLoader.getResource("stockagent/MockJson_Facebook");

        Path path = null;
        try {
            path = Paths.get(url.toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        String jsonData = "";
        try {
            jsonData = new Scanner(new File(path.toString())).useDelimiter("\\Z").next();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String urlStr = "https://www.quandl.com/api/v3/datasets/WIKI/"
                + keywordSymbol + ".json?api_key=" + apikey;

        HttpClient client = mock(HttpClient.class);
        when(client.get(urlStr)).thenReturn(jsonData);

        Agent stocks = new StockAgent(client, apikey);
        AgentRequest request = new AgentRequest("Stocks " + keyword);
        AgentResponse response = stocks.process(request);

        assertEquals("Here is the longitudinal stock market data from the last 30 days for Facebook, Inc.",
                response.getText());

    }

    @Test
    public void testStockAgentCloseMatch() {
        String keyword = "facebook";
        String keywordSymbol = "FB";

        ClassLoader classLoader = StockAgent.class.getClassLoader();
        URL url = classLoader.getResource("stockagent/MockJson_Facebook");

        Path path = null;
        try {
            path = Paths.get(url.toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        String jsonData = "";
        try {
            jsonData = new Scanner(new File(path.toString())).useDelimiter("\\Z").next();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String urlStr = "https://www.quandl.com/api/v3/datasets/WIKI/"
                + keywordSymbol + ".json?api_key=" + apikey;

        HttpClient client = mock(HttpClient.class);
        when(client.get(urlStr)).thenReturn(jsonData);

        Agent stocks = new StockAgent(client, apikey);
        AgentRequest request = new AgentRequest(keyword);
        AgentResponse response = stocks.process(request);

        assertEquals("Here is the longitudinal stock market data from the last 30 days for Facebook, Inc.",
                response.getText());

    }

    @Test
    public void testStockAgentExactMatch2() {
        String keyword = "Apple inc.";
        String keywordSymbol = "AAPL";

        ClassLoader classLoader = StockAgent.class.getClassLoader();
        URL url = classLoader.getResource("stockagent/MockJson_Apple");

        Path path = null;
        try {
            path = Paths.get(url.toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        String jsonData = "";
        try {
            jsonData = new Scanner(new File(path.toString())).useDelimiter("\\Z").next();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String urlStr = "https://www.quandl.com/api/v3/datasets/WIKI/"
                + keywordSymbol + ".json?api_key=" + apikey;

        HttpClient client = mock(HttpClient.class);
        when(client.get(urlStr)).thenReturn(jsonData);

        Agent stocks = new StockAgent(client, apikey);
        AgentRequest request = new AgentRequest(keyword);
        AgentResponse response = stocks.process(request);
        assertEquals("Here is the longitudinal stock market data from the last 30 days for Apple Inc.",
                response.getText());

    }

    @Test
    public void testStockAgentCloseMatch2() {
        String keyword = "apple";
        String keywordSymbol = "AAPL";

        ClassLoader classLoader = StockAgent.class.getClassLoader();
        URL url = classLoader.getResource("stockagent/MockJson_Apple");

        Path path = null;
        try {
            path = Paths.get(url.toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        String jsonData = "";
        try {
            jsonData = new Scanner(new File(path.toString())).useDelimiter("\\Z").next();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String urlStr = "https://www.quandl.com/api/v3/datasets/WIKI/"
                + keywordSymbol + ".json?api_key=" + apikey;

        HttpClient client = mock(HttpClient.class);
        when(client.get(urlStr)).thenReturn(jsonData);

        Agent stocks = new StockAgent(client, apikey);
        AgentRequest request = new AgentRequest(keyword);
        AgentResponse response = stocks.process(request);

        assertEquals("Here is the longitudinal stock market data from the last 30 days for Apple Inc.",
                response.getText());

    }

    @Test
    public void testStockAgentQuandlError() {
        String keyword = "First Bank";
        String keywordSymbol = "FRBA";

        String urlStr = "https://www.quandl.com/api/v3/datasets/WIKI/"
                + keywordSymbol + ".json?api_key=" + apikey;

        HttpClient client = mock(HttpClient.class);
        when(client.get(urlStr)).thenReturn(null);

        Agent stocks = new StockAgent(client, apikey);
        AgentRequest request = new AgentRequest(keyword);
        AgentResponse response = stocks.process(request);

        assertEquals("I am sorry, I can't find the proper stock data for the company "
                        + keyword + ".", response.getStatus().getMessage());

    }

    @Test
    public void testStockAgentFailure() {
        String keyword = "company that does not exist";

        String urlStr = "https://www.quandl.com/api/v3/datasets/WIKI/"
                + keyword + ".json?api_key=" + apikey;

        HttpClient client = mock(HttpClient.class);
        when(client.get(urlStr)).thenReturn(null);

        Agent stocks = new StockAgent(client, apikey);
        AgentRequest request = new AgentRequest(keyword);
        AgentResponse response = stocks.process(request);

        assertEquals("I am sorry, I don't understand which company you are asking about.",
                response.getStatus().getMessage());

    }
}
