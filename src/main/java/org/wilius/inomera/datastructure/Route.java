package org.wilius.inomera.datastructure;

import org.wilius.inomera.datastructure.exception.InvalidRouteException;

import java.math.BigInteger;
import java.util.LinkedList;

/**
 * This class created to demonstrate a route between given nodes
 */
class Route {

    /**
     * list of Way to demonstrate the route
     */
    private LinkedList<Node.Way> route;

    /**
     * creates a new Route instance with the given parameter to demonstrate a route
     *
     * @param route list of ways of route
     */
    Route(LinkedList<Node.Way> route) {
        if (route.size() == 0) {
            throw new InvalidRouteException("Empty list not acceptable as route");
        }

        this.route = new LinkedList<>(route);
    }

    /**
     * calculates the distance of the route from starting node to destination node
     *
     * @return calculated distance of route
     */
    BigInteger getDistance() {
        return route.stream()
                .map(Node.Way::getDistance)
                .reduce(BigInteger::add)
                .get();
    }

    /**
     * gets the stop amount in this route
     *
     * @return stop amount of route
     */
    int getStopAmount() {
        return route.size();
    }
}
