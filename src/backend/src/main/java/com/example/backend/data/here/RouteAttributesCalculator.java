package com.example.backend.data.here;

import com.example.backend.controllers.HereApiRestService;
import com.example.backend.data.nlp.RouteAttributes;
import com.example.backend.helpers.InvalidCalculationRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RouteAttributesCalculator {

    // Determines how many times we adjust the given distance before aborting the search:
    private static final int MAX_RECURSIONS = 6;

    // Determines how many random points we are looking for per given distance:
    private static final int MAX_RANDOM_POINTS_PER_DISTANCE = 3;

    private static final double ONE_THOUSAND_METERS_IN_COORD = 0.000015060;
    private int recursionStepsInMeters = 500;
    private final HereApiRestService hereApiRestService;
    private RoutingWaypoint origin;
    private int minimumMeters, maximumMeters;
    private final Logger logger = LogManager.getLogger("ROUTE_ATTRIBUTES_CALCULATOR");

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
        List<Route> potentialRoutes = tryFindingMultipleRandomWaypoints(localRequestedDistanceInCoordinates);
        for (Route route : potentialRoutes) {
            for (Section section : route.sections) {
                if (section.summary.length <= maximumMeters) {
                    logger.info("SUCCESS! We have found a route that meets our conditions: " + section.summary.length + " <= " + maximumMeters);
                    return createRoutingWaypoint(section);
                }
            }
        }
        if (recursionCounter < MAX_RECURSIONS) {
            logger.info("FAIL! We have NOT found a route that meets our conditions! We will adjust the specified distance now and try again!");
            recursionCounter++;
            return calculateMaximumRoute(localRequestedDistanceInMeters - recursionStepsInMeters, recursionCounter);
        } else {
            throw new InvalidCalculationRequest("Could not find a route with this maximum length: " + localRequestedDistanceInMeters);
        }
    }

    private RoutingWaypoint calculateMinimumRoute(double localRequestedDistanceInMeters, int recursionCounter) throws InvalidCalculationRequest {
        double localRequestedDistanceInCoordinates = localRequestedDistanceInMeters * ONE_THOUSAND_METERS_IN_COORD;
        logger.info("We will try to calculate a route with minimum length = " + localRequestedDistanceInMeters + "m (" + localRequestedDistanceInCoordinates + " in coordinates)...");
        List<Route> potentialRoutes = tryFindingMultipleRandomWaypoints(localRequestedDistanceInCoordinates);
        for (Route route : potentialRoutes) {
            for (Section section : route.sections) {
                if (section.summary.length >= minimumMeters) {
                    logger.info("SUCCESS! We have found a route that meets our conditions: " + section.summary.length + "m >= " + minimumMeters + "m");
                    return createRoutingWaypoint(section);
                }
            }
        }
        if (recursionCounter < MAX_RECURSIONS) {
            logger.info("FAIL! We have NOT found a route that meets our conditions! We will adjust the specified distance now and try again!");
            recursionCounter++;
            return calculateMinimumRoute(localRequestedDistanceInMeters + recursionStepsInMeters, recursionCounter);
        } else {
            throw new InvalidCalculationRequest("Could not find a route with this minimum length: " + localRequestedDistanceInMeters + "m");
        }
    }

    private List<Route> tryFindingMultipleRandomWaypoints(double localRequestDistanceInCoordinates) {
        List<Route> potentialRoutes = new LinkedList<>();
        for (int count = 0; count < MAX_RANDOM_POINTS_PER_DISTANCE; count++) {
            potentialRoutes.addAll(tryFindingSingleRandomWaypoint(localRequestDistanceInCoordinates));
        }
        return potentialRoutes;
    }

    private List<Route> tryFindingSingleRandomWaypoint(double localRequestDistanceInCoordinates) {
        RoutingWaypoint tempDestination = returnRandomWaypointInSpecifiedRange(localRequestDistanceInCoordinates);
        HereApiRoutingResponse hereApiRoutingResponse = hereApiRestService.getRoute(origin, tempDestination);
        if (!hereApiRoutingResponse.routes.isEmpty()) {
            logger.info("\t\t\tWe found a potential route to (" + tempDestination.getCoordinatesAsString() + ")! Route length = " + hereApiRoutingResponse.routes.get(0).sections.get(0).summary.length + "m");
        }
        return hereApiRoutingResponse.routes;
    }

    @SuppressWarnings("UnnecessaryLocalVariable")
    private RoutingWaypoint returnRandomWaypointInSpecifiedRange(double localRequestDistanceInCoordinates) {
        int randomAngle = ThreadLocalRandom.current().nextInt(0, 359);
        double hypotenuseLine = localRequestDistanceInCoordinates;
        double adjacentLine, oppositeLine;
        double newLatitude, newLongitude;
        logger.info("\tWe will now calculate a random point from (" + origin.getCoordinatesAsString() + ") with an angle = " + randomAngle + " degrees and distance = " + localRequestDistanceInCoordinates + " (in coordinates)");
        if (randomAngle > 270) {
            randomAngle -= 270;
            oppositeLine = hypotenuseLine * Math.sin(randomAngle);
            adjacentLine = hypotenuseLine * Math.cos(randomAngle);
            newLatitude = origin.getLatitude() + oppositeLine;
            newLongitude = origin.getLongitude() - adjacentLine;
        } else if (randomAngle > 180) {
            randomAngle -= 180;
            oppositeLine = hypotenuseLine * Math.sin(randomAngle);
            adjacentLine = hypotenuseLine * Math.cos(randomAngle);
            newLatitude = origin.getLatitude() - adjacentLine;
            newLongitude = origin.getLongitude() - oppositeLine;
        } else if (randomAngle > 90) {
            randomAngle -= 90;
            oppositeLine = hypotenuseLine * Math.sin(randomAngle);
            adjacentLine = hypotenuseLine * Math.cos(randomAngle);
            newLatitude = origin.getLatitude() - oppositeLine;
            newLongitude = origin.getLongitude() + adjacentLine;
        } else {
            oppositeLine = hypotenuseLine * Math.sin(randomAngle);
            adjacentLine = hypotenuseLine * Math.cos(randomAngle);
            newLatitude = origin.getLatitude() + adjacentLine;
            newLongitude = origin.getLongitude() + oppositeLine;
        }
        RoutingWaypoint result = new RoutingWaypoint("randomPoint(angle=" + randomAngle + "degrees)", newLatitude, newLongitude);
        logger.info("\t\tCalculated random point: normalized angle = " + randomAngle + " degrees, coordinates = (" + newLatitude + "," + newLongitude + ")");
        return result;
    }

    private RoutingWaypoint createRoutingWaypoint(Section section) {
        double lng = section.arrival.place.location.lng;
        double lat = section.arrival.place.location.lat;
        String name = section.arrival.place.type;
        return new RoutingWaypoint(name, lat, lng);
    }
}