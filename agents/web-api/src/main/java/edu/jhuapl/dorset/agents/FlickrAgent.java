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
import edu.jhuapl.dorset.agent.AbstractAgent;
import edu.jhuapl.dorset.agent.AgentRequest;
import edu.jhuapl.dorset.agent.AgentResponse;
import edu.jhuapl.dorset.agent.Description;
import edu.jhuapl.dorset.http.HttpClient;

public class FlickrAgent extends AbstractAgent {
    private final Logger logger = LoggerFactory.getLogger(FlickrAgent.class);
    
    private static final String SUMMARY = "Search for images based on keywords";
    private static final String EXAMPLE = "Show me an apple";
    private static final String REGEX = "Show me (a|an|the) (.*)";

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
        this.pattern = Pattern.compile(REGEX);
    }

    @Override
    public AgentResponse process(AgentRequest request) {
        logger.debug("Handling the request: " + request.getText());

        String agentRequest = request.getText();
        String searchPhrase = getSearchPhrase(agentRequest);
        if (searchPhrase == null) {
            return new AgentResponse(ResponseStatus.Code.AGENT_DID_NOT_UNDERSTAND_REQUEST);
        }
        String json = getFlickrJson(searchPhrase);
        String url = getImageUrl(json);
        byte[] data = getImage(url);
        String payload = encodeImage(data);

        return new AgentResponse(Response.Type.EMBEDDED_IMAGE, "Here is your image", payload);
    }

    protected String getSearchPhrase(String request) {
        String phrase = null;
        Matcher m = pattern.matcher(request);
        if (m.find()) {
            if (m.group(2) != null) {
                phrase = m.group(2);
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
                        + "&text=" + searchPhrase
                        + "&per_page=1&page=1&format=json&nojsoncallback=1";
    }

    protected String getFlickrJson(String searchPhrase) {
        String url = createFlickrRequestUrl(searchPhrase);
        return client.get(url);
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

    // TODO
    protected byte[] getImage(String url) {
        // need to extend http client to get binary data
        byte[] data = new byte[4];
        data[0] = 32;
        data[1] = 76;
        data[2] = 98;
        data[3] = 125;

        return data;
    }

    protected String encodeImage(byte[] data) {
        return DatatypeConverter.printBase64Binary(data);
    }
}
