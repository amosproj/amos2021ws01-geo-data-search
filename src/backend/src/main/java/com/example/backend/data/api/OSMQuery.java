package com.example.backend.data.api;

public class OSMQuery {
    private String outType = "json";
    private int timeOut = 10;
    private String area;
    private String amenity;
    private int resultCount = 10;
    private boolean isDetailed = true;

    public String toQuery() {
        StringBuilder builder = new StringBuilder()
                .append("[out:" + outType + "]")
                .append("[timeout:" + timeOut + "];\n");
        if (area != null) builder.append("area[name=\"" + area + "\"]->.searchArea;\n");
        builder.append("(\n")
                .append("  node[\"amenity\"=\"" + amenity + "\"](area.searchArea);\n")
                .append("  way[\"amenity\"=\"" + amenity + "\"](area.searchArea);\n")
                .append("  relation[\"amenity\"=\"" + amenity + "\"](area.searchArea);\n")
                .append(");\n");
        if (isDetailed) builder.append("out body center " + resultCount + ";\n");
        else builder.append("out sqel center" + resultCount + ";\n");
        return builder.toString();
    }

    public String getOutType() {
        return outType;
    }

    public void setOutType(String outType) {
        this.outType = outType;
    }

    public int getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(int timeOut) {
        this.timeOut = timeOut;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAmenity() {
        return amenity;
    }

    public void setAmenity(String amenity) {
        this.amenity = amenity;
    }

    public int getResultCount() {
        return resultCount;
    }

    public void setResultCount(int resultCount) {
        this.resultCount = resultCount;
    }

    public boolean isDetailed() {
        return isDetailed;
    }

    public void setDetailed(boolean detailed) {
        isDetailed = detailed;
    }
}
