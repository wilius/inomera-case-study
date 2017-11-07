package org.wilius.inomera.datastructure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wilius.inomera.datastructure.exception.InvalidExactRouteParameterException;
import org.wilius.inomera.datastructure.exception.InvalidRouteException;
import org.wilius.inomera.datastructure.exception.NodeNotFoundException;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * This class create to demonstrate the graph representation of the input
 */
public class Graph {
    private final Logger log = LoggerFactory.getLogger(Graph.class);

    /**
     * List of the available nodes in graph instance
     */
    private ArrayList<Node> nodes;

    /**
     * creates a new graph instance with given routes
     *
     * @param routes this parameter described at {{@link #init(String)}} in detail.
     */
    public Graph(String routes) {
        nodes = new ArrayList<>();
        init(routes);
    }

    /**
     * parses the routes to their Graph representation
     *
     * @param routes string to parse as Graph object. Each way should be delimited by a comma and
     *               nodes should be named with a latin letter. For example AB55 demonstrates that
     *               there are two node called A and B respectively and the distance between these
     *               nodes are 55 unit. An example of full node as below:
     *               <p>
     *               AB5, BC4, CD8, DC8, DE6, AD5, CE2, EB3, AE7
     */
    private void init(String routes) {
        StringTokenizer tokenizer = new StringTokenizer(routes, ",");

        while (tokenizer.hasMoreTokens()) {
            String route = tokenizer.nextToken().trim();
            if (!route.matches("[a-z]{2}\\d+")) {
                throw new RuntimeException(String.format("Input %s is not proper", route));
            }

            char from = route.charAt(0);
            char to = route.charAt(1);
            BigInteger distance = new BigInteger(route.substring(2));
            addWay(from, to, distance);
            log.info("Route from {} to {} with distance {}", from, to, distance.toString());
        }
    }

    /**
     * Looking for a route and calculates the distance for route
     *
     * @param fromName      initial node of the trip
     * @param pathNodeNames node names of the trip that we want to check the availability
     * @return the distance of the route
     * @throws InvalidRouteException when no route exists with given path
     */
    public BigInteger getRouteDistance(char fromName, char... pathNodeNames) {
        try {
            Route route = findExactPath(fromName, pathNodeNames);
            return route.getDistance();
        } catch (InvalidRouteException e) {
            return null;
        }
    }

    /**
     * finds the number of the routes which has stop amount less than or equal to the given parameter
     *
     * @param from          starting node of the trip
     * @param to            destination node of the trip
     * @param maxStopAmount maximum amount of the stop
     * @return number of the routes which are appropriate to the given situation
     */
    public long getNumberOfTripsWithMaxStop(char from, char to, final int maxStopAmount) {
        return findPaths(from, to).stream()
                .filter(x -> x.getStopAmount() <= maxStopAmount)
                .count();
    }

    /**
     * finds the number of the routes with given stop amount
     *
     * @param from       starting node of the trip
     * @param to         destination node of the trip
     * @param stopAmount amount of the stop
     * @return number of the routes which are appropriate to the given situation
     */
    public long getNumberOfRoutesWithExactStop(char from, char to, final int stopAmount) {
        return findPaths(from, to).stream()
                .filter(x -> x.getStopAmount() == stopAmount)
                .count();
    }

    /**
     * finds the minimum distance between the nodes
     *
     * @param from starting node of the trip
     * @param to   destination node of the trip
     * @return the shortest distance or null if no route is available
     */
    public BigInteger getShortestRoute(char from, char to) {
        ArrayList<Route> routes = findPaths(from, to);
        return getMinimumDistance(routes);
    }

    /**
     * finds the number of routes whom distance is less than the maxDistance parameter
     *
     * @param from        starting node of the trip
     * @param to          destination node of the trip
     * @param maxDistance allowed maximum distance
     * @return the amount of the routes between nodes
     */
    public long getNumberOfTripsWithDistanceLessThan(char from, char to, BigInteger maxDistance) {
        return findPaths(from, to).stream()
                .filter(x -> x.getDistance().compareTo(maxDistance) <= 0)
                .count();
    }

    /**
     * A shortcut of the function {{@link #get(char, boolean)}}.
     * Not creates a new instance if the node not found.
     *
     * @param name name of the node to find
     * @return {@link Node} representation of the name parameter
     */
    private Node get(char name) {
        return get(name, false);
    }

    /**
     * gets the node representation of the node name
     *
     * @param name           name of the {@link Node} to find
     * @param addIfNotExists adds a node with first parameter if specified as true
     * @return {@link Node} representation of the name parameter
     * @throws NodeNotFoundException if node not founded and second parameter specified as false
     */
    private Node get(char name, boolean addIfNotExists) {
        for (Node node : nodes) {
            if (node.is(name)) {
                return node;
            }
        }

        if (!addIfNotExists) {
            throw new NodeNotFoundException(String.format("Node with name %s not founded", name));
        }

        Node node = new Node(name);
        nodes.add(node);
        return node;
    }

    /**
     * Looking for an exact path with given parameters.
     *
     * @param fromName      starting node of trip
     * @param pathNodeNames node names of the trip that we want to check the availability
     * @return {@link Route} representation of the given path
     * @throws InvalidRouteException               when no route exists with given path
     * @throws InvalidExactRouteParameterException if no path nodes specified
     */
    private Route findExactPath(char fromName, char... pathNodeNames) {
        if (pathNodeNames.length == 0) {
            throw new InvalidExactRouteParameterException("No parameter for the destination specified");
        }

        Node from = get(fromName);

        ArrayList<Node> path = new ArrayList<>();
        path.add(from);
        for (char nodeName : pathNodeNames) {
            path.add(get(nodeName));
        }

        return from.findPath(path);
    }


    /**
     * Finds the minimum distance among the given routes
     *
     * @param routes List
     * @return minimum distance among the routes. returns null if the parameter is null or its size is zero
     */
    private BigInteger getMinimumDistance(ArrayList<Route> routes) {
        if (routes == null || routes.size() == 0) {
            return null;
        }

        return routes.stream()
                .map(Route::getDistance)
                .min(BigInteger::compareTo)
                .get();
    }

    /**
     * converts String parameters to their {@link Node} representation and triggers the route finding algorithm
     *
     * @param from starting point of the trip
     * @param to   terminal node of the trip
     * @return list of available routes
     */
    private ArrayList<Route> findPaths(char from, char to) {
        return get(from).findPaths(get(to));
    }

    /**
     * adds a new way to from node
     *
     * @param from     node to add a new way
     * @param to       destination of the way for from parameter
     * @param distance distance between from and to parameters
     */
    private void addWay(char from, char to, BigInteger distance) {
        get(from, true).addWay(get(to, true), distance);
    }
}