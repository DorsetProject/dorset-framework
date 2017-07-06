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

import edu.jhuapl.dorset.nlp.Tokenizer;
import edu.jhuapl.dorset.nlp.WhiteSpaceTokenizer;

public class TwitterAgent extends AbstractAgent{
    private final Logger logger = LoggerFactory.getLogger(TwitterAgent.class);

    private static final String PROTECTED_RESOURCE = "https://api.twitter.com/1.1/account/verify_credentials.json";
    private static final String POST_TWEET = "https://api.twitter.com/1.1/statuses/update.json";
    private static final String GET_HOME_TIMELINE = "https://api.twitter.com/1.1/statuses/home_timeline/.json";
    private static final String GET_MY_TIMELINE = "https://api.twitter.com/1.1/statuses/user_timeline.json";
    private static final String GET_FAVORITES = "https://api.twitter.com/1.1/favorites/list.json";
    
    private static final String API_KEY = "apiKey";
    private static final String API_SECRET = "apiSecret";
    private static final String ACCESS_TOKEN = "accessToken";
    private static final String ACCESS_TOKEN_SECRET = "accessTokenSecret";
    private static final String RAW_RESPONSE = "rawResponse";

    private static final int WITHOUT_POST = 5;
    
    private String apiKey;
    private String apiSecret;
    private String accessToken;
    private String accessTokenSecret;
    private String rawResponse;
    private OAuth10aService service;
    private OAuth1AccessToken oauth;
    private OAuthRequest twitterRequest;
    private AgentRequest agentRequest;

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
     * Processes request
     *
     * @param agentRequest   the tweet request
     * @return   Success or failure message
     * @throws InterruptedException   if...
     * @throws ExecutionException   if...
     * @throws IOException   if...
     */
    public AgentResponse process(AgentRequest agentRequest) {
        this.agentRequest = agentRequest;
        if (!verfiyAccountCredentials()) {
            return new AgentResponse("Could not create OAuth request. Check your connection.");
        }

        String userRequest = this.agentRequest.getText().toUpperCase();
        if (userRequest.contains("POST")) {
            return post();
        } else if (userRequest.contains("GET")) {
            return get();
        } else {
            return new AgentResponse("Your request could not be understood");
        }
    }
    
    /**
     * Create an OAuth Request
     *
     * @throws InterruptedException   if thread is waiting/sleeping and gets interrupted
     * @throws ExecutionException   if the result of creating the request cannot be found
     * @throws IOException   if the request cannot be created
     */
    private boolean verfiyAccountCredentials() {
        twitterRequest = new OAuthRequest(Verb.GET, PROTECTED_RESOURCE);
        service.signRequest(oauth, twitterRequest);
        
        try {
            service.execute(twitterRequest);
        } catch (InterruptedException e) {
            logger.error("Could not create OAuth request" + e);
            return false;
        } catch (ExecutionException e) {
            logger.error("Could not create OAuth request" + e);
            return false;
        } catch (IOException e) {
            logger.error("Could not create OAuth request" + e);
            return false;
        }
        return true;
    }
    
    /**
     * Creates, validates, and posts a tweet
     *
     * @return success or failure message
     */
    private AgentResponse post() {
        String text = createTweetText();
        if (!checkCharLength(text)) {
            return new AgentResponse("Too many characters");
        }
        
        try {
            sendTweet(text);
        } catch (InterruptedException e) {
            logger.error("Could not send tweet" + e);
            return new AgentResponse("Could not post Tweet. Check your connection.");
        } catch (ExecutionException e) {
            logger.error("Could not send tweet" + e);
            return new AgentResponse("Could not post Tweet. This tweet may have been a duplicate.");
        } catch (IOException e) {
            logger.error("Could not send tweet" + e);
            return new AgentResponse("Could not post Tweet. Check your connection.");
        }
        return new AgentResponse("Tweet successful");
    }

