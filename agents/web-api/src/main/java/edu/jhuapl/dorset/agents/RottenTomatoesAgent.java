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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import edu.jhuapl.dorset.ResponseStatus;
import edu.jhuapl.dorset.agents.AbstractAgent;
import edu.jhuapl.dorset.agents.AgentRequest;
import edu.jhuapl.dorset.agents.AgentResponse;
import edu.jhuapl.dorset.agents.Description;
import edu.jhuapl.dorset.http.HttpClient;
import edu.jhuapl.dorset.http.HttpRequest;
import edu.jhuapl.dorset.http.HttpResponse;
import edu.jhuapl.dorset.nlp.WhiteSpaceTokenizer;

public class RottenTomatoesAgent extends AbstractAgent {
    private final Logger logger = LoggerFactory.getLogger(RottenTomatoesAgent.class);
    
    private static final String SUMMARY = "Get answers to questions about movies. Ask about actors, actors, runtime, or release date.";
    private static final String EXAMPLE = "Who are the actors in the movie The Shining?";

    private String apikey;
    private HttpClient client;

    /**
     * Create a Rotten Tomatoes agent
     *
     * @param client  An http client object
     * @param apikey  A Rotten Tomatoes API key 
     */
    public RottenTomatoesAgent(HttpClient client, String apikey) {
        this.apikey = apikey;
        this.client = client;
        this.setDescription(new Description("movies", SUMMARY, EXAMPLE));
    }

    @Override
    public AgentResponse process(AgentRequest request) {
        logger.debug("Handling the request: " + request.getText());

        String agentRequest = request.getText();

        String movieTitle = findMovieTitle(agentRequest);
        if (movieTitle.equals("")) {
            return new AgentResponse(ResponseStatus.Code.AGENT_DID_NOT_UNDERSTAND_REQUEST);
        }

        String keyword = findKeyWord(agentRequest);
        if (keyword == null) {
            return new AgentResponse(ResponseStatus.Code.AGENT_DID_NOT_UNDERSTAND_REQUEST);
        }

        AgentResponse response = null;
        String json = requestData(movieTitle);

        if (json == null) {
            response = new AgentResponse(new ResponseStatus(
                            ResponseStatus.Code.AGENT_INTERNAL_ERROR,
                            "Something went wrong with the Rotten Tomatoes API request. Please check your API key."));
        } else {
            String responseText = formatResponse(keyword, json);
            if (responseText != null) {
                response = new AgentResponse(responseText);
            } else {
                response = new AgentResponse(ResponseStatus.Code.AGENT_INTERNAL_ERROR);
            }
        }

        return response;
    }

    protected String requestData(String movieTitle) {
        movieTitle = movieTitle.replace(" ", "%20");
        String url = "http://api.rottentomatoes.com/api/public/v1.0/movies.json?apikey="
                + this.apikey + "&q=" + movieTitle;
        HttpResponse response = client.execute(HttpRequest.Get(url));
        return response.asString();
    }

    protected String findMovieTitle(String agentRequest) {

        WhiteSpaceTokenizer bt = new WhiteSpaceTokenizer();
        String movieTitle = "";
        boolean filmFlag = false;

        String[] tokens = bt.tokenize(agentRequest);
        for (String token : tokens) {
            token = token.toLowerCase();
            if (filmFlag) {
                movieTitle = movieTitle + " " + token;
            }
            if (token.equals("movie") || token.equals("film") && !filmFlag) {
                filmFlag = true;
            }
        }
        movieTitle = movieTitle.replace("?", "").replace(".", "").trim();

        return movieTitle;
    }

    protected String findKeyWord(String agentRequest) {
        String keyword = null;

        if (agentRequest.toLowerCase().contains("year")) {
            // year the film was made
            keyword = "year";
        } else if (agentRequest.toLowerCase().contains("runtime")) {
            // the length of the film
            keyword = "runtime";
        } else if (agentRequest.toLowerCase().contains("mpaa rating")) {
            // film rating
            keyword = "mpaa_rating";
        } else if (agentRequest.toLowerCase().contains("actor")) {
            // actor names
            keyword = "name";
        }
        return keyword;
    }

    protected String formatResponse(String keyword, String response) {
        Gson gson = new Gson();

        JsonObject jsonObj = gson.fromJson(response.toString(),
                JsonObject.class);
        JsonArray jsonMoviesArray = jsonObj.get("movies").getAsJsonArray();

        if (jsonMoviesArray.size() == 0) {
            return "I am sorry, I don't know that movie.";
        }

        JsonObject jsonObjMovie = gson.fromJson(jsonMoviesArray.get(0),
                JsonObject.class);

        JsonArray jsonCastArray = jsonObjMovie.get("abridged_cast")
                .getAsJsonArray();

        String movieTitle = jsonObjMovie.get("title").toString();

        String agentResponse = null;

        switch (keyword) {
            case "runtime":
                agentResponse = "The runtime for the film, "
                        + movieTitle.replace("\"", " ").trim() + ", is "
                        + jsonObjMovie.get(keyword) + " minutes long.";
                break;
            case "year":
                agentResponse = "The year the film, "
                        + movieTitle.replace("\"", " ").trim()
                        + ", was created is " + jsonObjMovie.get(keyword) + ".";
                break;
            case "mpaa_rating":
                agentResponse = "The MPAA rating for the film, "
                        + movieTitle.replace("\"", " ").trim() + ", is "
                        + jsonObjMovie.get(keyword) + ".";
                break;
            case "name":
                String nameList = "";
                String name = "";
                for (int i = 0; i < jsonCastArray.size(); i++) {
                    JsonObject jsonObjNames = gson.fromJson(jsonCastArray.get(i),
                            JsonObject.class);
                    name = jsonObjNames.get("name").toString();

                    if (i != jsonCastArray.size() - 1) {
                        nameList = nameList + name + ", ";
                    } else {
                        nameList = nameList + name + ".";
                    }
                }
                nameList = nameList.replace("\"", "").trim();

                agentResponse = "The film, " + movieTitle.replace("\"", " ").trim()
                        + ", stars actors " + nameList;
                break;
            default:
                break;
        }
        return agentResponse;
    }

}
