package com.example.backend.data.here;

import com.example.backend.controllers.HereApiRestService;
import com.example.backend.data.api.HereApiRoutingResponse;
import com.example.backend.data.nlp.RouteAttributes;
import com.example.backend.helpers.InvalidCalculationRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RouteAttributesCalculator {

    private static final String LOG_PREFIX = "ROUTE_ATTRIBUTE_CALCULATOR";
    private static final double ONE_THOUSAND_METERS_IN_COORD = 0.000015060;
    private static final int MAX_RECURSIONS = 6;
    private int recursionStepsInMeters = 500;
    private final HereApiRestService hereApiRestService;
    private RoutingWaypoint origin;
    private int minimumMeters, maximumMeters;
    private final Logger logger = LogManager.getLogger(this.getClass());

    public RouteAttributesCalculator(HereApiRestService hereApiRestService) {
        this.hereApiRestService = hereApiRestService;
    }

    public RoutingWaypoint getDestinationOnGivenRouteAttributes(RoutingWaypoint origin, RouteAttributes routeAttributes) throws InvalidCalculationRequest {
        this.origin = origin;
        minimumMeters = routeAttributes.getLength().getMin();
        maximumMeters = routeAttributes.getLength().getMax();

        if (minimumMeters > maximumMeters && maximumMeters != 0) {
            throw new InvalidCalculationRequest("Minimum (" + minimumMeters + ") cannot be greater than maximum (" + maximumMeters + ")!");
        }
        int recursionCounter = 0;
        if (minimumMeters > 0) {
            recursionStepsInMeters = (int) (minimumMeters * 0.1);
            return calculateMinimumRoute(minimumMeters, recursionCounter);
        }
        recursionCounter = 0;
        if (maximumMeters > 0) {
            recursionStepsInMeters = (int) (maximumMeters * 0.1);
            return calculateMaximumRoute(maximumMeters, recursionCounter);
        }
        throw new InvalidCalculationRequest("Minimum=" + minimumMeters + " and Maximum=" + maximumMeters + "! This is an invalid calculation request!");
    }

    private RoutingWaypoint calculateMaximumRoute(double localRequestedDistanceInMeters, int recursionCounter) throws InvalidCalculationRequest {
        double localRequestedDistanceInCoordinates = localRequestedDistanceInMeters * ONE_THOUSAND_METERS_IN_COORD;
        logger.info("We will try to calculate a route with maximum length = " + localRequestedDistanceInMeters + "m (" + localRequestedDistanceInCoordinates + " in coordinates)...");
        List<Route> potentialRoutes = tryInAllDirections(localRequestedDistanceInCoordinates);
        for (Route route : potentialRoutes) {
            for (Section section : route.sections) {
                if (section.summary.length <= maximumMeters) {
                    logger.info(section.summary.length + " <= " + maximumMeters);
                    return createRoutingWaypoint(section);
                }
            }
        }
        if (recursionCounter < MAX_RECURSIONS) {
            recursionCounter++;
            return calculateMaximumRoute(localRequestedDistanceInMeters - recursionStepsInMeters, recursionCounter);
        } else {
            throw new InvalidCalculationRequest("Could not find a route with this maximum length: " + localRequestedDistanceInMeters);
        }
    }

    private RoutingWaypoint calculateMinimumRoute(double localRequestedDistanceInMeters, int recursionCounter) throws InvalidCalculationRequest {
        double localRequestedDistanceInCoordinates = localRequestedDistanceInMeters * ONE_THOUSAND_METERS_IN_COORD;
        logger.info("We will try to calculate a route with minimum length = " + localRequestedDistanceInMeters + "m (" + localRequestedDistanceInCoordinates + " in coordinates)...");
        List<Route> potentialRoutes = tryInAllDirections(localRequestedDistanceInCoordinates);
        for (Route route : potentialRoutes) {
            for (Section section : route.sections) {
                if (section.summary.length >= minimumMeters) {
                    logger.info(section.summary.length + " >= " + minimumMeters);
                    return createRoutingWaypoint(section);
                }
            }
        }
        if (recursionCounter < MAX_RECURSIONS) {
            recursionCounter++;
            return calculateMinimumRoute(localRequestedDistanceInMeters + recursionStepsInMeters, recursionCounter);
        } else {
            throw new InvalidCalculationRequest("Could not find a route with this minimum length: " + localRequestedDistanceInMeters);
        }
    }

    private List<Route> tryInAllDirections(double localRequestedDistanceInKm) {
        List<Route> potentialRoutes = new LinkedList<>();
        potentialRoutes.addAll(tryNorth(localRequestedDistanceInKm));
        potentialRoutes.addAll(tryEast(localRequestedDistanceInKm));
        potentialRoutes.addAll(trySouth(localRequestedDistanceInKm));
        potentialRoutes.addAll(tryWest(localRequestedDistanceInKm));
        return potentialRoutes;
    }

    private List<Route> tryNorth(double localRequestedDistanceInCoordinates) {
        RoutingWaypoint tempDestination = new RoutingWaypoint("point_north", origin.getLatitude() + localRequestedDistanceInCoordinates, origin.getLongitude());
        HereApiRoutingResponse hereApiRoutingResponse = hereApiRestService.getRoute(origin, tempDestination);
        logger.info("NORTH: origin=(" + origin.getCoordinatesAsString() + "), length=" + hereApiRoutingResponse.routes.get(0).sections.get(0).summary.length + ", lat=" + hereApiRoutingResponse.routes.get(0).sections.get(0).arrival.place.location.lat + ", lng=" + hereApiRoutingResponse.routes.get(0).sections.get(0).arrival.place.location.lng);
        return hereApiRoutingResponse.routes;
    }

    private List<Route> tryEast(double localRequestedDistanceInCoordinates) {
        RoutingWaypoint tempDestination = new RoutingWaypoint("point_east", origin.getLatitude(), origin.getLongitude() + localRequestedDistanceInCoordinates);
        HereApiRoutingResponse hereApiRoutingResponse = hereApiRestService.getRoute(origin, tempDestination);
        logger.info("EAST: origin=(" + origin.getCoordinatesAsString() + "),length=" + hereApiRoutingResponse.routes.get(0).sections.get(0).summary.length + ", lat=" + hereApiRoutingResponse.routes.get(0).sections.get(0).arrival.place.location.lat + ", lng=" + hereApiRoutingResponse.routes.get(0).sections.get(0).arrival.place.location.lng);
        return hereApiRoutingResponse.routes;
    }

    private List<Route> trySouth(double localRequestedDistanceInCoordinates) {
        RoutingWaypoint tempDestination = new RoutingWaypoint("point_south", origin.getLatitude() - localRequestedDistanceInCoordinates, origin.getLongitude());
        HereApiRoutingResponse hereApiRoutingResponse = hereApiRestService.getRoute(origin, tempDestination);
        logger.info("SOUTH: origin=(" + origin.getCoordinatesAsString() + "),length=" + hereApiRoutingResponse.routes.get(0).sections.get(0).summary.length + ", lat=" + hereApiRoutingResponse.routes.get(0).sections.get(0).arrival.place.location.lat + ", lng=" + hereApiRoutingResponse.routes.get(0).sections.get(0).arrival.place.location.lng);
        return hereApiRoutingResponse.routes;
    }

    private List<Route> tryWest(double localRequestedDistanceInCoordinates) {
        RoutingWaypoint tempDestination = new RoutingWaypoint("point_west", origin.getLatitude(), origin.getLongitude() - localRequestedDistanceInCoordinates);
        HereApiRoutingResponse hereApiRoutingResponse = hereApiRestService.getRoute(origin, tempDestination);
        logger.info("WEST: origin=(" + origin.getCoordinatesAsString() + "),length=" + hereApiRoutingResponse.routes.get(0).sections.get(0).summary.length + ", lat=" + hereApiRoutingResponse.routes.get(0).sections.get(0).arrival.place.location.lat + ", lng=" + hereApiRoutingResponse.routes.get(0).sections.get(0).arrival.place.location.lng);
        return hereApiRoutingResponse.routes;
    }

    @SuppressWarnings("UnnecessaryLocalVariable")
    private RoutingWaypoint returnRandomWaypointInSpecifiedRange(double localRequestDistanceInCoordinates) {
        double randomAngle = ThreadLocalRandom.current().nextDouble(0.0, 360.0);
        // Fall 1: Winkel 0-90
        if (randomAngle > 270.0) {
            randomAngle -= 270.0;

        } else if (randomAngle > 180.0) {
            randomAngle -= 180.0;

        } else if (randomAngle > 90.0) {
            randomAngle -= 90.0;

        } else {

        }
        // Fall 2: Winkel 91-180
        // Fall 3: Winkel 181-270
        // Fall 4: Winkel else

        double hypotenuseLine = localRequestDistanceInCoordinates;
        double adjacentLine = hypotenuseLine * Math.cos(randomAngle); // lng
        double oppositeLine = hypotenuseLine * Math.sin(randomAngle); // lat
        RoutingWaypoint result = new RoutingWaypoint("randomPoint(angle=" + randomAngle + ")", origin.getLatitude() + adjacentLine, origin.getLongitude() + oppositeLine);
        logger.info("RANDOM WAYPOINT: " + result.getName() + " with (" + result.getCoordinatesAsString() + ")");
        return result;
    }

    private RoutingWaypoint createRoutingWaypoint(Section section) {
        double lng = section.arrival.place.location.lng;
        double lat = section.arrival.place.location.lat;
        String name = section.arrival.place.type;
        return new RoutingWaypoint(name, lat, lng);
    }
}