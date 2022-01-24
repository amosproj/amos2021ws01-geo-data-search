package com.example.backend.data.api;

import com.example.backend.data.nlp.Height;

public class OSMQuery {
    private String outType = "json";
    private int timeOut = 10;
    private String area;
    private String amenity;
    private String natural;
    private Height height = null;
    private int resultCount = 10;
    private boolean isDetailed = true;

    public String toQuery() {
        StringBuilder builder = new StringBuilder()
                .append("[out:" + outType + "]")
                .append("[timeout:" + timeOut + "];\n");
        if (area != null) builder.append("area[name=\"" + area + "\"]->.searchArea;\n");
        String key = null;
        String value = null;
        if (amenity != null) {
            key = "amenity";
            value = amenity;
        } else if (natural != null) {
            key = "natural";
            value = "peak";
        }
        builder.append("(\n")
                .append("  node[\""+key+"\"=\"" + value + "\"](area.searchArea)");

        // consider elevation height restrictions
        if (natural != null && height != null){
            builder.append("(if:number(t[\"ele\"])>" + height.getMin() + " && number(t[\"ele\"])<" + height.getMax() + ")");
        }

        builder.append(";\n")  // line ending from node
                .append("  way[\""+key+"\"=\"" + value + "\"](area.searchArea);\n")
                .append("  relation[\""+key+"\"=\"" + value + "\"](area.searchArea);\n")
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
        this.area = area.substring(0, 1).toUpperCase() + area.substring(1);
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

    public String getNatural() {
        return natural;
    }

    public void setNatural(String natural) {
        this.natural = natural;
    }

    public void setHeight(Height height) {
        Integer min = height.getMin();
        Integer max = height.getMax();

        if (height.getMax() == 0)
            // there is no elevation higher than 9.000m
            max = 9000;

        // make sure that minimum height is smaller than maximum height
        else if (height.getMax() < height.getMin()) {
            Integer temp = max;
            max = min;
            min = temp;
        }
        this.height = new Height(min, max);
    }

    public Height getHeight() {
        return height;
    }
}
