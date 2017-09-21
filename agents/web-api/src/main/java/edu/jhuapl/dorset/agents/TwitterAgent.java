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

import edu.jhuapl.dorset.ResponseStatus;

/**
 * Twitter Agent for posting tweets.
 *
 * Currently only handles a single user.
 * The user's access tokens must be configured and passed into constructor.
 * The configuration should look like this:
 *   apiKey=xxxxxxxxxxxxxxxx
 *   apiSecret=xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
 *   accessToken=xxxxxxxxxx-xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
 *   accessTokenSecret=xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
 * The API keys and tokens can be retrieved at https://apps.twitter.com/
 *
 * The agent understands requests of the form:
 * tweet text to tweet
 *   or
 * text to tweet
 */
public class TwitterAgent extends AbstractAgent{
    private final Logger logger = LoggerFactory.getLogger(TwitterAgent.class);

    private static final String VERIFY_CREDENTIALS_URL = "https://api.twitter.com/1.1/account/verify_credentials.json";
    private static final String POST_TWEET_URL = "https://api.twitter.com/1.1/statuses/update.json";

    private static final int MAX_TWEET_LEN = 140;

    private static final String API_KEY = "apiKey";
    private static final String API_SECRET = "apiSecret";
    private static final String ACCESS_TOKEN = "accessToken";
    private static final String ACCESS_TOKEN_SECRET = "accessTokenSecret";

    private String apiKey;
    private String apiSecret;
    private String accessToken;
    private String accessTokenSecret;
    private OAuth10aService service;
    private OAuth1AccessToken oauthToken;

    /**
     * Create a Twitter Agent
     *
     * @param config  The configuration for the Twitter API
     */
    public TwitterAgent(Config config) {
        apiKey = config.getString(API_KEY);
        apiSecret = config.getString(API_SECRET);
        accessToken = config.getString(ACCESS_TOKEN);
        accessTokenSecret = config.getString(ACCESS_TOKEN_SECRET);
        service = new ServiceBuilder().apiKey(apiKey).apiSecret(apiSecret)
                                .build(TwitterApi.instance());
        oauthToken = new OAuth1AccessToken(accessToken, accessTokenSecret);
    }

    /**
     * Create a Twitter Agent
     *
     * @param service  The OAuth service
     * @param oauth  The OAuth token
     */
    public TwitterAgent(OAuth10aService service, OAuth1AccessToken oauth) {
        this.service = service;
        this.oauthToken = oauth;
    }

    /**
     * Process request
     *
     * @param agentRequest  The user's request
     * @return AgentResponse
     */
    public AgentResponse process(AgentRequest agentRequest) {
        try {
            verfiyAccountCredentials();
        } catch (TwitterException e) {
            return new AgentResponse(new ResponseStatus(ResponseStatus.Code.AGENT_INTERNAL_ERROR, "Failed to authenticate with Twitter"));
        }

        String tweetText = agentRequest.getText().trim();
        if (tweetText.toLowerCase().startsWith("tweet")) {
            tweetText = tweetText.replaceFirst("tweet", "").trim();
        }

        return postTweet(tweetText);
    }

    /**
     * Verify account credentials against the Twitter API
     */
    private void verfiyAccountCredentials() throws TwitterException {
        OAuthRequest verifyRequest = createOAuthRequest(Verb.GET, VERIFY_CREDENTIALS_URL);
        signRequest(verifyRequest);

        try {
            Response response = service.execute(verifyRequest);
            if (response.getMessage().toLowerCase().equals("Too Many Requests")) {
                throw new TwitterException("Too many requests for this account.");
            } else if (!response.getMessage().toLowerCase().equals("ok")) {
                throw new TwitterException("Invalid twitter configuration");
            }
        } catch (IOException | ExecutionException | InterruptedException e) {
            throw new TwitterException("Could not verify account credentials.");
        }
    }

    /**
     * Post a tweet to Twitter
     *
     * @return AgentResponse
     */
    private AgentResponse postTweet(String text) {
        if (text.length() > MAX_TWEET_LEN) {
            return new AgentResponse(new ResponseStatus(ResponseStatus.Code.AGENT_CANNOT_COMPLETE_ACTION,
                            "This text was too many characters."));
        } else if (text.isEmpty()) {
            return new AgentResponse(new ResponseStatus(ResponseStatus.Code.AGENT_CANNOT_COMPLETE_ACTION,
                            "No text to tweet."));
        }

        OAuthRequest request = createPostOAuthRequest(text);

        Response response;
        try {
            response = service.execute(request);
            if (response.getBody().contains("duplicate")) {
                return new AgentResponse(new ResponseStatus(ResponseStatus.Code.AGENT_INTERNAL_ERROR,
                                "This tweet was a duplicate and could not be posted"));
            }
        } catch (InterruptedException | ExecutionException | IOException e) {
            logger.error("Failed to post tweet", e);
            return new AgentResponse(new ResponseStatus(ResponseStatus.Code.AGENT_INTERNAL_ERROR,
                            "Failed to post tweet"));
        }

        return new AgentResponse("Tweeted " + text);
    }

    /**
     * Create an OAuth Request
     *
     * @param action  Verb.GET or Verb.POST
     * @param url  the URL the OAuth Request is sent to
     */
    private OAuthRequest createOAuthRequest(Verb action, String url) {
        return new OAuthRequest(action, url);
    }

    /**
     * Sign an OAuth Request
     */
    private void signRequest(OAuthRequest request) {
        service.signRequest(oauthToken, request);
    }

    /**
     * Create an OAuth request to post a tweet
     *
     * @param text  the text to be tweeted
     */
    private OAuthRequest createPostOAuthRequest(String text) {
        OAuthRequest request = createOAuthRequest(Verb.POST, POST_TWEET_URL);
        request.addBodyParameter("status", text);
        signRequest(request);
        return request;
    }

    public class TwitterException extends Exception {
        private static final long serialVersionUID = 1L;

        public TwitterException(String string) {
            super(string);
        }
    }
}
