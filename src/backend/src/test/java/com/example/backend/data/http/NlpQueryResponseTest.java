package com.example.backend.data.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class NlpQueryResponseTest {

    private final String expectedLocation = "" + new Random().nextInt();
    private final Integer expectedMaxDistance = new Random().nextInt();
    private final String expectedQueryObject = "" + new Random().nextInt();
    private final Integer expectedRouteHeightMin = new Random().nextInt();
    private final Integer expectedRouteHeightMax = new Random().nextInt();
    private final Integer expectedRouteLengthMin = new Random().nextInt();
    private final Integer expectedRouteLengthMax = new Random().nextInt();
    private final Integer expectedRouteGradiantMin = new Random().nextInt();
    private final Integer expectedRouteGradiantMax = new Random().nextInt();
    private final Integer expectedRouteGradiantLength = new Random().nextInt();
    private final Integer expectedRouteCurvesMin = new Random().nextInt();
    private final Integer expectedRouteCurvesMax = new Random().nextInt();
    private final Integer expectedRouteCurvesCount = new Random().nextInt();
    private final Integer expectedRouteCurvesLeftMin = new Random().nextInt();
    private final Integer expectedRouteCurvesLeftMax = new Random().nextInt();
    private final Integer expectedRouteCurvesLeftCount = new Random().nextInt();
    private final Integer expectedRouteCurvesRightMin = new Random().nextInt();
    private final Integer expectedRouteCurvesRightMax = new Random().nextInt();
    private final Integer expectedRouteCurvesRightCount = new Random().nextInt();

    @Test
    public void test_correctExtractionAndStructureOfNlpResponse() {

        // arrange
        String nlpAnswerString = buildNlpAnswerString();

        // act
        Gson gson = new GsonBuilder().serializeNulls().create();
        NlpQueryResponse sut = gson.fromJson(nlpAnswerString, NlpQueryResponse.class);

        // assert
        // this is not a good style to put more than one assertion into one test, but it should be enough for our purposes
        assertThat(sut.getLocation(), is(expectedLocation));
        assertThat(sut.getMaxDistance(), is(expectedMaxDistance));
        assertThat(sut.getQueryObject(), is(expectedQueryObject));
        assertThat(sut.getRouteAttributes().getHeight().getMin(), is(expectedRouteHeightMin));
        assertThat(sut.getRouteAttributes().getHeight().getMax(), is(expectedRouteHeightMax));
        assertThat(sut.getRouteAttributes().getLength().getMin(), is(expectedRouteLengthMin));
        assertThat(sut.getRouteAttributes().getLength().getMax(), is(expectedRouteLengthMax));
        assertThat(sut.getRouteAttributes().getGradient().getMin(), is(expectedRouteGradiantMin));
        assertThat(sut.getRouteAttributes().getGradient().getMax(), is(expectedRouteGradiantMax));
        assertThat(sut.getRouteAttributes().getGradient().getLength(), is(expectedRouteGradiantLength));
        assertThat(sut.getRouteAttributes().getCurves().getMin(), is(expectedRouteCurvesMin));
        assertThat(sut.getRouteAttributes().getCurves().getMax(), is(expectedRouteCurvesMax));
        assertThat(sut.getRouteAttributes().getCurves().getCount(), is(expectedRouteCurvesCount));
        assertThat(sut.getRouteAttributes().getCurves().getLeft().getMin(), is(expectedRouteCurvesLeftMin));
        assertThat(sut.getRouteAttributes().getCurves().getLeft().getMax(), is(expectedRouteCurvesLeftMax));
        assertThat(sut.getRouteAttributes().getCurves().getLeft().getCount(), is(expectedRouteCurvesLeftCount));
        assertThat(sut.getRouteAttributes().getCurves().getRight().getMin(), is(expectedRouteCurvesRightMin));
        assertThat(sut.getRouteAttributes().getCurves().getRight().getMax(), is(expectedRouteCurvesRightMax));
        assertThat(sut.getRouteAttributes().getCurves().getRight().getCount(), is(expectedRouteCurvesRightCount));
    }

    private String buildNlpAnswerString() {
        String nlpAnswerAsString = "";
        try {
            JSONObject routeHeight = new JSONObject()
                    .put("min", expectedRouteHeightMin)
                    .put("max", expectedRouteHeightMax);
            JSONObject routeLength = new JSONObject()
                    .put("min", expectedRouteLengthMin)
                    .put("max", expectedRouteLengthMax);
            JSONObject routeGradiant = new JSONObject()
                    .put("min", expectedRouteGradiantMin)
                    .put("max", expectedRouteGradiantMax)
                    .put("length", expectedRouteGradiantLength);
            JSONObject routeCurvesLeft = new JSONObject()
                    .put("min", expectedRouteCurvesLeftMin)
                    .put("max", expectedRouteCurvesLeftMax)
                    .put("count", expectedRouteCurvesLeftCount);
            JSONObject routeCurvesRight = new JSONObject()
                    .put("min", expectedRouteCurvesRightMin)
                    .put("max", expectedRouteCurvesRightMax)
                    .put("count", expectedRouteCurvesRightCount);
            JSONObject routeCurves = new JSONObject()
                    .put("min", expectedRouteCurvesMin)
                    .put("max", expectedRouteCurvesMax)
                    .put("count", expectedRouteCurvesCount)
                    .put("left", routeCurvesLeft)
                    .put("right", routeCurvesRight);
            JSONObject route = new JSONObject()
                    .put("height", routeHeight)
                    .put("length", routeLength)
                    .put("gradiant", routeGradiant)
                    .put("curves", routeCurves);
            JSONObject nlpAnswerAsJson = new JSONObject()
                    .put("location", expectedLocation)
                    .put("max_distance", expectedMaxDistance)
                    .put("query_object", expectedQueryObject)
                    .put("route_attributes", route);
            nlpAnswerAsString = nlpAnswerAsJson.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return nlpAnswerAsString;
    }
}