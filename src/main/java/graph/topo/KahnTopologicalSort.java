package graph.topo;

import java.util.*;

public class KahnTopologicalSort {
    public static TopoResult sort(Map<Integer, List<Integer>> graph, int n) {
        int[] indegree = new int[n];
        for (List<Integer> edges : graph.values()) {
            for (int v : edges) indegree[v]++;
        }

        Queue<Integer> q = new LinkedList<>();
        for (int i = 0; i < n; i++)
            if (indegree[i] == 0) q.add(i);

        List<Integer> order = new ArrayList<>();
        while (!q.isEmpty()) {
            int u = q.poll();
            order.add(u);
            for (int v : graph.getOrDefault(u, Collections.emptyList())) {
                indegree[v]--;
                if (indegree[v] == 0) q.add(v);
            }
        }

        return new TopoResult(order, order.size() != n);
    }
}
