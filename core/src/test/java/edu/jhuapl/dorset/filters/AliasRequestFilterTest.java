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
        aliasMap.put("film", "movie");

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
        aliasMap.put("film", "movie");
        aliasMap.put("total time", "runtime");

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

    @Test
    public void AliasRequestFilterRegex() {
        String strRequest = "Is the film \"Abe Lincoln: Vampire Slayer\" playing in Va?";
        String strFilteredRequest = "Is the film \"Abe Lincoln: Vampire Slayer\" playing in Virginia?";

        Map<String, String> aliasMap = new HashMap<String, String>();
        aliasMap.put("Va", "Virginia");

        RequestFilter requestFilter = new AliasRequestFilter(aliasMap);

        Request request = new Request(strRequest);
        request = requestFilter.filter(request);
        assertEquals(strFilteredRequest, request.getText());

    }

}
