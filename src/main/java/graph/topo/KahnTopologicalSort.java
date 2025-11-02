package graph.topo;

import common.Metrics;
import java.util.*;

public class KahnTopologicalSort {

    public static TopoResult sort(List<List<Integer>> dag, Metrics metrics) {
        int n = dag.size();
        int[] inDegree = new int[n];
        for (int u = 0; u < n; u++) {
            for (int v : dag.get(u)) inDegree[v]++;
        }

        Deque<Integer> queue = new ArrayDeque<>();
        for (int i = 0; i < n; i++) if (inDegree[i] == 0) queue.add(i);

        List<Integer> topo = new ArrayList<>();
        metrics.startTimer();
        while (!queue.isEmpty()) {
            metrics.kahnPops++;
            int u = queue.poll();
            topo.add(u);
            for (int v : dag.get(u)) {
                if (--inDegree[v] == 0) {
                    metrics.kahnPushes++;
                    queue.add(v);
                }
            }
        }
        metrics.endTimer();

        boolean isDAG = topo.size() == n;
        return new TopoResult(topo, isDAG);
    }
}
