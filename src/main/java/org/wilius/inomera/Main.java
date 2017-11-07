package org.wilius.inomera;

import org.wilius.inomera.datastructure.Graph;

import java.math.BigInteger;

public class Main {

    public static void main(String[] args) {
        System.out.print("Graph : ");
        //Scanner reader = new Scanner(System.in);
        String routes = "AB5, BC4, CD8, DC8, DE6, AD5, CE2, EB3, AE7".toLowerCase();//reader.nextLine().toLowerCase();

        Graph graph = new Graph(routes);
        System.out.println(String.format("Output #1: %s", getDistanceText(graph.getRouteLength("a", "b", "c"))));
        System.out.println(String.format("Output #2: %s", getDistanceText(graph.getRouteLength("a", "d"))));
        System.out.println(String.format("Output #3: %s", getDistanceText(graph.getRouteLength("a", "d", "c"))));
        System.out.println(String.format("Output #4: %s", getDistanceText(graph.getRouteLength("a", "e", "b", "c", "d"))));
        System.out.println(String.format("Output #5: %s", getDistanceText(graph.getRouteLength("a", "e", "d"))));
        System.out.println(String.format("Output #6: %d", graph.getNumberOfTripsWithMaxStop("c", "c", 3)));
        System.out.println(String.format("Output #7: %d", graph.getNumberOfTripsWithExactStop("a", "c", 4)));
        System.out.println(String.format("Output #8: %s", getDistanceText(graph.getShortestRoute("a", "c"))));
        System.out.println(String.format("Output #9: %s", getDistanceText(graph.getShortestRoute("b", "b"))));
        System.out.println(String.format("Output #10: %s", graph.getNumberOfTripsWithDistanceLessThan("c", "c", new BigInteger("30"))));
    }

    private static String getDistanceText(BigInteger bigInteger) {
        if (bigInteger == null) {
            return "NO SUCH ROUTE";
        }

        return bigInteger.toString();
    }
}