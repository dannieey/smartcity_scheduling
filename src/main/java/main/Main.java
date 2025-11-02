package main;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.core.type.TypeReference;
import common.Metrics;
import graph.scc.*;
import graph.topo.*;
import graph.dagsp.*;

import java.io.File;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        String[] datasets = {
                "data/small1",
                "data/small2",
                "data/small3",
                "data/medium1",
                "data/medium2",
                "data/medium3",
                "data/large1",
                "data/large2",
                "data/large3"
        };

        ObjectMapper om = new ObjectMapper();

        for (String path : datasets) {
            System.out.println("\n============================");
            System.out.println("Processing dataset: " + path);
            System.out.println("============================");

            Map<String, Object> root = om.readValue(new File(path), new TypeReference<Map<String, Object>>() {
            });

            int n = (Integer) (root.getOrDefault("n", 0));
            List<Map<String, Object>> edges = (List<Map<String, Object>>) root.getOrDefault("edges", Collections.emptyList());
            int source = (Integer) root.getOrDefault("source", 0);
            String weightModel = (String) root.getOrDefault("weight_model", "edge");

            // Build adjacency (unweighted) for SCC
            List<List<Integer>> adj = new ArrayList<>();
            for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
            for (Map<String, Object> e : edges) {
                int u = ((Number) e.get("u")).intValue();
                int v = ((Number) e.get("v")).intValue();
                adj.get(u).add(v);
            }

            Metrics metrics = new Metrics();
            TarjanSCC tarjan = new TarjanSCC(adj, metrics);
            SCCResult sccRes = tarjan.computeSCC();
            System.out.println("=== SCCs ===");
            System.out.println(sccRes);
            System.out.println("SCC metrics: " + metrics);

            // Build condensation
            CondensationGraph cond = new CondensationGraph(adj, sccRes.getComponents());
            List<List<Integer>> compAdj = cond.getComponentAdj();
            int[] nodeToComp = cond.getNodeToComp();
            int compCount = cond.getCompCount();
            System.out.println("Condensed nodes: " + compCount);
            System.out.println("Condensed adj: " + compAdj);

            // Topological sort on condensed DAG
            Metrics topoMetrics = new Metrics();
            TopoResult topo = KahnTopologicalSort.sort(compAdj, topoMetrics);
            System.out.println("Topo result: " + topo);
            System.out.println("Topo metrics: " + topoMetrics);

            // Build weighted condensed DAG for DAG-SP (edge-weight model)
            List<List<Edge>> compWeighted = new ArrayList<>();
            for (int i = 0; i < compCount; i++) compWeighted.add(new ArrayList<>());

            Map<Long, Integer> best = new HashMap<>();
            for (Map<String, Object> e : edges) {
                int u = ((Number) e.get("u")).intValue();
                int v = ((Number) e.get("v")).intValue();
                int w = ((Number) e.get("w")).intValue();
                int cu = nodeToComp[u];
                int cv = nodeToComp[v];
                if (cu == cv) continue;
                long key = (((long) cu) << 32) | (cv & 0xffffffffL);
                if (!best.containsKey(key) || w < best.get(key)) best.put(key, w);
            }
            for (Map.Entry<Long, Integer> en : best.entrySet()) {
                int cu = (int) (en.getKey() >> 32);
                int cv = (int) (en.getKey().longValue());
                int w = en.getValue();
                compWeighted.get(cu).add(new Edge(cv, w));
            }

            int sourceComp = nodeToComp[source];

            // DAG shortest paths
            Metrics dagMetrics = new Metrics();
            DAGShortestPaths dagsp = new DAGShortestPaths(compWeighted, dagMetrics);
            DAGPathResult sp = dagsp.shortestPaths(sourceComp, topo.getOrder());
            System.out.println("DAG shortest dist (comp-level): " + Arrays.toString(sp.getDist()));
            System.out.println("DAG SP metrics: " + dagMetrics);

            // DAG longest (critical path)
            Metrics longMetrics = new Metrics();
            DAGLongestPath longp = new DAGLongestPath(compWeighted, longMetrics);
            DAGPathResult lp = longp.longestPaths(sourceComp, topo.getOrder());
            DAGLongestPath.OptionalIntIndex bestIdx = DAGLongestPath.findLongest(lp.getDist());
            System.out.println("DAG longest dist (comp-level): " + Arrays.toString(lp.getDist()));
            System.out.println("Critical node (comp) = " + bestIdx.index + ", length = " + bestIdx.value);
            System.out.println("Critical path (comp-level): " + lp.reconstructPath(bestIdx.index));
            System.out.println("DAG Longest metrics: " + longMetrics);

            // Derived order of original tasks after SCC compression:
            List<Integer> derived = new ArrayList<>();
            for (int compId : topo.getOrder()) {
                List<Integer> compNodes = sccRes.getComponents().get(compId);
                derived.addAll(compNodes);
            }
            System.out.println("Derived order of original tasks after SCC compression: " + derived);
        }

        System.out.println("\n=== All datasets processed successfully ===");
    }
}
