package graph.scc;

import java.util.*;

public class SCCResult {
    private final List<List<Integer>> components;
    private final int[] nodeToComponent;

    public SCCResult(List<List<Integer>> components, int[] nodeToComponent) {
        this.components = components;
        this.nodeToComponent = nodeToComponent;
    }

    public List<List<Integer>> getComponents() {
        return components;
    }

    public int[] getNodeToComponent() {
        return nodeToComponent;
    }

    public void printResult() {
        System.out.println("SCC Components:");
        for (int i = 0; i < components.size(); i++) {
            System.out.println("Component " + i + ": " + components.get(i));
        }
    }
}
