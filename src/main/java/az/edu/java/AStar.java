package az.edu.java;

import java.util.*;

public class AStar {
    static class NodeEntry {
        int id; double f, g;
        NodeEntry(int id, double f, double g) { this.id = id; this.f = f; this.g = g; }
    }

    public static class Result {
        boolean found; double cost; List<Integer> path = new ArrayList<>();
        int expanded, pushes, maxFrontier; double runtimeSeconds;
        Set<Integer> expandedNodes = new HashSet<>();
        Set<Integer> frontierNodes = new HashSet<>();
        Set<Integer> pathSet = new HashSet<>();
    }

    public static Result search(Graph g, int start, int goal, Heuristic h) {
        Result res = new Result();
        long t0 = System.nanoTime();

        Map<Integer, Double> gCost = new HashMap<>();
        Map<Integer, Integer> parent = new HashMap<>();
        PriorityQueue<NodeEntry> pq = new PriorityQueue<>(
                Comparator.comparingDouble((NodeEntry n) -> n.f).thenComparingInt(n -> n.id));

        gCost.put(start, 0.0);
        pq.add(new NodeEntry(start, h.h(start, g, goal), 0.0));
        res.pushes++;

        while (!pq.isEmpty()) {
            res.maxFrontier = Math.max(res.maxFrontier, pq.size());
            NodeEntry cur = pq.poll();
            if (!Objects.equals(cur.g, gCost.get(cur.id))) continue;

            res.expanded++;
            res.expandedNodes.add(cur.id);
            res.frontierNodes.remove(cur.id);

            if (cur.id == goal) {
                res.found = true;
                res.cost = cur.g;
                for (int n = goal; n != 0; n = parent.getOrDefault(n, 0)) {
                    res.path.add(0, n);
                    res.pathSet.add(n);
                    if (n == start) break;
                }
                break;
            }

            for (Edge e : g.adj.getOrDefault(cur.id, List.of())) {
                int nei = e.v;
                double tentative = cur.g + e.w;
                if (tentative < gCost.getOrDefault(nei, Double.POSITIVE_INFINITY)) {
                    gCost.put(nei, tentative);
                    parent.put(nei, cur.id);
                    pq.add(new NodeEntry(nei, tentative + h.h(nei, g, goal), tentative));
                    res.pushes++;
                    res.frontierNodes.add(nei);
                }
            }
        }

        res.runtimeSeconds = (System.nanoTime() - t0) / 1e9;
        return res;
    }
}
