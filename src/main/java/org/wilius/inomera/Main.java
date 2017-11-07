package org.wilius.inomera;

import java.math.BigInteger;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Main {
    public static void main(String[] args) {
        System.out.print("Graph : ");
        Scanner reader = new Scanner(System.in);
        String routes = reader.nextLine();
        StringTokenizer tokenizer = new StringTokenizer(routes, ",");

        while (tokenizer.hasMoreTokens()) {
            String route = tokenizer.nextToken().trim();
            if (!route.matches("[A-Da-d][A-Da-d]\\d+")) {
                throw new RuntimeException(String.format("Input %s is not proper", route));
            }

            String startNode = String.valueOf(route.charAt(0));
            String terminateNode = String.valueOf(route.charAt(1));
            BigInteger distance = new BigInteger(route.substring(2));
            System.out.println(String.format("Route from %s to %s with distance %s", startNode, terminateNode, distance.toString()));
        }
    }
}