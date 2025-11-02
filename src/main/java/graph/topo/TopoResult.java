package graph.topo;

import java.util.*;

public class TopoResult {
    private final List<Integer> order;
    private final boolean isDAG;

    public TopoResult(List<Integer> order, boolean isDAG) {
        this.order = order;
        this.isDAG = isDAG;
    }

    public TopoResult(List<Integer> order) {
        this(order, order != null);
    }

    public List<Integer> getOrder() { return order; }
    public boolean isDAG() { return isDAG; }

    @Override
    public String toString() {
        return "TopoResult{isDAG=" + isDAG + ", order=" + order + "}";
    }
}
