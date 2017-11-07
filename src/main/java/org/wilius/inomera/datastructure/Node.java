package org.wilius.inomera.datastructure;

import java.math.BigInteger;
import java.util.ArrayList;

class Node {

    private String name;

    private ArrayList<Way> ways;

    Node(String name) {
        this.name = name;
        this.ways = new ArrayList<Way>();
    }

    boolean is(String nameToCheck) {
        return name.equals(nameToCheck);
    }

    void addWay(Node node, BigInteger distance) {
        checkWayExists(node);
        ways.add(new Way(node, distance));
    }

    private void checkWayExists(Node node) {
        for (Way way : ways) {
            if (way.is(node)) {
                throw new RuntimeException("Way is already exists");
            }
        }
    }

    ArrayList<ArrayList<Way>> findPaths(Node to) {
        ArrayList<ArrayList<Way>> result = new ArrayList<>();
        findPaths(to, new ArrayList<>(), result);
        return result;
    }

    private boolean findPaths(Node to, ArrayList<Way> pipe, ArrayList<ArrayList<Way>> result) {
        boolean found = false;
        if (equals(to) && pipe.size() > 0) {
            found = true;
        }

        for (Way way : ways) {
            if (way.isVisited()) {
                continue;
            }

            pipe.add(way);
            if (way.findPaths(to, pipe, result)) {
                fill(pipe, result);
            }
            pipe.remove(way);
        }

        return found;
    }

    ArrayList<Way> findPath(ArrayList<Node> path) {
        ArrayList<Way> result = new ArrayList<>();
        findPath(path, result, 0);
        return result;
    }

    private boolean findPath(ArrayList<Node> path, ArrayList<Way> pipe, int index) {
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

    private void fill(ArrayList<Way> pipe, ArrayList<ArrayList<Way>> result) {
        result.add(new ArrayList<>(pipe));
    }

    class Way {
        private Node to;
        private BigInteger distance;
        private boolean visited = false;

        Way(Node to, BigInteger distance) {
            this.to = to;
            this.distance = distance;
        }


        boolean is(Node node) {
            return to.equals(node);
        }

        boolean isVisited() {
            return visited;
        }

        boolean findPaths(Node to, ArrayList<Way> pipe, ArrayList<ArrayList<Way>> result) {
            visited = true;
            boolean founded = this.to.findPaths(to, pipe, result);
            visited = false;
            return founded;
        }

        boolean findPath(ArrayList<Node> path, ArrayList<Way> pipe, int index) {
            return this.to.findPath(path, pipe, index);
        }

        String getDestinationName() {
            return to.name;
        }

        public BigInteger getDistance() {
            return distance;
        }
    }
}
