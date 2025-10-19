package az.edu.java;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GraphPanel extends JPanel {
    private Graph graph;
    private AStar.Result lastResult = null;
    private int padding = 30;
    private double scale = 50.0; // pixels per grid unit

    public GraphPanel(Graph g) {
        this.graph = g;
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(700, 700));
    }

    public void resetView() { lastResult = null; }

    public void setResult(AStar.Result r) { this.lastResult = r; }

    private Point2D nodeToPoint(Vertex v) {
        double x = padding + v.x * scale;
        double y = padding + v.y * scale;
        return new Point2D((int) x, (int) y);
    }

    @Override
    protected void paintComponent(Graphics g0) {
        super.paintComponent(g0);
        Graphics2D g2 = (Graphics2D) g0;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);


        g2.setStroke(new BasicStroke(2));
        for (Edge e : graph.edges) {
            Vertex a = graph.vertices.get(e.u);
            Vertex b = graph.vertices.get(e.v);
            Point2D pa = nodeToPoint(a);
            Point2D pb = nodeToPoint(b);
            g2.setColor(Color.LIGHT_GRAY);
            g2.drawLine(pa.x, pa.y, pb.x, pb.y);
            g2.setColor(Color.DARK_GRAY);
            g2.setFont(g2.getFont().deriveFont(10f));
            int mx = (pa.x + pb.x) / 2, my = (pa.y + pb.y) / 2;
            g2.drawString(String.format("%.1f", e.w), mx + 3, my - 3);
        }

        if (lastResult != null && lastResult.found) {
            g2.setStroke(new BasicStroke(4));
            g2.setColor(Color.RED);
            List<Integer> p = lastResult.path;
            for (int i = 0; i + 1 < p.size(); i++) {
                Vertex a = graph.vertices.get(p.get(i));
                Vertex b = graph.vertices.get(p.get(i + 1));
                Point2D pa = nodeToPoint(a);
                Point2D pb = nodeToPoint(b);
                g2.drawLine(pa.x, pa.y, pb.x, pb.y);
            }
        }

        for (Vertex v : graph.vertices.values()) {
            Point2D p = nodeToPoint(v);
            int r = 12;
            Color fill = Color.WHITE;
            if (lastResult != null) {
                if (lastResult.expandedNodes.contains(v.id)) fill = new Color(173, 216, 230);
                else if (lastResult.frontierNodes.contains(v.id)) fill = new Color(240, 230, 140);
                if (lastResult.found && lastResult.pathSet.contains(v.id)) fill = Color.PINK;
            }
            g2.setColor(Color.BLACK);
            g2.fillOval(p.x - r, p.y - r, r * 2, r * 2);
            g2.setColor(fill);
            g2.fillOval(p.x - r + 3, p.y - r + 3, (r - 3) * 2, (r - 3) * 2);
            g2.setColor(Color.BLACK);
            g2.drawString(String.valueOf(v.id), p.x - 6, p.y + 4);
        }


        if (graph.source != null && graph.vertices.containsKey(graph.source)) {
            Point2D p = nodeToPoint(graph.vertices.get(graph.source));
            g2.setColor(Color.GREEN.darker());
            g2.drawString("S", p.x - 10, p.y - 10);
        }
        if (graph.dest != null && graph.vertices.containsKey(graph.dest)) {
            Point2D p = nodeToPoint(graph.vertices.get(graph.dest));
            g2.setColor(Color.BLUE.darker());
            g2.drawString("D", p.x - 10, p.y - 10);
        }
    }

    static class Point2D { int x, y; Point2D(int x, int y){this.x=x; this.y=y;} }
}