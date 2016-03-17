package edu.jhuapl.dorset.filters;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import edu.jhuapl.dorset.Request;

public class AliasRequestFilterTest {

    @Test
    public void AliasRequestFilterOneAlias() {
        String strRequest = "What is the runtime for the film Finding Nemo?";
        String strFilteredRequest = "What is the runtime for the movie Finding Nemo?";

        Map<String, String> aliasMap = new HashMap<String, String>();

        aliasMap.put("movie", "film");

        RequestFilter requestFilter = new AliasRequestFilter(aliasMap);

        Request request = new Request(strRequest);
        request = requestFilter.filter(request);
        assertEquals(strFilteredRequest, request.getText());

    }

    @Test
    public void AliasRequestFilterTwoAliases() {
        String strRequest = "What is the total time for the film Finding Nemo?";
        String strFilteredRequest = "What is the runtime for the movie Finding Nemo?";

        Map<String, String> aliasMap = new HashMap<String, String>();

        aliasMap.put("movie", "film");
        aliasMap.put("runtime", "total time");

        RequestFilter requestFilter = new AliasRequestFilter(aliasMap);

        Request request = new Request(strRequest);
        request = requestFilter.filter(request);
        assertEquals(strFilteredRequest, request.getText());

    }

    @Test
    public void AliasRequestFilterNullAliases() {
        String strRequest = "What is the total time for the film Finding Nemo?";

        Map<String, String> aliasMap = null;

        RequestFilter requestFilter = new AliasRequestFilter(aliasMap);

        Request request = new Request(strRequest);
        request = requestFilter.filter(request);
        assertEquals(strRequest, request.getText());

    }
}
