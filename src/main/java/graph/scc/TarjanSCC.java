package graph.scc;

import metrics.Metrics;
import java.util.*;

public class TarjanSCC implements Metrics {
    private final Map<Integer, List<Integer>> graph;
    private final int n;
    private int time;
    private long execTime;
    private int iterations;
    private final int[] ids, low;
    private final boolean[] onStack;
    private final Deque<Integer> stack;
    private final List<List<Integer>> sccs;

    public TarjanSCC(Map<Integer, List<Integer>> graph, int n) {
        this.graph = graph;
        this.n = n;
        ids = new int[n];
        low = new int[n];
        onStack = new boolean[n];
        stack = new ArrayDeque<>();
        sccs = new ArrayList<>();
        Arrays.fill(ids, -1);
    }

    public SCCResult run() {
        long start = System.nanoTime();
        for (int i = 0; i < n; i++) {
            if (ids[i] == -1)
                dfs(i);
        }
        execTime = System.nanoTime() - start;

        int[] mapping = new int[n];
        for (int i = 0; i < sccs.size(); i++) {
            for (int node : sccs.get(i))
                mapping[node] = i;
        }
        return new SCCResult(sccs, mapping);
    }

    private void dfs(int at) {
        stack.push(at);
        onStack[at] = true;
        ids[at] = low[at] = time++;
        iterations++;

        for (int to : graph.getOrDefault(at, Collections.emptyList())) {
            if (ids[to] == -1) dfs(to);
            if (onStack[to]) low[at] = Math.min(low[at], low[to]);
        }

        if (ids[at] == low[at]) {
            List<Integer> comp = new ArrayList<>();
            while (true) {
                int node = stack.pop();
                onStack[node] = false;
                low[node] = ids[at];
                comp.add(node);
                if (node == at) break;
            }
            sccs.add(comp);
        }
    }

    public long getExecutionTime() { return execTime; }
    public int getIterations() { return iterations; }
}
