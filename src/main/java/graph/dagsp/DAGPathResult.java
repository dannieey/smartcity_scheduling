package graph.dagsp;

import java.util.*;

public class DAGPathResult {
    private final int[] dist;
    private final int[] parent;

    public DAGPathResult(int[] dist, int[] parent) {
        this.dist = dist;
        this.parent = parent;
    }

    public int[] getDist() { return dist; }
    public int[] getParent() { return parent; }

    public List<Integer> reconstructPath(int target) {
        if (target < 0 || target >= parent.length) return Collections.emptyList();
        if (dist[target] == Integer.MAX_VALUE || dist[target] == Integer.MIN_VALUE) return Collections.emptyList();
        LinkedList<Integer> path = new LinkedList<>();
        int cur = target;
        while (cur != -1) {
            path.addFirst(cur);
            cur = parent[cur];
        }
        return path;
    }

    @Override
    public String toString() {
        return "DAGPathResult{dist=" + Arrays.toString(dist) + "}";
    }
}
