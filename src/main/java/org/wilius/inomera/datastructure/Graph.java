package org.wilius.inomera.datastructure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Graph {
    private final Logger log = LoggerFactory.getLogger(Graph.class);

    private ArrayList<Node> nodes;

    public Graph(String routes) {
        nodes = new ArrayList<>();
        init(routes);
    }

    private void init(String routes) {
        StringTokenizer tokenizer = new StringTokenizer(routes, ",");

        while (tokenizer.hasMoreTokens()) {
            String route = tokenizer.nextToken().trim();
            if (!route.matches("[a-z]{2}\\d+")) {
                throw new RuntimeException(String.format("Input %s is not proper", route));
            }

            String from = String.valueOf(route.charAt(0));
            String to = String.valueOf(route.charAt(1));
            BigInteger distance = new BigInteger(route.substring(2));
            addRoute(from, to, distance);
            log.info("Route from {} to {} with distance {}", from, to, distance.toString());
        }
    }

    private void addRoute(final String from, String to, BigInteger distance) {
        get(from, true).addWay(get(to, true), distance);
    }

    private Node get(String name) {
        return get(name, false);
    }

    private Node get(String name, boolean addIfNotExists) {
        for (Node node : nodes) {
            if (node.is(name)) {
                return node;
            }
        }

        if (!addIfNotExists) {
            throw new RuntimeException(String.format("Node with name %s not founded", name));
        }

        Node node = new Node(name);
        nodes.add(node);
        return node;
    }

    public ArrayList<ArrayList<Node.Way>> findPaths(String fromName, String toName) {
        Node from = get(fromName);
        Node to = get(toName);
        return from.findPaths(to);
    }

    public ArrayList<Node.Way> findPath(String fromName, String... nodeNames) {
        Node from = get(fromName);

        ArrayList<Node> path = new ArrayList<>();
        path.add(from);
        for (String nodeName : nodeNames) {
            Node node = get(nodeName);
            path.add(node);
        }

        return from.findPath(path);
    }

    public BigInteger getRouteLength(String fromName, String... nodeNames) {
        ArrayList<Node.Way> path = findPath(fromName, nodeNames);
        if (path.size() == 0) {
            return null;
        }

        return path.stream()
                .map(Node.Way::getDistance)
                .reduce(BigInteger::add)
                .get();
    }

    public long getNumberOfTripsWithMaxStop(String from, String to, final int maxStopAmount) {
        return get(from).findPaths(get(to)).stream().filter(x -> x.size() <= maxStopAmount).count();
    }

    public long getNumberOfTripsWithExactStop(String from, String to, final int stopAmount) {
        return get(from).findPaths(get(to)).stream().filter(x -> x.size() == stopAmount).count();
    }

    public BigInteger getShortestRoute(String from, String to) {
        ArrayList<ArrayList<Node.Way>> routes = get(from).findPaths(get(to));
        if (routes.size() == 0) {
            return null;
        }

        BigInteger min = BigInteger.ZERO;
        for (ArrayList<Node.Way> route : routes) {
            BigInteger routeLength = route.stream()
                    .map(Node.Way::getDistance)
                    .reduce(BigInteger::add)
                    .get();

            if (min.equals(BigInteger.ZERO) || routeLength.compareTo(min) < 0) {
                min = routeLength;
            }
        }

        return min;
    }

    public int getNumberOfTripsWithDistanceLessThan(String from, String to, BigInteger maxDistace) {
        ArrayList<ArrayList<Node.Way>> routes = get(from).findPaths(get(to));
        if (routes.size() == 0) {
            return 0;
        }

        int count = 0;
        for (ArrayList<Node.Way> route : routes) {
            BigInteger routeLength = route.stream()
                    .map(Node.Way::getDistance)
                    .reduce(BigInteger::add)
                    .get();

            if (routeLength.compareTo(maxDistace) <= 0) {
                count++;
            }
        }

        return count;
    }
}