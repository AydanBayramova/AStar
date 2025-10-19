package az.edu.java;

public class Heuristics {
    public static double zero(int nodeId, Graph g, int goalId) { return 0.0; }

    public static double euclidean(int nodeId, Graph g, int goalId) {
        Vertex a = g.vertices.get(nodeId), b = g.vertices.get(goalId);
        if (a == null || b == null) return 0.0;
        return Math.hypot(a.x - b.x, a.y - b.y);
    }

    public static double manhattan(int nodeId, Graph g, int goalId) {
        Vertex a = g.vertices.get(nodeId), b = g.vertices.get(goalId);
        if (a == null || b == null) return 0.0;
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
    }
}
