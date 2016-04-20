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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import edu.jhuapl.dorset.Response;
import edu.jhuapl.dorset.ResponseStatus;
import edu.jhuapl.dorset.agents.AbstractAgent;
import edu.jhuapl.dorset.agents.AgentRequest;
import edu.jhuapl.dorset.agents.AgentResponse;
import edu.jhuapl.dorset.agents.Description;
import edu.jhuapl.dorset.http.HttpClient;
import edu.jhuapl.dorset.http.HttpRequest;
import edu.jhuapl.dorset.http.HttpResponse;

public class FlickrAgent extends AbstractAgent {
    private final Logger logger = LoggerFactory.getLogger(FlickrAgent.class);
    
    private static final String SUMMARY = "Search for images based on keywords";
    private static final String EXAMPLE = "Show me an apple";
    private static final String REGEX = "Show me (an |a |the )?(.*)";

    private String apikey;
    private HttpClient client;
    private Pattern pattern;

    /**
     * Create a Flickr agent
     *
     * @param client  An http client object
     * @param apikey  Flickr API key 
     */
    public FlickrAgent(HttpClient client, String apikey) {
        this.apikey = apikey;
        this.client = client;
        this.setDescription(new Description("images", SUMMARY, EXAMPLE));
        this.pattern = Pattern.compile(REGEX, Pattern.CASE_INSENSITIVE);
    }

    @Override
    public AgentResponse process(AgentRequest request) {
        logger.debug("Handling the request: " + request.getText());

        String agentRequest = request.getText();
        String searchPhrase = getSearchPhrase(agentRequest);
        logger.debug("Search phrase: " + searchPhrase);
        if (searchPhrase == null) {
            return new AgentResponse(ResponseStatus.Code.AGENT_DID_NOT_UNDERSTAND_REQUEST);
        }
        String json = getFlickrJson(searchPhrase);
        logger.debug("Flickr JSON: " + json);
        if (json == null) {
            return new AgentResponse(ResponseStatus.Code.AGENT_INTERNAL_ERROR);
        }
        String url = getImageUrl(json);
        logger.debug("Flickr image URL: " + url);
        byte[] data = getImage(url);
        if (data == null) {
            return new AgentResponse(ResponseStatus.Code.AGENT_INTERNAL_ERROR);
        }
        String payload = encodeImage(data);

        return new AgentResponse(Response.Type.IMAGE_EMBED, "Here is your image", payload);
    }

    protected String getSearchPhrase(String request) {
        String phrase = null;
        Matcher matcher = pattern.matcher(request);
        if (matcher.find()) {
            if (matcher.group(2) != null) {
                phrase = matcher.group(2).trim();
            }
        }
        return phrase;
    }

    protected String createFlickrRequestUrl(String searchPhrase) {
        try {
            searchPhrase = URLEncoder.encode(searchPhrase, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // this isn't going to happen
            logger.error("Unexpected exception when encoding url", e);
        }
        return "https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=" + apikey
                        + "&text=" + searchPhrase + "&sort=relevance"
                        + "&per_page=1&page=1&format=json&nojsoncallback=1";
    }

    protected String getFlickrJson(String searchPhrase) {
        String url = createFlickrRequestUrl(searchPhrase);
        HttpRequest httpRequest = HttpRequest.get(url);
        HttpResponse httpResponse = client.execute(httpRequest);
        if (httpResponse.isSuccess()) {
            return httpResponse.asString();
        } else {
            return null;
        }
    }

    protected String getImageUrl(String json) {
        Gson gson = new Gson();
        JsonObject jsonObj = gson.fromJson(json, JsonObject.class);
        JsonObject photos = jsonObj.getAsJsonObject("photos");
        JsonArray photoArray = photos.getAsJsonArray("photo");
        JsonObject photo = photoArray.get(0).getAsJsonObject();
        String id = photo.get("id").getAsString();
        String secret = photo.get("secret").getAsString();
        String server = photo.get("server").getAsString();
        String farm = photo.get("farm").getAsString();
        
        return "https://farm" + farm + ".staticflickr.com/" + server + "/" + id + "_" + secret + "_z.jpg";
    }

    protected byte[] getImage(String url) {
        HttpRequest httpRequest = HttpRequest.get(url);
        HttpResponse httpResponse = client.execute(httpRequest);
        if (httpResponse.isSuccess()) {
            return httpResponse.asBytes();
        } else {
            return null;
        }
    }

    protected String encodeImage(byte[] data) {
        return DatatypeConverter.printBase64Binary(data);
    }
}
