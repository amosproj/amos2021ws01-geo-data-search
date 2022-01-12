package com.example.backend.data.here;

public class HereRoutingAttributes {

    private static boolean includeChargingStations = false;
    private static boolean avoidTollRoads = true;
    private final static String DELIMITER = "&";
    private String returnType = "";

    public HereRoutingAttributes() {
        // empty constructor
    }

    public void includeChargingStations() {
        includeChargingStations = true;
    }

    public void excludeChargingStations() {
        includeChargingStations = false;
    }

    public void avoidToll() {
        avoidTollRoads = true;
    }

    public void includeTollRoads() {
        avoidTollRoads = false;
    }

    public String getUrlArguments(boolean guidance) {
        String url_query_attributes = "";
        if (includeChargingStations) {
            url_query_attributes += "ev[connectorTypes]=iec62196Type2Combo" + DELIMITER +
                    "ev[freeFlowSpeedTable]=0,0.239,27,0.239,45,0.259,60,0.196,75,0.207,90,0.238,100,0.26,110,0.296,120,0.337,130,0.351,250,0.351" + DELIMITER +
                    "ev[trafficSpeedTable]=0,0.349,27,0.319,45,0.329,60,0.266,75,0.287,90,0.318,100,0.33,110,0.335,120,0.35,130,0.36,250,0.36" + DELIMITER +
                    "ev[auxiliaryConsumption]=1.8" + DELIMITER +
                    "ev[ascent]=9" + DELIMITER +
                    "ev[descent]=4.3" + DELIMITER +
                    "ev[makeReachable]=true" + DELIMITER +
                    "ev[initialCharge]=48" + DELIMITER +
                    "ev[maxCharge]=80" + DELIMITER +
                    "ev[chargingCurve]=0,239,32,199,56,167,60,130,64,111,68,83,72,55,76,33,78,17,80,1" + DELIMITER +
                    "ev[maxChargeAfterChargingStation]=72" + DELIMITER;
        }
        if (avoidTollRoads && guidance) {
            url_query_attributes += "avoid[features]=tollRoad" + DELIMITER;
        }
        url_query_attributes += "return=" + returnType + DELIMITER;
        return url_query_attributes;
    }

    public void setReturnTypeToSummary() {
        returnType = "summary";
    }

    public void setReturnTypeToPolylineAndTurnByTurnActions() {
        returnType = "polyline,turnbyturnactions";
    }
}