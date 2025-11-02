package graph.dagsp;

import graph.topo.KahnTopologicalSort;
import graph.topo.TopoResult;
import java.util.*;

public class DAGShortestPaths {
    public static DAGPathResult compute(Map<Integer, List<int[]>> graph, int n, int source) {
        TopoResult topo = KahnTopologicalSort.sort(convert(graph), n);
        double[] dist = new double[n];
        int[] prev = new int[n];
        Arrays.fill(dist, Double.POSITIVE_INFINITY);
        Arrays.fill(prev, -1);
        dist[source] = 0;

        for (int u : topo.getOrder()) {
            for (int[] edge : graph.getOrDefault(u, Collections.emptyList())) {
                int v = edge[0];
                double w = edge[1];
                if (dist[u] + w < dist[v]) {
                    dist[v] = dist[u] + w;
                    prev[v] = u;
                }
            }
        }
        return new DAGPathResult(dist, prev);
    }

    private static Map<Integer, List<Integer>> convert(Map<Integer, List<int[]>> g) {
        Map<Integer, List<Integer>> out = new HashMap<>();
        for (var e : g.entrySet()) {
            List<Integer> list = new ArrayList<>();
            for (int[] pair : e.getValue()) list.add(pair[0]);
            out.put(e.getKey(), list);
        }
        return out;
    }
}
