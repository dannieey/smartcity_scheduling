package graph.scc;

import java.util.*;

public class CondensationGraph {
    public static Map<Integer, List<Integer>> build(SCCResult result, Map<Integer, List<Integer>> original) {
        Map<Integer, List<Integer>> dag = new HashMap<>();
        int[] comp = result.getNodeToComponent();

        for (int i = 0; i < comp.length; i++) {
            for (int j : original.getOrDefault(i, Collections.emptyList())) {
                if (comp[i] != comp[j])
                    dag.computeIfAbsent(comp[i], k -> new ArrayList<>()).add(comp[j]);
            }
        }
        return dag;
    }
}
