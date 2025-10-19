 A* Search Visualizer

A Java-based implementation of the A* search algorithm with multiple heuristics and a simple interactive visualization.
The program finds the optimal path between two nodes in a 2D grid graph and compares the performance of different heuristic modes.

 Features
🔹 A* Search Modes

Zero heuristic (UCS) — behaves like Uniform Cost Search

Euclidean distance heuristic — straight-line (L2) distance

Manhattan distance heuristic — grid-based (L1) distance

 Input Format (Text File)

Example: astar_small.txt
# vertices
1,11
2,12
3,22
4,32
5,33

# edges
1,2,7
2,3,4
3,4,3
2,5,6
5,4,2

# source and destination
S,1
D,4

