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
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import edu.jhuapl.dorset.http.HttpClient;

public class StockAgent extends AbstractAgent {

    private String baseurl = "https://www.quandl.com/api/v3/datasets/WIKI/";
    private String apiKey;
    private HttpClient client;
    private TreeMap<String, String> stockSymbolMap;
    private static final int DAYS_IN_A_MONTH = 30;
    private static final Logger logger = LoggerFactory.getLogger(StockAgent.class);

    private static final CellProcessor[] processors = new CellProcessor[] {
            new NotNull(), new NotNull(), new NotNull(), new Optional(),
            new Optional(), new Optional(), new NotNull(), new Optional() };

    /**
     * Stock agent
     *
     * @param client An http client object
     * @param apiKey A Quandl API key
     */
    public StockAgent(HttpClient client, String apiKey) {
        this.client = client;
        this.apiKey = apiKey;

        ICsvBeanReader csvBeanReader = null;

        InputStream nasdaqCompanies = StockAgent.class.getClassLoader().getResourceAsStream("stockagent/NASDAQ_Companies.csv");

        this.stockSymbolMap = new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);

        try {
            csvBeanReader = new CsvBeanReader(new BufferedReader(new InputStreamReader(nasdaqCompanies)),
                    CsvPreference.STANDARD_PREFERENCE);
            final String[] header = csvBeanReader.getHeader(true);
            CompanyInfo companyInfo;
            while ((companyInfo = csvBeanReader.read(CompanyInfo.class, header, processors)) != null) {
                this.stockSymbolMap.put(companyInfo.getName(), companyInfo.getSymbol());
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            logger.debug("Failed to load stockagent/NASDAQ_Companies.csv");
        }

        InputStream nyseCompanies = StockAgent.class.getClassLoader()
                .getResourceAsStream("stockagent/NYSE_Companies.csv");

        try {
            csvBeanReader = new CsvBeanReader(new BufferedReader(new InputStreamReader(nyseCompanies)),
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
            logger.debug("Failed to load stockagent/NYSE_Companies.csv");
        } 

    }

    @Override
    public AgentResponse process(AgentRequest request) {
        logger.debug("Handling the request: " + request.getText());

        String requestCompanyName = request.getText();
        String[] stockCompanyInfo = findStockSymbol(requestCompanyName);
        String keywordCompanyName = null;
        String keywordCompanySymbol = null;

        if (stockCompanyInfo[0] != null) {
            keywordCompanyName = stockCompanyInfo[0].substring(0, 1).toUpperCase()
                    + stockCompanyInfo[0].substring(1, stockCompanyInfo[0].length());
            keywordCompanySymbol = stockCompanyInfo[1];
        }

        String json = null;

        if (keywordCompanySymbol == null) {
            return new AgentResponse(
                    "I am sorry, I don't understand which company you are asking about.");
        } else {
            json = requestData(keywordCompanySymbol);

        }

        if (json == null) {
            if (keywordCompanyName != null) {
                return new AgentResponse(("I am sorry, I can't find the proper stock data for the company "
                                + keywordCompanyName + ".").replace("..", "."));
            } else {
                return new AgentResponse(ResponseStatus.Code.AGENT_DID_NOT_KNOW_ANSWER);
            }

        } else {
            JsonObject returnObj = processData(json, keywordCompanyName);

            if (returnObj != null) {
                return new AgentResponse(Response.Type.JSON,
                        ("Here is the longitudinal stock market data from the last "
                                + DAYS_IN_A_MONTH
                                + " days for "
                                + keywordCompanyName + ".").replace("..", "."),
                        returnObj.toString());
            } else {
                return new AgentResponse(ResponseStatus.Code.AGENT_DID_NOT_KNOW_ANSWER);
            }
        }

    }

    protected String[] findStockSymbol(String stockCompanyName) {

        String keywordCompanyName = null;
        String keywordCompanySymbol = null;

        ArrayList<String> regexMatches = new ArrayList<String>();

        if (this.stockSymbolMap.get(stockCompanyName) != null) {
            keywordCompanyName = stockCompanyName;
            keywordCompanySymbol = this.stockSymbolMap.get(stockCompanyName);

        } else {
            String regex = "\\b" + stockCompanyName + "\\b";

            Pattern pat = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);

            for (Map.Entry<String, String> entry : stockSymbolMap.entrySet()) {
                Matcher matcher = pat.matcher(entry.getKey());

                if (matcher.find()) {
                    regexMatches.add(entry.getKey());
                }
            }

            if (regexMatches.size() == 0) {
                keywordCompanyName = null;
                keywordCompanySymbol = null;
            } else if (regexMatches.size() == 1) {
                keywordCompanyName = regexMatches.get(0);
                keywordCompanySymbol = this.stockSymbolMap.get(regexMatches.get(0));

            } else {
                int distance;
                HashMap<String, Integer> matchDistanceMap = new HashMap<String, Integer>();
                for (int i = 0; i < regexMatches.size(); i++) {
                    distance = (StringUtils.getLevenshteinDistance(
                            regexMatches.get(i), stockCompanyName));
                    matchDistanceMap.put(regexMatches.get(i), distance);
                }

                Entry<String, Integer> minDistancePair = null;
                for (Entry<String, Integer> entry : matchDistanceMap.entrySet()) {
                    if (minDistancePair == null || minDistancePair.getValue() > entry.getValue()) {
                        minDistancePair = entry;
                    }
                }

                keywordCompanyName = minDistancePair.getKey();
                keywordCompanySymbol = this.stockSymbolMap.get(minDistancePair.getKey());

            }

        }

        return new String[] { keywordCompanyName, keywordCompanySymbol };
    }

