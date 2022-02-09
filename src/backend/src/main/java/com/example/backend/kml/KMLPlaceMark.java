package com.example.backend.kml;

import com.example.backend.api.ApiResult;
import org.jdom2.Element;

import java.util.List;

public class KMLPlaceMark extends KML {

    private KMLPlaceMark(Builder builder) {
        super(builder);
    }

    public static class Builder extends KML.Builder {
        public Builder() {
            super();
        }

        public void addPlaceMark(PlaceMark placeMarkObj) {
            getPlaceMarks().add(placeMarkObj);
            Element placeMarkElement = new Element("Placemark", getNamespace());
            Element nameElement = new Element("name", getNamespace()).setText(placeMarkObj.getName());
            placeMarkElement.addContent(nameElement);

            Element descriptionElement = new Element("description", getNamespace()).setText(placeMarkObj.getDescription());
            placeMarkElement.addContent(descriptionElement);

            Element point = new Element("Point", getNamespace());
            Element coordinates = new Element("coordinates", getNamespace());
            coordinates.setText(placeMarkObj.getCoordinates());
            point.addContent(coordinates);

            placeMarkElement.addContent(point);
            getRootDoc().addContent(placeMarkElement);
        }

        @Override
        public KML build() {
            return new KMLPlaceMark(this);
        }

        @Override
        protected void forRoute(List<ApiResult> apiResultList) {
        }

        @Override
        protected void forElevation(List<ApiResult> apiResultList) {
            for (ApiResult apiResult : apiResultList) {
                PlaceMark placeMark = new PlaceMark(apiResult.getName(), apiResult.getType(), apiResult.getLat(), apiResult.getLon());
                addPlaceMark(placeMark);
            }
            this.build();
        }
    }
}