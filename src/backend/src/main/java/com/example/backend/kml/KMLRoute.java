package com.example.backend.kml;

import com.example.backend.clients.HereApiRestService;
import com.example.backend.api.ApiResult;
import org.jdom2.Element;

import java.util.ArrayList;
import java.util.List;

public class KMLRoute extends KML {

    private KMLRoute(Builder builder) {
        super(builder);
    }

    public static class Builder extends KML.Builder {
        private PlaceMark startingPoint;
        private PlaceMark targetPoint;
        private List<String> polylineStrings = new ArrayList<>();
        private String coordinatesString;

        public Builder() {
            super();
        }

        public void addStartingPoint(PlaceMark point) {
            addPlaceMark(point);
        }

        public void addTargetPoint(PlaceMark point) {
            addPlaceMark(point);
        }

        private void addRoute(String listOfCoordinates) {
            Element placeMarkElement = new Element("Placemark", getNamespace());
            Element nameElement = new Element("name", getNamespace()).setText("Route");
            placeMarkElement.addContent(nameElement);

            Element lineString = new Element("LineString", getNamespace());
            Element coordinates = new Element("coordinates", getNamespace());
            coordinates.setText(listOfCoordinates);
            lineString.addContent(coordinates);

            placeMarkElement.addContent(lineString);
            getRootDoc().addContent(placeMarkElement);
        }

        private void addPlaceMark(PlaceMark placeMark) {
            Element placeMarkElement = new Element("Placemark", getNamespace());
            Element nameElement = new Element("name", getNamespace()).setText(placeMark.getName());
            placeMarkElement.addContent(nameElement);

            Element point = new Element("Point", getNamespace());
            Element coordinates = new Element("coordinates", getNamespace());
            coordinates.setText(placeMark.getCoordinates());
            point.addContent(coordinates);

            placeMarkElement.addContent(point);
            getRootDoc().addContent(placeMarkElement);
        }

        @Override
        public KML build() {
            return new KMLRoute(this);
        }

        /**
         * adds Start, Target PlaceMarkers and add the route to the KML
         *
         * @param apiResultList result list
         */
        @Override
        protected void forRoute(List<ApiResult> apiResultList) {
            findAndAddPlacemarks(apiResultList);
            //Add Route
            findRoute(apiResultList);
            addRoute(coordinatesString);
            this.build();
        }

        @Override
        protected void forElevation(List<ApiResult> apiResultList) {
        }

        /**
         * finds and initializes the starting and target PlaceMark
         *
         * @param apiResultList result list
         */
        private void findAndAddPlacemarks(List<ApiResult> apiResultList) {
            for (ApiResult apiResult : apiResultList) {
                if (apiResult.getType().equalsIgnoreCase(HereApiRestService.TYPE_START)) {
                    //Add Start Point
                    startingPoint = new PlaceMark(HereApiRestService.TYPE_START, null, apiResult.getLat(), apiResult.getLon());
                    addStartingPoint(startingPoint);
                } else if (apiResult.getType().equalsIgnoreCase(HereApiRestService.TYPE_FINISH)) {
                    //Add Target Point
                    targetPoint = new PlaceMark(HereApiRestService.TYPE_FINISH, null, apiResult.getLat(), apiResult.getLon());
                    addTargetPoint(targetPoint);
                } else {
                    //Add remaining points (for example: Charging Stations)
                    PlaceMark point = new PlaceMark(apiResult.getName(), apiResult.getType(), apiResult.getLat(), apiResult.getLon());
                    addPlaceMark(point);
                }
            }
        }

        /**
         * gets the polyline object, decodes and creates the coordinate string from it
         *
         * @param apiResultList result list
         */
        private void findRoute(List<ApiResult> apiResultList) {
            for (ApiResult apiResult : apiResultList) {
                if (apiResult.getPolyline() != null && !apiResult.getPolyline().isBlank()) {
                    polylineStrings.add(apiResult.getPolyline());
                }
            }
            //Need to add @ at the end of the polyline, it is missing in the here api
            List<PolylineEncoderDecoder.LatLngZ> decodedPath = new ArrayList<>();
            for (String polyLine : polylineStrings) {
                decodedPath.addAll(PolylineEncoderDecoder.decode(polyLine));
            }

            StringBuilder sBuilder = new StringBuilder();
            for (PolylineEncoderDecoder.LatLngZ path : decodedPath) {
                sBuilder.append(path.lng).append(",").append(path.lat).append(" ");
            }
            coordinatesString = sBuilder.toString();
        }
    }
}