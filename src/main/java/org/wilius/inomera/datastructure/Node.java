package org.wilius.inomera.datastructure;

import org.wilius.inomera.datastructure.exception.InvalidRouteException;
import org.wilius.inomera.datastructure.exception.WayAlreadyExistsException;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.LinkedList;

class Node {

    /**
     * name of the node as a letter
     */
    private char name;

    /**
     * available ways from this node
     */
    private ArrayList<Way> ways;


    /**
     * creates a new node with given name
     *
     * @param name name identifier of the node
     */
    Node(char name) {
        this.name = name;
        this.ways = new ArrayList<>();
    }

    /**
     * checks whether or not the given name is belong to the this node
     *
     * @param nameToCheck parameter to check with this node
     * @return true if given name is belong to the this not, otherwise false
     */
    boolean is(char nameToCheck) {
        return name == nameToCheck;
    }

    /**
     * adds a new way to the node
     *
     * @param node     destination node
     * @param distance distance between current instance and first parameter
     */
    void addWay(Node node, BigInteger distance) {
        checkWayExists(node);
        ways.add(new Way(node, distance));
    }

    /**
     * checks a way is already exists for given node in this node
     *
     * @param node node to check
     * @throws WayAlreadyExistsException throws an exception if a way already exists for given node
     */
    private void checkWayExists(Node node) {
        for (Way way : ways) {
            if (way.is(node)) {
                throw new WayAlreadyExistsException("Way is already exists");
            }
        }
    }

    /**
     * finds all available paths from this node
     *
     * @param to destination node to find the routes
     * @return list of routes
     */
    ArrayList<Route> findPaths(Node to) {
        ArrayList<Route> result = new ArrayList<>();
        findPaths(to, new LinkedList<>(), result);
        return result;
    }

    /**
     * adds the available paths to the given result object if a route exists
     *
     * @param to     destination node
     * @param pipe   backtrack of the line to add it as a route if a route exists
     * @param result keeps the founded routes
     * @return true if the current node is equal to the destination node, otherwise returns false
     */
    private boolean findPaths(Node to, LinkedList<Way> pipe, ArrayList<Route> result) {
        boolean found = false;

        // pipe size check added for the sake of the self route.
        // for example C to C route won't work because starting and
        // destination node will be equal. To solve this problem,
        // forced to the pipe variables size should be greater than zero
        // which mean we are not on the starting node
        if (equals(to)) {
            found = true;
        }

        for (Way way : ways) {
            if (way.isVisited()) {
                continue;
            }

            pipe.add(way);
            if (way.findPaths(to, pipe, result)) {
                result.add(new Route(pipe));
            }
            pipe.remove(way);
        }

        return found;
    }

    /**
     * looking for a Route with given path
     *
     * @param path list of the path Nodes
     * @return a {@link Route} instance of given path
     * @throws InvalidRouteException if no route exists with given path
     */
    Route findPath(ArrayList<Node> path) {
        LinkedList<Way> result = new LinkedList<>();
        findPath(path, result, 0);
        return new Route(result);
    }

    /**
     * util method of the recursion which starts with the function {{@link #findPath(ArrayList)}}
     *
     * @param path  list of the path Nodes
     * @param pipe  backtrack of the line to add it as a route if a route exists
     * @param index deep of the recursion
     * @return true if the given route exists, otherwise returns false
     */
    private boolean findPath(ArrayList<Node> path, LinkedList<Way> pipe, int index) {
        if (equals(path.get(index))) {
            if (path.size() - 1 == index) {
                return true;
            }

            for (Way way : ways) {
                if (way.findPath(path, pipe, index + 1)) {
                    pipe.add(way);
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * this class created to demonstrate a single-way route between two nodes
     */
    final class Way {
        /**
         * destination node at the end of the this way
         */
        private Node to;

        /**
         * distance between the owner of the way and {{@link #to}}
         */
        private BigInteger distance;

        /**
         * a helper variable to mark this line as visited to prevent {@link StackOverflowError}
         */
        private boolean visited = false;

        /**
         * creates a new Way instance
         *
         * @param to       destination node of the at the end of the way
         * @param distance distance between owner of the way and {{@link #to}}
         */
        Way(Node to, BigInteger distance) {
            this.to = to;
            this.distance = distance;
        }

        /**
         * checks the given node is the same as the destination of the way
         *
         * @param node node to check
         * @return if destination node and parameter are same instance returns true, otherwise false
         */
        boolean is(Node node) {
            return to.equals(node);
        }

        /**
         * @return visitation status of the way
         */
        boolean isVisited() {
            return visited;
        }

        /**
         * helper function for the recursion of the {@link Node#findPaths(Node, LinkedList, ArrayList)}
         *
         * @param to     destination node
         * @param pipe   pipe of the backtrack
         * @param result found list of Routes
         * @return true if a new route founded
         */
        boolean findPaths(Node to, LinkedList<Way> pipe, ArrayList<Route> result) {
            visited = true;
            boolean founded = this.to.findPaths(to, pipe, result);
            visited = false;
            return founded;
        }

        /**
         * helper function for the recursion of the {@link Node#findPath(ArrayList, LinkedList, int)}
         *
         * @param path  node list
         * @param pipe  pipe of the backtrack
         * @param index deep of the recursion with a zero based enumeration
         * @return true if route with given path found
         */
        boolean findPath(ArrayList<Node> path, LinkedList<Way> pipe, int index) {
            return this.to.findPath(path, pipe, index);
        }

        /**
         * @return gets the distance of the way
         */
        BigInteger getDistance() {
            return distance;
        }
    }
}
