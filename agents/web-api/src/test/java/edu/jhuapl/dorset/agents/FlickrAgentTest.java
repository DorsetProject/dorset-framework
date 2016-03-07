package edu.jhuapl.dorset.agents;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

import edu.jhuapl.dorset.agent.Agent;
import edu.jhuapl.dorset.agent.AgentRequest;
import edu.jhuapl.dorset.agent.AgentResponse;
import edu.jhuapl.dorset.http.HttpClient;

public class FlickrAgentTest {

    @Test
    public void testGoodResponse() {        
        String query = "Show me an apple";
        String jsonData = FileReader.getFileAsString("flickr/apple.json");
        HttpClient client = mock(HttpClient.class);
        when(client.get((String)anyObject())).thenReturn(jsonData);

        Agent agent = new FlickrAgent(client, "key");
        AgentResponse response = agent.process(new AgentRequest(query));
    }

}
