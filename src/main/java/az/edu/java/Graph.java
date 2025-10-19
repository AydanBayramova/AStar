package az.edu.java;

import java.util.*;

public class Graph {
    public Map<Integer, Vertex> vertices = new HashMap<>();
    public List<Edge> edges = new ArrayList<>();
    public Map<Integer, List<Edge>> adj = new HashMap<>();
    public Integer source = null, dest = null;

    public void addVertex(int id, int cell) {
        int x = cell / 10;
        int y = cell % 10;
        vertices.put(id, new Vertex(id, x, y, cell));
        adj.putIfAbsent(id, new ArrayList<>());
    }

    public void addEdge(int u, int v, double w) {
        edges.add(new Edge(u, v, w));
        adj.putIfAbsent(u, new ArrayList<>());
        adj.putIfAbsent(v, new ArrayList<>());
        adj.get(u).add(new Edge(u, v, w));
        adj.get(v).add(new Edge(v, u, w)); // undirected
    }

    public void copyFrom(Graph g) {
        vertices.clear(); edges.clear(); adj.clear();
        for (var e : g.vertices.entrySet()) {
            Vertex v = e.getValue();
            vertices.put(e.getKey(), new Vertex(v.id, v.x, v.y, v.cell));
            adj.put(e.getKey(), new ArrayList<>());
        }
        for (Edge e : g.edges) addEdge(e.u, e.v, e.w);
        source = g.source; dest = g.dest;
    }
}