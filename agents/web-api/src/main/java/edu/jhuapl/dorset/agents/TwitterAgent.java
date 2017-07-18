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
import java.util.regex.Pattern;

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

import edu.jhuapl.dorset.ResponseStatus;
import edu.jhuapl.dorset.nlp.Tokenizer;
import edu.jhuapl.dorset.nlp.WhiteSpaceTokenizer;

public class TwitterAgent extends AbstractAgent{
    private final Logger logger = LoggerFactory.getLogger(TwitterAgent.class);

    private static final String VERIFY_CREDENTIALS_URL = "https://api.twitter.com/1.1/account/verify_credentials.json";
    private static final String POST_TWEET_URL = "https://api.twitter.com/1.1/statuses/update.json";
    private static final String GET_HOME_TIMELINE_URL = "https://api.twitter.com/1.1/statuses/home_timeline/.json";
    private static final String GET_MY_TIMELINE_URL = "https://api.twitter.com/1.1/statuses/user_timeline.json";
    private static final String GET_FAVORITES_URL = "https://api.twitter.com/1.1/favorites/list.json";
    private static final String GET_SEARCH_URL = "https://api.twitter.com/1.1/search/tweets.json";

    private static final String POST_REGEX = ".*(POST).*";
    private static final String GET_REGEX = ".*(GET).*";
    private static final String MINE_REGEX = ".*(MINE).*";
    private static final String FAVORITES_REGEX = ".*(FAVORITES).*";
    private static final String HOME_REGEX = ".*(HOME).*";
    
    private static final String END_OF_SINGULAR_TWEET = "\"default_profile_image\":";
    private static final String CREATED_AT = "\"created_at\":";
    private static final String END_OF_CREATED_AT = "\"id\":";
    private static final String NAME = "\"name\":";
    private static final String END_OF_NAME = "\"screen_name\":";
    private static final String TEXT = "\"text\":";
    private static final String END_OF_TEXT = "\"truncated\":";

    private static final String API_KEY_KEY = "apiKey";
    private static final String API_SECRET_KEY = "apiSecret";
    private static final String ACCESS_TOKEN_KEY = "accessToken";
    private static final String ACCESS_TOKEN_SECRET_KEY = "accessTokenSecret";
    private static final String RAW_RESPONSE_KEY = "rawResponse";

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
     * Twitter Agent
     *
     * The Twitter Agent posts and gathers tweets. It can read in tweets from
     * the user's home or individual timeline and list of favorites.
     *
     * @param config   the configuration values
     */
    public TwitterAgent(Config config) {
        apiKey = config.getString(API_KEY_KEY);
        apiSecret = config.getString(API_SECRET_KEY);
        accessToken = config.getString(ACCESS_TOKEN_KEY);
        accessTokenSecret = config.getString(ACCESS_TOKEN_SECRET_KEY);
        rawResponse = config.getString(RAW_RESPONSE_KEY);
        try {
            service = new ServiceBuilder().apiKey(apiKey).apiSecret(apiSecret)
                                .build(TwitterApi.instance());
            oauth = new OAuth1AccessToken(accessToken, accessTokenSecret, rawResponse);
        } catch (IllegalArgumentException e) {
            logger.error("Invalid account credentials or failed to connect to network: " + e);
        }
    }

    /**
     * Processes request
     *
     * @param agentRequest   the tweet request
     * @return AgentResponse
     */
    public AgentResponse process(AgentRequest agentRequest) {
        this.agentRequest = agentRequest;
        if (!verfiyAccountCredentials()) {
            return new AgentResponse(new ResponseStatus(ResponseStatus.Code.AGENT_INTERNAL_ERROR, 
                            "Could not verify account credentials. Check your connection and configuration values."));
        }

        String userRequest = this.agentRequest.getText().toUpperCase();
        if (userRequest.matches(POST_REGEX)) {
            return post();
        } else if (userRequest.matches(GET_REGEX)) {
            return get();
        } else {
            return new AgentResponse(new ResponseStatus(ResponseStatus.Code.AGENT_DID_NOT_UNDERSTAND_REQUEST, 
                            "Your request could not be understood."));
        }
    }

    /**
     * Create an OAuth Request to verify account credentials
     *
     * @return whether the account credentials worked correctly
     */
    private boolean verfiyAccountCredentials() {
        createOAuthRequest(Verb.GET, VERIFY_CREDENTIALS_URL);
        signRequest();
        
        Response response;
        try {
            response = service.execute(twitterRequest);
        } catch (InterruptedException | ExecutionException | IOException e) {
            logger.error("Could not verify account credentials. " + e);
            return false;
        }
        if (response.getMessage().toLowerCase().equals("Too Many Requests")) {
            logger.error("Could not process request at this time due to too many requests. "
                            + "Please wait a few minutes and try again.");
            return false;
        } else if (!response.getMessage().toLowerCase().equals("ok")) {
            logger.error("Could not process request due to an invalid configuration.");
            return false;
        }
        return true;
    }
    
