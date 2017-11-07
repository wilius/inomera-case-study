package org.wilius.inomera.datastructure;

import java.math.BigInteger;

class Way {
    private Node to;
    private BigInteger distance;

    Way(Node to, BigInteger distance) {
        this.to = to;
        this.distance = distance;
    }


    boolean is(Node node) {
        return to.equals(node);
    }
}
