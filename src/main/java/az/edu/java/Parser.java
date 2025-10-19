package az.edu.java;

public class Parser {
    public static Graph parse(String content) {
        Graph g = new Graph();
        String[] lines = content.split("\\r?\\n");
        for (String raw : lines) {
            String line = raw.trim();
            if (line.isEmpty() || line.startsWith("#")) continue;
            String[] parts = line.split(",");
            if (parts.length == 2 && parts[0].equalsIgnoreCase("S")) {
                g.source = Integer.parseInt(parts[1].trim());
            } else if (parts.length == 2 && parts[0].equalsIgnoreCase("D")) {
                g.dest = Integer.parseInt(parts[1].trim());
            } else if (parts.length == 2) {
                int id = Integer.parseInt(parts[0].trim());
                int cell = Integer.parseInt(parts[1].trim());
                g.addVertex(id, cell);
            } else if (parts.length == 3) {
                int u = Integer.parseInt(parts[0].trim());
                int v = Integer.parseInt(parts[1].trim());
                double w = Double.parseDouble(parts[2].trim());
                g.addEdge(u, v, w);
            }
        }
        return g;
    }
}