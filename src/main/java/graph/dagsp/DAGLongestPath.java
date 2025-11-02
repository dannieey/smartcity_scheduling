package graph.dagsp;

import common.Metrics;
import java.util.*;

public class DAGLongestPath {
    private final List<List<Edge>> dag;
    private final Metrics metrics;

    public DAGLongestPath(List<List<Edge>> dag, Metrics metrics) {
        this.dag = Objects.requireNonNull(dag);
        this.metrics = metrics == null ? new Metrics() : metrics;
    }

    /**
     * Returns DAGPathResult where dist[v] is longest distance from source to v.
     * Uses DP over topological order.
     */
    public DAGPathResult longestPaths(int source, List<Integer> topoOrder) {
        int n = dag.size();
        int[] dist = new int[n];
        int[] parent = new int[n];
        Arrays.fill(dist, Integer.MIN_VALUE);
        Arrays.fill(parent, -1);
        if (source >= 0 && source < n) dist[source] = 0;

        metrics.startTimer();
        for (int u : topoOrder) {
            if (dist[u] == Integer.MIN_VALUE) continue;
            for (Edge e : dag.get(u)) {
                metrics.relaxations++;
                if ((long)dist[u] + e.weight > dist[e.to]) {
                    dist[e.to] = dist[u] + e.weight;
                    parent[e.to] = u;
                }
            }
        }
        metrics.endTimer();
        return new DAGPathResult(dist, parent);
    }

    /**
     * Finds a node with maximum distance and returns it and its length.
     */
    public static OptionalIntIndex findLongest(int[] dist) {
        int best = Integer.MIN_VALUE;
        int idx = -1;
        for (int i = 0; i < dist.length; i++) {
            if (dist[i] > best) { best = dist[i]; idx = i; }
        }
        return new OptionalIntIndex(idx, best);
    }

    public static class OptionalIntIndex {
        public final int index;
        public final int value;
        public OptionalIntIndex(int index, int value) { this.index = index; this.value = value; }
    }
}
