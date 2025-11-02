package graph.dagsp;

import java.util.*;

public class DAGPathResult {
    private final double[] dist;
    private final int[] prev;

    public DAGPathResult(double[] dist, int[] prev) {
        this.dist = dist;
        this.prev = prev;
    }

    public double[] getDistances() { return dist; }

    public void print(int source) {
        System.out.println("Source: " + source);
        for (int i = 0; i < dist.length; i++) {
            System.out.println("To " + i + ": " + dist[i]);
        }
    }
}
