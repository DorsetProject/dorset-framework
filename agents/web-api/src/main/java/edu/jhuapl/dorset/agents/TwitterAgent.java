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

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.scribejava.apis.TwitterApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth10aService;
import com.typesafe.config.Config;

public class TwitterAgent extends AbstractAgent{
    private final Logger logger = LoggerFactory.getLogger(TwitterAgent.class);

    private static final String PROTECTED_RESOURCE_URL = "https://api.twitter.com/1.1/account/verify_credentials.json";
    private static final String NEW_TWEET_URL = "https://api.twitter.com/1.1/statuses/update.json";    
    private static final String API_KEY = "apiKey";
    private static final String API_SECRET = "apiSecret";
    private static final String ACCESS_TOKEN = "accessToken";
    private static final String ACCESS_TOKEN_SECRET = "accessTokenSecret";
    private static final String RAW_RESPONSE = "rawResponse";

    private String apiKey;
    private String apiSecret;
    private String accessToken;
    private String accessTokenSecret;
    private String rawResponse;
    private OAuth10aService service;
    private OAuth1AccessToken oauth;
    private OAuthRequest twitterRequest;

    /**
     * Create a Twitter Agent
     *
     * @param config   the configuration values
     */
    public TwitterAgent(Config config) {
        apiKey = config.getString(API_KEY);
        apiSecret = config.getString(API_SECRET);
        accessToken = config.getString(ACCESS_TOKEN);
        accessTokenSecret = config.getString(ACCESS_TOKEN_SECRET);
        rawResponse = config.getString(RAW_RESPONSE);

        service = new ServiceBuilder().apiKey(apiKey).apiSecret(apiSecret)
                        .build(TwitterApi.instance());
        oauth = new OAuth1AccessToken(accessToken, accessTokenSecret, rawResponse);
    }

    /**
     * Process Agent Request and send tweet
     *
     * @param request   the tweet request
     * @return   Success or failure
     * @throws InterruptedException   if...
     * @throws ExecutionException   if...
     * @throws IOException   if...
     */
    public AgentResponse process(AgentRequest request) {
        if (!checkCharLength(request.getText())) {
            return new AgentResponse("Too many characters");
        }

        try {
            createOAuthRequest();
            sendTweet(request.getText());
        } catch (InterruptedException e) {
            logger.error("Could not create OAuth request and send tweet" + e);
            return new AgentResponse("Could not post Tweet. Check your connection."
                            + "This tweet may have been a duplicate.");
        } catch (ExecutionException e) {
            logger.error("Could not create OAuth request and send tweet" + e);
            return new AgentResponse("Could not post Tweet. Check your connection."
                            + "This tweet may have been a duplicate.");
        } catch (IOException e) {
            logger.error("Could not create OAuth request and send tweet" + e);
            return new AgentResponse("Could not post Tweet. Check your connection."
                            + "This tweet may have been a duplicate.");
        }
        return new AgentResponse("Tweet successful");
    }

    /**
     * Checks if request text is a valid length
     *
     * @param text   the text to be validated
     * @return   whether the length is valid
     */
    private boolean checkCharLength(String text) {
        if (text.length() > 140 || text.length() == 0) {
            return false;
        }
        return true;
    }

    /**
     * Create a OAuth Request
     *
     * @throws InterruptedException   if thread is waiting/sleeping and gets interrupted
     * @throws ExecutionException   if the result of creating the request cannot be found
     * @throws IOException   if the request cannot be created
     */
    private void createOAuthRequest() throws InterruptedException, ExecutionException, IOException {
        twitterRequest = new OAuthRequest(Verb.GET, PROTECTED_RESOURCE_URL);
        service.signRequest(oauth, twitterRequest);
        
        try {
            service.execute(twitterRequest);
        } catch (InterruptedException e) {
            throw new InterruptedException("Could not create OAuth request.Check your network connection.");
        } catch (ExecutionException e) {
            throw new ExecutionException("Could not create OAuth request.Check your network connection.", e);
        } catch (IOException e) {
            throw new IOException("Could not create OAuth request.Check your network connection.", e);
        }
    }

    /**
     * Creates and post tweet
     *
     * @param text   the text to be tweeted
     * @throws InterruptedException   if thread is waiting/sleeping and gets interrupted
     * @throws ExecutionException   if the result of posting the tweet cannot be found
     * @throws IOException   if the tweet cannot be sent or is a duplicate
     */
    private void sendTweet(String text) throws InterruptedException, ExecutionException, IOException {
        twitterRequest = new OAuthRequest(Verb.POST, NEW_TWEET_URL);
        twitterRequest.addBodyParameter("status", text);
        service.signRequest(oauth, twitterRequest);

        Response response;
        try {
            response = service.execute(twitterRequest);
        } catch (InterruptedException e) {
            throw new InterruptedException("Could not post tweet.Check your network connection.");
        } catch (ExecutionException e) {
            throw new ExecutionException("Could not post tweet.Check your network connection.", e);
        } catch (IOException e) {
            throw new IOException("Could not post tweet.Check your network connection.", e);
        }
        
        if (!isDuplicateTweet(response)) {
            throw new IOException("Duplicate tweet"); 
        }
    }
    
    /**
     * Checks whether the response reflects that the tweet was a duplicate and therefore wasn't posted
     *
     * @param response   the response from attempting to post a tweet
     * @return   whether the tweet was a duplicate or not
     * @throws IOException   if response cannot be read
     */
    private boolean isDuplicateTweet(Response response) throws IOException {
        if (response.getBody().contains("duplicate")) {
            return false;
        } else {
            return true;
        }
    }
}