    /**
     * Create an OAuth Request
     *
     * @param action   Verb.GET or Verb.POST
     * @param url   the url OAuth Request is sent to
     */
    private void createOAuthRequest(Verb action, String url) {
        twitterRequest = new OAuthRequest(action, url);
    }

    /**
     * Sign an OAuth Request
     */
    private void signRequest() {
        service.signRequest(oauth, twitterRequest);
    }

    /**
     * Creates, validates, and posts a tweet
     *
     * @return AgentResponse
     */
    private AgentResponse post() {
        String text = createTweetText();
        if (!checkCharLength(text)) {
            return new AgentResponse(new ResponseStatus(ResponseStatus.Code.AGENT_CANNOT_COMPLETE_ACTION,
                            "This text was either too many characters or empty. "
                            + "A tweet cannot be longer than 140 characters."));
        }
        return sendTweet(text);
    }

    /**
     * Creates the text to be tweeted
     *
     * @return the text to be tweeted
     */
    private String createTweetText() {
        String text = agentRequest.getText();
        return text.substring(0, text.indexOf("POST")) + text.substring(text.indexOf("POST") + 5);
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
     * @return AgentResponse
     */
    private AgentResponse sendTweet(String text) {
        createPostOAuthRequest(text);

        Response response;
        try {
            response = service.execute(twitterRequest);
        } catch (InterruptedException | ExecutionException | IOException e) {
            logger.error("Failed to post tweet");
            return new AgentResponse(new ResponseStatus(ResponseStatus.Code.AGENT_INTERNAL_ERROR,
                            "Could not execute request " + agentRequest));
        }

        try {
            if (!isDuplicateTweet(response)) {
                return new AgentResponse(new ResponseStatus(ResponseStatus.Code.AGENT_INTERNAL_ERROR, 
                                "This tweet was a duplicate and could not be posted")); 
            }
        } catch (IOException e) {
            logger.error("Failed to access execution response");
            return new AgentResponse(new ResponseStatus(ResponseStatus.Code.AGENT_INTERNAL_ERROR,
                            "Could not access twitter content" + agentRequest));
        }
        return new AgentResponse("Tweet successful");
    }

    /**
     * Create and send OAuth Requets to post a tweet
     *
     * @param text   the text to be tweeted
     */
    private void createPostOAuthRequest(String text) {
        createOAuthRequest(Verb.POST, POST_TWEET_URL);
        addBodyParameter("status", text);
        signRequest();
    }
    
    /**
     * Add a body parameter to twitter request
     *
     * @param param   body parameter
     * @param value   body parameter value
     */
    private void addBodyParameter(String param, String value) {
        twitterRequest.addBodyParameter(param, value);
    }

    /**
     * Checks whether the response reflects that the tweet was a duplicate and therefore wasn't posted
     *
     * @param response   the execution response
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

    /**
     * Get tweets
     *
     * @return AgentResponse
     */
    private AgentResponse get() {
        createGetOAuthRequest();

        Response response;
        try {
            response = service.execute(twitterRequest);
        } catch (InterruptedException | ExecutionException | IOException e) {
            return new AgentResponse(new ResponseStatus(ResponseStatus.Code.AGENT_INTERNAL_ERROR,
                            "Could not execute request " + agentRequest));
        }        
        if (checkEmptyResponse(response)) {
            return new AgentResponse(new ResponseStatus(ResponseStatus.Code.AGENT_INTERNAL_ERROR, 
                        "No tweets could be found in " + getUrlName() + "\nPlease be sure to "
                        + "phrase your request as such: action number location. \nactions: GET   POST"
                        + "\t\tlocations: HOME   MINE   search term or phrase"));
        } else if (!response.getMessage().toLowerCase().equals("ok")) {
            return new AgentResponse(new ResponseStatus(ResponseStatus.Code.AGENT_INTERNAL_ERROR, 
                            "Could not authenticate you. Check your configurations."));
        } else {
            return getTweets(response);
        }
    }

    /**
     * Creating and sending OAuth Request to get tweets
     */
    private void createGetOAuthRequest() {
        createOAuthRequest(Verb.GET, getUrl());
        String search = getSearch();
        if (search != null) {
            addParameter("q", search);
        }
        addParameter("count", getNumberOfTweets());
        signRequest();
    }

    /**
     * Get correct URL
     *
     * @return the url
     */
    private String getUrl() {
        String text = agentRequest.getText().toUpperCase();
        if (text.matches(MINE_REGEX)) {
            return GET_MY_TIMELINE_URL;
        } else if (text.matches(FAVORITES_REGEX)) {
            return GET_FAVORITES_URL;
        } else if (text.matches(HOME_REGEX)) {
            return GET_HOME_TIMELINE_URL;
        } else {
            return GET_SEARCH_URL;
        }
    }

    /**
     * Get the search word/phrase
     *
     * @return the search word/phrase
     */
    private String getSearch() {
        if (getUrl().equals(GET_SEARCH_URL)) {
            String[] requestTokenized = new WhiteSpaceTokenizer().tokenize(agentRequest.getText());
            String search = "";
            for (int n = 0; n < requestTokenized.length; n++) {
                if (!requestTokenized[n].equals("GET") && !Pattern.matches("[0-9]", requestTokenized[n])) {
                    search += requestTokenized[n];
                }
            }
            return search;
        } else {
            return null;
        }
    }

    /**
     * Add a parameter to twitter request
     *
     * @param param   parameter
     * @param value   parameter value
     */
    private void addParameter(String param, String value) {
        twitterRequest.addParameter(param, value);
    }

    /**
     * Gets the number of tweets specified by the user
     *
     * @return the number of tweets
     */
    private String getNumberOfTweets() {
        String[] requestTokenized = new WhiteSpaceTokenizer().tokenize(agentRequest.getText());
        for (int n = 0; n < requestTokenized.length; n++) {
            if (Pattern.matches("[0-9]", requestTokenized[n])) {
                Integer.parseInt(requestTokenized[n]);
                return requestTokenized[n];
            }
        }
        return "1";
    }

    /**
     * Check if the execution response is empty
     *
     * @param response   the execution response
     * @return whether the response is empty or not
     */
    private boolean checkEmptyResponse(Response response) {
        try {
            if (response.getBody().equals("[]") || response.getBody()
                            .contains("parameters are missing")) {
                return true;
            }
        } catch (IOException e) {
            logger.error("Failed to get twitter content");
            return true;
        }
        return false;
    }

    /**
     * Get the tweets on a timeline
     *
     * @param response   the response from attempting to access a timeline
     * @return AgentResponse
     */
    private AgentResponse getTweets(Response response) {
        String tweet = "";
        try {
            tweet = response.getBody();
        } catch (IOException e) {
            return new AgentResponse(new ResponseStatus(ResponseStatus.Code.AGENT_INTERNAL_ERROR,
                            "Could not access twitter content"));
        }

        if (tweet.contains("Rate limit")) {
            return new AgentResponse(new ResponseStatus(ResponseStatus.Code.AGENT_CANNOT_COMPLETE_ACTION, 
                            "Rate limit exceeded. Please wait a few minutes and try again."));
        } else {
            return createAgentResponse(tweet);
        }
    }

    /**
     * Creates an AgentResponse to display tweets
     *
     * @param tweet   the text response from geting the tweets
     * @return AgentResponse
     */
    private AgentResponse createAgentResponse(String tweet) {
        String wordTweet = "tweet";
        String numTweets = getNumberOfTweets();
        if (!numTweets.equals("1")) {
            wordTweet += "s";
        }

        String info = "";
        for (int n = 0; n < Integer.parseInt(numTweets); n ++) {
            info += "\n\n" + getDate(tweet) + "\n" + getUser(tweet) + "\n\t" + getText(tweet);
            tweet = tweet.substring(tweet.indexOf(END_OF_SINGULAR_TWEET));
        }
        return new AgentResponse("Showing " +  numTweets + " " + wordTweet + " from " + getUrlName() + info);
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
            date = tweet.substring((tweet.indexOf(CREATED_AT) + 14), (tweet.indexOf(END_OF_CREATED_AT) - 2));
        } catch (IndexOutOfBoundsException e) {
            date = "A date could not be retrievd";
            logger.error("Date of tweet could not be retreived");
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
            name = tweet.substring((tweet.indexOf(NAME) + 8), (tweet.indexOf(END_OF_NAME) - 2));
        } catch (IndexOutOfBoundsException e) {
            name = "A user could not be retreived";
            logger.error("Author of tweet could not be retreived");
        }
        return "Posted by: " + name;
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
            text = tweet.substring((tweet.indexOf(TEXT) + 8), (tweet.indexOf(END_OF_TEXT) - 2));
        } catch (IndexOutOfBoundsException e) {
            text = "Text could not be retrieved";
            logger.error("Text of tweet could not be retreived");
        }
        return "\t" + text;
    }

    /**
     * Get URL name
     *
     * @return the url name
     */
    private String getUrlName() {
        String url = getUrl();
        if (url.equals(GET_HOME_TIMELINE_URL)) {
            return "home timeline";
        } else if (url.equals(GET_MY_TIMELINE_URL)) {
            return "my timeline";
        } else if (url.equals(GET_FAVORITES_URL)) {
            return "favorites";
        } else if (url.equals(GET_SEARCH_URL)) {
            return "search for " + getSearch();
        } else {
            return "unknown";
        }
    }
}
