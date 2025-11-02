package graph.topo;

import java.util.*;

public class TopoResult {
    private final List<Integer> order;
    private final boolean hasCycle;

    public TopoResult(List<Integer> order, boolean hasCycle) {
        this.order = order;
        this.hasCycle = hasCycle;
    }

    public List<Integer> getOrder() { return order; }
    public boolean hasCycle() { return hasCycle; }

    public void print() {
        if (hasCycle) System.out.println("Cycle detected!");
        else System.out.println("Topological order: " + order);
    }
}