    protected JsonObject processData(String json, String keyWordCompanyName) {

        Gson gson = new Gson();
        JsonObject returnObj = null;
        JsonObject jsonObj = gson.fromJson(json, JsonObject.class);

        if (jsonObj != null) {

            if ((jsonObj.get("dataset")) != null) {
                JsonArray jsonDataArray = (JsonArray) (((JsonObject) jsonObj.get("dataset")).get("data"));

                ArrayList<JsonElement> responseDataArrayList = new ArrayList<>();
                ArrayList<JsonElement> responseLabelsArrayList = new ArrayList<>();

                for (int i = 0; i < jsonDataArray.size(); i++) {
                    JsonArray jsonDataArrayNested = (JsonArray) (jsonDataArray
                            .get(i));
                    responseDataArrayList.add(jsonDataArrayNested.get(4));
                    responseLabelsArrayList.add(jsonDataArrayNested.get(0));
                }
                Collections.reverse(responseDataArrayList);
                Collections.reverse(responseLabelsArrayList);

                returnObj = gson.fromJson(json.toString(), JsonObject.class);

                returnObj.addProperty("plotType", "lineplot");

                List<JsonElement> returnDataJsonList = responseDataArrayList.subList(responseDataArrayList.size() - DAYS_IN_A_MONTH,
                                responseDataArrayList.size());
                returnObj.addProperty("data", returnDataJsonList.toString());

                List<JsonElement> returnLabelsJsonList = responseLabelsArrayList.subList(responseLabelsArrayList.size() - DAYS_IN_A_MONTH,
                                responseLabelsArrayList.size());
                returnObj.addProperty("labels", returnLabelsJsonList.toString());
                returnObj.addProperty("title", keyWordCompanyName + " Stock Ticker");
                returnObj.addProperty("xaxis", "Day");
                returnObj.addProperty("yaxis", "Close of day market price ($)");
            }

        }

        return returnObj;
    }

    protected String requestData(String keyword) {
        String url = this.baseurl + keyword + ".json?api_key=" + apiKey;
        return client.get(url);
    }

}
