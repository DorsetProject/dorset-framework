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

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

import edu.jhuapl.dorset.ResponseStatus;
import edu.jhuapl.dorset.agents.Agent;
import edu.jhuapl.dorset.agents.AgentRequest;
import edu.jhuapl.dorset.agents.AgentResponse;
import edu.jhuapl.dorset.http.HttpClient;

public class RottenTomatoesAgentTest {

    private String apikey = "default_apikey";

    @Test
    public void testRuntime() {
        String jsonData = FileReader.getFileAsString("rotten_tomatoes/test.json");

        String movieTitle = "finding%20nemo";
        String urlStr = "http://api.rottentomatoes.com/api/public/v1.0/movies.json?apikey="
                + apikey + "&q=" + movieTitle;

        String testQuestion = "What is the runtime for the film finding nemo?";
        String correctAnswer = "The runtime for the film, Finding Nemo, is 100 minutes long.";

        HttpClient client = mock(HttpClient.class);
        when(client.get(urlStr)).thenReturn(jsonData);

        Agent movie = new RottenTomatoesAgent(client, apikey);
        AgentRequest request = new AgentRequest(testQuestion);
        AgentResponse response = movie.process(request);

        assertEquals(correctAnswer, response.getText());
    }

    @Test
    public void testYear() {
        String jsonData = FileReader.getFileAsString("rotten_tomatoes/test.json");

        String movieTitle = "finding%20nemo";
        String urlStr = "http://api.rottentomatoes.com/api/public/v1.0/movies.json?apikey="
                + apikey + "&q=" + movieTitle;

        String testQuestion = "What is the creation year for the film finding nemo?";
        String correctAnswer = "The year the film, Finding Nemo, was created is 2003.";

        HttpClient client = mock(HttpClient.class);
        when(client.get(urlStr)).thenReturn(jsonData);

        Agent movie = new RottenTomatoesAgent(client, apikey);
        AgentRequest request = new AgentRequest(testQuestion);
        AgentResponse response = movie.process(request);

        assertEquals(correctAnswer, response.getText());
    }

    @Test
    public void testActors() {
        String jsonData = FileReader.getFileAsString("rotten_tomatoes/test.json");

        String movieTitle = "finding%20nemo";
        String urlStr = "http://api.rottentomatoes.com/api/public/v1.0/movies.json?apikey="
                + apikey + "&q=" + movieTitle;

        String testQuestion = "Who are the main actors in the movie finding nemo?";
        String correctAnswer = "The film, Finding Nemo, stars actors Albert Brooks, "
                + "Ellen DeGeneres, Alexander Gould, Willem Dafoe, Brad Garrett.";

        HttpClient client = mock(HttpClient.class);
        when(client.get(urlStr)).thenReturn(jsonData);

        Agent movie = new RottenTomatoesAgent(client, apikey);
        AgentRequest request = new AgentRequest(testQuestion);
        AgentResponse response = movie.process(request);

        assertEquals(correctAnswer, response.getText());
    }

    @Test
    public void testNoKeyword() {
        String jsonData = FileReader.getFileAsString("rotten_tomatoes/test.json");

        String movieTitle = "finding%20nemo";
        String urlStr = "http://api.rottentomatoes.com/api/public/v1.0/movies.json?apikey="
                + apikey + "&q=" + movieTitle;

        String testQuestion = "What is the  movie finding nemo?";

        HttpClient client = mock(HttpClient.class);
        when(client.get(urlStr)).thenReturn(jsonData);

        Agent movie = new RottenTomatoesAgent(client, apikey);
        AgentRequest request = new AgentRequest(testQuestion);
        AgentResponse response = movie.process(request);

        assertFalse(response.isSuccess());        
        assertEquals(ResponseStatus.Code.AGENT_DID_NOT_UNDERSTAND_REQUEST, response.getStatus().getCode());
    }

    @Test
    public void testBadApiKey() {
        String jsonData = FileReader.getFileAsString("rotten_tomatoes/test.json");

        String movieTitle = "finding%20nemo";
        String urlStr = "http://api.rottentomatoes.com/api/public/v1.0/movies.json?apikey="
                + "otherapi_key" + "&q=" + movieTitle;

        String testQuestion = "What is the runtime for the movie finding nemo?";

        HttpClient client = mock(HttpClient.class);
        when(client.get(urlStr)).thenReturn(jsonData);

        Agent movie = new RottenTomatoesAgent(client, apikey);
        AgentRequest request = new AgentRequest(testQuestion);
        AgentResponse response = movie.process(request);

        assertFalse(response.isSuccess());        
        assertEquals(ResponseStatus.Code.AGENT_INTERNAL_ERROR, response.getStatus().getCode());
    }

}
