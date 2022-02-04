package com.example.backend.data.kml;

import com.example.backend.controllers.HereApiRestService;
import com.example.backend.data.ApiResult;
import org.jdom2.Element;

import java.util.List;

public class KMLRoute extends KML {

    private KMLRoute(Builder builder) {
        super(builder);
    }

    public static class Builder extends KML.Builder {
        private PlaceMark startingPoint;
        private PlaceMark targetPoint;
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
            findStartTargetPoints(apiResultList);
            //Add Start Point
            addStartingPoint(startingPoint);
            //Add Target Point
            addTargetPoint(targetPoint);
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
        private void findStartTargetPoints(List<ApiResult> apiResultList) {
            for (ApiResult apiResult : apiResultList) {
                if (apiResult.getType().equalsIgnoreCase(HereApiRestService.TYPE_START)) {
                    startingPoint = new PlaceMark(HereApiRestService.TYPE_START, null, apiResult.getLat(), apiResult.getLon());
                } else if (apiResult.getType().equalsIgnoreCase(HereApiRestService.TYPE_FINISH)) {
                    targetPoint = new PlaceMark(HereApiRestService.TYPE_FINISH, null, apiResult.getLat(), apiResult.getLon());
                }
            }
        }

        /**
         * gets the polyline object, decodes and creates the coordinate string from it
         *
         * @param apiResultList result list
         */
        private void findRoute(List<ApiResult> apiResultList) {
            String polyLine = apiResultList.get(0).getPolyline();
            //Need to add @ at the end of the polyline, it is missing in the here api
            List<PolylineEncoderDecoder.LatLngZ> decodedPath = PolylineEncoderDecoder.decode(polyLine);

            StringBuilder sBuilder = new StringBuilder();
            for (PolylineEncoderDecoder.LatLngZ path : decodedPath) {
                sBuilder.append(path.lng).append(",").append(path.lat).append(" ");
            }
            coordinatesString = sBuilder.toString();
        }
    }
}