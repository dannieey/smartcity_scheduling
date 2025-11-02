package main;

import com.google.gson.*;
import graph.scc.*;
import graph.topo.*;
import graph.dagsp.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Main {

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        System.out.println("=== Smart City Scheduling ===");
        System.out.println("Available datasets in /data:");
        Files.list(Paths.get("data")).filter(f -> f.toString().endsWith(".json"))
                .forEach(f -> System.out.println("- " + f.getFileName()));

        System.out.print("\nEnter dataset filename (e.g., small1.json): ");
        String fileName = sc.nextLine();
        String json = Files.readString(Paths.get("data/" + fileName));

        JsonObject obj = JsonParser.parseString(json).getAsJsonObject();
        int n = obj.get("n").getAsInt();
        int source = obj.get("source").getAsInt();
        JsonArray edgesArr = obj.getAsJsonArray("edges");

        Map<Integer, List<int[]>> graph = new HashMap<>();
        for (int i = 0; i < n; i++) graph.put(i, new ArrayList<>());
        for (JsonElement e : edgesArr) {
            JsonObject edge = e.getAsJsonObject();
            int u = edge.get("u").getAsInt();
            int v = edge.get("v").getAsInt();
            int w = edge.get("w").getAsInt();
            graph.get(u).add(new int[]{v, w});
        }

        System.out.println("\n[1] Running Tarjan SCC...");
        TarjanSCC scc = new TarjanSCC(graph);
        SCCResult result = scc.run();
        result.print();

        System.out.println("\n[2] Building condensation DAG...");
        CondensationGraph cond = new CondensationGraph(graph, result);
        Map<Integer, List<int[]>> dag = cond.build();

        System.out.println("\n[3] Running Topological Sort (Kahn)...");
        KahnTopologicalSort topo = new KahnTopologicalSort(dag);
        List<Integer> topoOrder = topo.sort();
        System.out.println("Topological Order: " + topoOrder);

        System.out.println("\n[4] Shortest Paths in DAG...");
        DAGShortestPaths sp = new DAGShortestPaths(dag);
        sp.shortestPaths(source);

        System.out.println("\n[5] Longest (Critical) Path in DAG...");
        DAGLongestPath lp = new DAGLongestPath(dag);
        lp.longestPath(source);

        System.out.println("\n=== Done ===");
    }
}
