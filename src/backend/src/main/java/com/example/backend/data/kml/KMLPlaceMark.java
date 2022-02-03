package com.example.backend.data.kml;

import com.example.backend.data.ApiResult;
import org.jdom2.Element;

import java.util.List;

public class KMLPlaceMark extends KML {

    private KMLPlaceMark(Builder builder) {
        super(builder);
        setPlaceMarks(builder.getPlaceMarks());
    }

    public static class Builder extends KML.Builder {
        public Builder() {
            super();
        }

        public Builder addPlaceMark(PlaceMark placeMarkObj) {
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
            return this;
        }

        @Override
        public KML build() {
            return new KMLPlaceMark(this);
        }

        @Override
        protected KML forRoute(List<ApiResult> apiResultList) {
            return null;
        }

        @Override
        protected KML forElevation(List<ApiResult> apiResultList) {
            for (ApiResult apiResult : apiResultList) {
                PlaceMark placeMark = new PlaceMark(apiResult.getName(), apiResult.getType(), apiResult.getLat(), apiResult.getLon());
                addPlaceMark(placeMark);
            }
            return this.build();
        }
    }
}