    /**
     * Creates the text to be tweeted
     *
     * @return the text to be tweeted
     */
    private String createTweetText() {
        String text = agentRequest.getText();
        return text.substring(WITHOUT_POST); 
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
     * Creates and post tweet
     *
     * @param text   the text to be tweeted
     * @throws InterruptedException   if thread is waiting/sleeping and gets interrupted
     * @throws ExecutionException   if the result of posting the tweet cannot be found
     * @throws IOException   if the tweet cannot be sent or is a duplicate
     */
    private void sendTweet(String text) throws InterruptedException, ExecutionException, IOException {
        twitterRequest = new OAuthRequest(Verb.POST, POST_TWEET);
        twitterRequest.addBodyParameter("status", text);
        service.signRequest(oauth, twitterRequest);

        Response response;
        try {
            response = service.execute(twitterRequest);
        } catch (InterruptedException e) {
            throw new InterruptedException("Could not post tweet. Check your network connection.");
        } catch (ExecutionException e) {
            throw new ExecutionException("Could not post tweet. Check your network connection.", e);
        } catch (IOException e) {
            throw new IOException("Could not post tweet. Check your network connection.", e);
        }
        
        //TODO dont throw this
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
    
    private AgentResponse get() {
        String url = getURL();
        twitterRequest = new OAuthRequest(Verb.GET, url);
        
        String numTweets = getNumberOfTweets();
        twitterRequest.addParameter("count", numTweets);
        service.signRequest(oauth, twitterRequest);
        
        Response response;
        try {
            response = service.execute(twitterRequest);
        } catch (InterruptedException e) {
            return new AgentResponse("Could not process request. " + agentRequest + " Check your connection. "
                            + "Rate limit may have been exceeded.");
        } catch (ExecutionException e) {
            return new AgentResponse("Could not process request. " + agentRequest + " Check your connection. "
                            + "Rate limit may have been exceeded.");
        } catch (IOException e) {
            return new AgentResponse("Could not process request. " + agentRequest + " Check your connection. "
                            + "Rate limit may have been exceeded.");
        }

        return getTweets(response);
    }

    /**
     * Gets the number of tweets specified by the user
     *
     * @return the number of tweets
     */
    private String getNumberOfTweets() {
        Tokenizer tokenizer = new WhiteSpaceTokenizer();
        String[] requestTokenized = tokenizer.tokenize(agentRequest.getText());
        if (requestTokenized.length < 2) {
            return "1";
        }
        try {
            Integer.parseInt(requestTokenized[1]); 
        } catch (NumberFormatException e) {
            return "1";
        }
        return requestTokenized[1];
    }
    
    /**
     * Get URL
     *
     * @return the url
     */
    private String getURL() {
        String url = GET_HOME_TIMELINE;
        if (agentRequest.getText().toUpperCase().contains(" ME")) {
            url = GET_MY_TIMELINE;
        } else if (agentRequest.getText().toUpperCase().contains("FAVORITES")) {
            url = GET_FAVORITES;
        }
        return url;
    }
    
    /**
     * Get URL name
     *
     * @return the url name
     */
    private String getURLName() {
       String url = getURL();
       if (url.equals(GET_HOME_TIMELINE)) {
           return "home timeline";
       } else if (url.equals(GET_MY_TIMELINE)) {
           return "my timeline";
       } else if (url.equals(GET_FAVORITES)){
           return "favorites";
       } else {
           return "unknown";
       }
    }
    
    /**
     * Get the tweets on a timeline
     *
     * @param response   the response from attempting to access a timeline
     * @return success or failure message
     */
    private AgentResponse getTweets(Response response) {
        String tweet = "";
        try {
            tweet = response.getBody();
        } catch (IOException e) {
            return new AgentResponse("Could not access timeline. Check your connection.");
        }
        
        if (tweet.contains("Rate limit")) {
            return new AgentResponse("Rate limit exceeded. Please wait a few minutes and try again.");
        } else {
            if (getNumberOfTweets().equals("1")) {
                return new AgentResponse("Showing " +  getNumberOfTweets() + " tweet from " + getURLName() 
                                + "\n" + getDate(tweet) + "\n" + getUser(tweet) + "\n\t" + getText(tweet));
            } else {
                return new AgentResponse("Showing " +  getNumberOfTweets() + " tweets from " + getURLName() 
                                + "\n" + getDate(tweet) + "\n" + getUser(tweet) + "\n\t" + getText(tweet));
            }
        }
    }

    /**
     * Get the date of a tweet
     *
     * @param tweet   a tweet
     * @return the date of a tweet
     */
    private String getDate(String tweet) {
        String date;
        try {
            date = tweet.substring((tweet.indexOf("\"created_at\":") + 14), (tweet.indexOf("\"id\":") - 2));
        } catch (IndexOutOfBoundsException e) {
            date = "A date could not be retrievd";
            logger.error("Date of tweet could not be received");
        }
            return "Posted on: " + date;
    }
    
    /**
     * Get the author of a tweet
     *
     * @param tweet   a tweet
     * @return the author of a tweet
     */
    private String getUser(String tweet) {
        String name;
        try {
            name = tweet.substring((tweet.indexOf("\"name\":") + 8), (tweet.indexOf("\"screen_name\":") - 2));
        } catch (IndexOutOfBoundsException e) {
            name = "A user could not be found";
            logger.error("Author of tweet could not be received");
        }
        return "User: " + name;
    }
    
    /**
     * Get the text of a tweet
     *
     * @param tweet   a tweet
     * @return the text of a tweet
     */
    private String getText(String tweet) {
        String text;
        try {
            text = tweet.substring((tweet.indexOf("\"text\":") + 8), (tweet.indexOf("\"truncated\":") - 2));
        } catch (IndexOutOfBoundsException e) {
            text = "Text could not be retrieved";
            logger.error("Text of tweet could not be received");
        }
        return "\t" + text;
    }
}
