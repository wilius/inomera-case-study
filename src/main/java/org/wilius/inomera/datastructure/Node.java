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
}
