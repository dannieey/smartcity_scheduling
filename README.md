# Smart City Scheduling 

##  Overview
This project combines two major graph theory concepts — **Strongly Connected Components (SCC)** and **Shortest Paths in Directed Acyclic Graphs (DAGs)** — to optimize task scheduling for Smart City / Smart Campus operations.

### Objectives
1. Detect and compress **cyclic dependencies** using Tarjan’s SCC algorithm.  
2. Build a **DAG** (Directed Acyclic Graph) of condensed components.  
3. Perform **Topological Sorting** (Kahn’s Algorithm).  
4. Compute **Shortest and Longest Paths** in the DAG to find optimal task order and critical paths.



##  Features
- Automatic SCC detection and condensation  
- DAG generation and visualization via adjacency lists  
- Kahn’s topological sort for ordering tasks  
- Shortest and longest path analysis in DAG  
- Full performance metrics collection (DFS visits, relaxations, execution time)  
- Batch execution for **9 datasets (small → large)**


##  Datasets

| Category | Files | Nodes | Description |
|-----------|--------|--------|-------------|
| Small | small1–3 | 6–8 | Compact examples for debugging |
| Medium | medium1–3 | 15–20 | Mid-scale operational tasks |
| Large | large1–3 | 25–40 | High-density, complex scheduling |

Each dataset simulates Smart City operations such as:
- Street cleaning  
- Maintenance scheduling  
- Traffic camera repairs  
- Sensor network updates  



##  Execution

To process **all datasets automatically**:
```bash
java -cp target/classes main.Main


## Metrics Summary (All Datasets)
<img width="923" height="539" alt="image" src="https://github.com/user-attachments/assets/f32c921c-8551-473e-9a7a-44b61e3f5fe6" />

Analysis
SCC Detection

Tarjan’s algorithm efficiently detected cycles.

SCC count scales linearly with graph size.

Example: small1 has 4 SCCs; large3 has 38.

Condensation Graph

Each SCC is represented as one node in a DAG.

This compression simplifies scheduling logic.

Topological Sorting

Kahn’s algorithm successfully sorted all DAGs in linear time O(V + E).

Guarantees valid order for task execution.

Shortest & Longest Path

Shortest path → minimum execution chain (best-case).

Longest path → critical chain (bottleneck tasks).

Example: large3 → critical path length = 66.

