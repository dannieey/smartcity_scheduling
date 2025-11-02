package graph.scc;

import java.util.*;

public class SCCResult {
    private final List<List<Integer>> components;

    public SCCResult(List<List<Integer>> components) {
        this.components = components;
    }

    public List<List<Integer>> getComponents() {
        return components;
    }

    public List<Integer> sizes() {
        List<Integer> s = new ArrayList<>();
        for (List<Integer> c : components) s.add(c.size());
        return s;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SCC count=").append(components.size()).append("\n");
        for (int i = 0; i < components.size(); i++) {
            sb.append("C").append(i).append(": ").append(components.get(i)).append("\n");
        }
        return sb.toString();
    }
}
