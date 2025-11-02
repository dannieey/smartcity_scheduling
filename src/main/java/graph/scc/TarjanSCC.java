package graph.scc;

import common.Metrics;
import java.util.*;

public class TarjanSCC {
    private final List<List<Integer>> graph;
    private final Metrics metrics;
    private int index = 0;
    private final int[] indices;
    private final int[] lowlink;
    private final boolean[] onStack;
    private final Deque<Integer> stack = new ArrayDeque<>();
    private final List<List<Integer>> sccs = new ArrayList<>();

    public TarjanSCC(List<List<Integer>> graph, Metrics metrics) {
        this.graph = Objects.requireNonNull(graph);
        this.metrics = metrics == null ? new Metrics() : metrics;
        int n = graph.size();
        indices = new int[n];
        Arrays.fill(indices, -1);
        lowlink = new int[n];
        onStack = new boolean[n];
    }

    public SCCResult computeSCC() {
        metrics.startTimer();
        for (int v = 0; v < graph.size(); v++) {
            if (indices[v] == -1) dfs(v);
        }
        metrics.endTimer();
        return new SCCResult(sccs);
    }

    private void dfs(int v) {
        metrics.dfsVisits++;
        indices[v] = index;
        lowlink[v] = index++;
        stack.push(v);
        onStack[v] = true;

        for (int w : graph.get(v)) {
            metrics.dfsEdges++;
            if (indices[w] == -1) {
                dfs(w);
                lowlink[v] = Math.min(lowlink[v], lowlink[w]);
            } else if (onStack[w]) {
                lowlink[v] = Math.min(lowlink[v], indices[w]);
            }
        }

        if (lowlink[v] == indices[v]) {
            List<Integer> component = new ArrayList<>();
            int w;
            do {
                w = stack.pop();
                onStack[w] = false;
                component.add(w);
            } while (w != v);
            sccs.add(component);
        }
    }
}
