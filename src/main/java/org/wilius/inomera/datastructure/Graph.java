package org.wilius.inomera.datastructure;

import java.math.BigInteger;
import java.util.ArrayList;

public class Graph {
    private ArrayList<Node> nodes;

    public Graph() {
        nodes = new ArrayList<Node>();
    }

    public void addRoute(final String from, String to, BigInteger distance) {
        get(from).addWay(get(to), distance);
    }

    private Node get(String name) {
        for (Node node : nodes) {
            if (node.is(name)) {
                return node;
            }
        }

        Node node = new Node(name);
        nodes.add(node);
        return node;
    }
}