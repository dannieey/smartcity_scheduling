package graph.scc;

import java.util.*;

/**
 * Builds condensation graph (DAG of components).
 * Input: original adjacency (List<List<Integer>>) and compId for each node.
 * Output: adjacency list of condensed graph and mapping node->component.
 */
public class CondensationGraph {
    private final List<List<Integer>> componentAdj;
    private final int[] nodeToComp;
    private final int compCount;

    public CondensationGraph(List<List<Integer>> original, List<List<Integer>> components) {
        this.compCount = components.size();
        this.nodeToComp = new int[original.size()];
        Arrays.fill(nodeToComp, -1);
        for (int ci = 0; ci < components.size(); ci++) {
            for (int v : components.get(ci)) {
                nodeToComp[v] = ci;
            }
        }
        componentAdj = new ArrayList<>();
        for (int i = 0; i < compCount; i++) componentAdj.add(new ArrayList<>());

        // Build edges between components (no duplicates)
        Set<Long> seen = new HashSet<>();
        for (int u = 0; u < original.size(); u++) {
            int cu = nodeToComp[u];
            for (int v : original.get(u)) {
                int cv = nodeToComp[v];
                if (cu != cv) {
                    long key = (((long)cu) << 32) | (cv & 0xffffffffL);
                    if (!seen.contains(key)) {
                        componentAdj.get(cu).add(cv);
                        seen.add(key);
                    }
                }
            }
        }
    }

    public List<List<Integer>> getComponentAdj() { return componentAdj; }
    public int[] getNodeToComp() { return nodeToComp; }
    public int getCompCount() { return compCount; }
}
