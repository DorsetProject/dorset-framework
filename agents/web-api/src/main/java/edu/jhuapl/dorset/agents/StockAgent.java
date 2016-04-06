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

package edu.jhuapl.dorset.agents;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import edu.jhuapl.dorset.Response;
import edu.jhuapl.dorset.ResponseStatus;
import edu.jhuapl.dorset.agents.AbstractAgent;
import edu.jhuapl.dorset.agents.AgentRequest;
import edu.jhuapl.dorset.agents.AgentResponse;
import edu.jhuapl.dorset.http.HttpClient;

public class StockAgent extends AbstractAgent {

    private String baseurl = "https://www.quandl.com/api/v3/datasets/WIKI/";
    private String apiKey;
    private HttpClient client;
    private HashMap<String, String> stockSymbolMap;
    private final int daysInAMonth = 30;

    private static final CellProcessor[] processors = new CellProcessor[] {
            new NotNull(), new NotNull(), new NotNull(), new Optional(),
            new Optional(), new Optional(), new NotNull(), new Optional() };

    /**
     * Stock agent
     *
     * @param client  An http client object
     * @param apiKey  A Quandl API key 
     */
    public StockAgent(HttpClient client, String apiKey) {
        this.client = client;
        this.apiKey = apiKey;

        ICsvBeanReader csvBeanReader = null;

        InputStream nasdaqCompanies = StockAgent.class.getClassLoader().getResourceAsStream("stockagent/NASDAQ_Companies.csv");

        this.stockSymbolMap = new HashMap<String, String>();
        try {
            csvBeanReader = new CsvBeanReader(new BufferedReader( new InputStreamReader(nasdaqCompanies)),
                    CsvPreference.STANDARD_PREFERENCE);
            final String[] header = csvBeanReader.getHeader(true);
            CompanyInfo companyInfo;
            while ((companyInfo = csvBeanReader.read(CompanyInfo.class, header, processors)) != null) {
                this.stockSymbolMap.put(companyInfo.getName(), companyInfo.getSymbol());
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (csvBeanReader != null) {
                try {
                    csvBeanReader.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();

                }
            }
        }

        InputStream nyseCompanies = StockAgent.class.getClassLoader().getResourceAsStream("stockagent/NYSE_Companies.csv");

        try {
            csvBeanReader = new CsvBeanReader(new BufferedReader( new InputStreamReader(nyseCompanies)),
                    CsvPreference.STANDARD_PREFERENCE);
            final String[] header = csvBeanReader.getHeader(true);
            CompanyInfo companyInfo;
            while ((companyInfo = csvBeanReader.read(CompanyInfo.class, header, processors)) != null) {

                if (!this.stockSymbolMap.containsKey(companyInfo.getName())) {
                    this.stockSymbolMap.put(companyInfo.getName(), companyInfo.getSymbol());
                }
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (csvBeanReader != null) {
                try {
                    csvBeanReader.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

    }

    @Override
    public AgentResponse process(AgentRequest request) {

        String requestCompanyName = request.getText().toLowerCase().replace("stocks ", "").replace("stock ", "");
        String[] stockCompanyInfo = findStockSymbol(requestCompanyName);
        String keywordCompanyName = stockCompanyInfo[0];
        String keywordCompanySymbol = stockCompanyInfo[1];
        String json = null;

        if (keywordCompanySymbol == null) {
            return new AgentResponse("I am sorry, I don't understand which company you are asking about.");
        } else {
            json = requestData(keywordCompanySymbol);
        }
        
        JsonObject returnObj = processData(json, keywordCompanyName);
        
        if (returnObj != null ) {
            return new AgentResponse(
                    Response.Type.JSON,
                    ("Here is the longitudinal stock market data from the last " + daysInAMonth + " days for "
                            + keywordCompanyName + ".").replace("..", "."),
                    returnObj.toString());
        } else if ( returnObj == null && keywordCompanyName != null) {
            return new AgentResponse(
                    ("I am sorry, I can't find the proper stock data for the company "
                            + keywordCompanyName + ".").replace("..", "."));
        } else {
            return new AgentResponse(ResponseStatus.Code.AGENT_DID_NOT_KNOW_ANSWER);
        }

    }

    protected String[] findStockSymbol(String stockCompanyName) {

        String keywordCompanyName = null;
        String keywordCompanySymbol = null;

        int minDistance = 8;

        for (Map.Entry<String, String> entry : stockSymbolMap.entrySet()) {

            if ((entry.getKey().toLowerCase().startsWith(stockCompanyName.toLowerCase()))) {
                int companyNameLength = stockCompanyName.length();
                int keywordCompanyNameLength = entry.getKey().length();
                if (((double) companyNameLength / keywordCompanyNameLength) > 0.35) {
                    minDistance = 4;
                    keywordCompanyName = entry.getKey();
                    keywordCompanySymbol = entry.getValue();
                }
            } else if ((StringUtils.getLevenshteinDistance(entry.getKey().toLowerCase(),
                            stockCompanyName.toLowerCase(), minDistance)) < minDistance
                    && ((StringUtils.getLevenshteinDistance(entry.getKey(),
                            stockCompanyName, minDistance)) != -1)) {

                minDistance = (StringUtils.getLevenshteinDistance(
                        entry.getKey(), stockCompanyName, minDistance));
                keywordCompanyName = entry.getKey();
                keywordCompanySymbol = entry.getValue();

            }
        }
        return new String[] { keywordCompanyName, keywordCompanySymbol };
    }

    
    protected JsonObject processData(String json, String keyWordCompanyName) {

        Gson gson = new Gson();
        JsonObject returnObj = null;
        try {
            JsonObject jsonObj = gson.fromJson(json.toString(), JsonObject.class);

            JsonArray jsonDataArray = (JsonArray) (((JsonObject) jsonObj.get("dataset")).get("data"));

            ArrayList<JsonElement> responseDataArrayList = new ArrayList<>();
            ArrayList<JsonElement> responseLabelsArrayList = new ArrayList<>();
            
            for (int i = 0; i < jsonDataArray.size(); i++) {
                JsonArray jsonDataArrayNested = (JsonArray) (jsonDataArray.get(i));
                responseDataArrayList.add(jsonDataArrayNested.get(4));
                responseLabelsArrayList.add(jsonDataArrayNested.get(0));
            }
            Collections.reverse(responseDataArrayList);
            Collections.reverse(responseLabelsArrayList);

            returnObj = gson.fromJson(json.toString(), JsonObject.class);
           
            returnObj.addProperty("plotType", "lineplot");
            
            List<JsonElement> returnDataJsonList = responseDataArrayList.subList(responseDataArrayList.size() - daysInAMonth, responseDataArrayList.size());
            returnObj.addProperty("data", returnDataJsonList.toString());
            
            List<JsonElement> returnLabelsJsonList = responseLabelsArrayList.subList(responseLabelsArrayList.size() - daysInAMonth, responseLabelsArrayList.size());
            returnObj.addProperty("labels", returnLabelsJsonList.toString());
            returnObj.addProperty("title", keyWordCompanyName + " Stock Ticker");
            returnObj.addProperty("xaxis", "Day");
            returnObj.addProperty("yaxis", "Close of day market price ($)");
        } catch (NullPointerException e) {
            e.printStackTrace();
            return null;
        }

        return returnObj;
    }
    
    protected String requestData(String keyword) {
        String url = this.baseurl + keyword + ".json?api_key=" + apiKey;
        return client.get(url);
    }

}
