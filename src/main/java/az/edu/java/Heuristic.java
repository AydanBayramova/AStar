package az.edu.java;


public interface Heuristic {
    double h(int nodeId, Graph g, int goalId);
}
