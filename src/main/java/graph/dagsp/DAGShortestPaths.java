package graph.dagsp;

import common.Metrics;
import java.util.*;

public class DAGShortestPaths {
    private final List<List<Edge>> dag;
    private final Metrics metrics;

    public DAGShortestPaths(List<List<Edge>> dag, Metrics metrics) {
        this.dag = Objects.requireNonNull(dag);
        this.metrics = metrics == null ? new Metrics() : metrics;
    }

    public DAGPathResult shortestPaths(int source, List<Integer> topoOrder) {
        int n = dag.size();
        int[] dist = new int[n];
        int[] parent = new int[n];
        Arrays.fill(dist, Integer.MAX_VALUE);
        Arrays.fill(parent, -1);
        if (source < 0 || source >= n) return new DAGPathResult(dist, parent);
        dist[source] = 0;

        metrics.startTimer();
        for (int u : topoOrder) {
            if (dist[u] == Integer.MAX_VALUE) continue;
            for (Edge e : dag.get(u)) {
                metrics.relaxations++;
                if ((long)dist[u] + e.weight < dist[e.to]) {
                    dist[e.to] = dist[u] + e.weight;
                    parent[e.to] = u;
                }
            }
        }
        metrics.endTimer();
        return new DAGPathResult(dist, parent);
    }
}
