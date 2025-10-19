package az.edu.java;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.util.Locale;

public class AStarFrame extends JFrame {
    private final Graph graph = new Graph();
    private final JTextArea statsArea = new JTextArea(10, 40);
    private final GraphPanel graphPanel = new GraphPanel(graph);
    private final JLabel modeLabel = new JLabel("Mode: -");

    public AStarFrame() {
        setTitle("A* Visualizer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        JPanel control = new JPanel();
        JButton loadBtn = new JButton("Load Graph");
        JButton ucsBtn = new JButton("Run UCS (h=0)");
        JButton euBtn = new JButton("Run A* Euclidean");
        JButton maBtn = new JButton("Run A* Manhattan");
        JButton sample1 = new JButton("Save astar_small.txt");
        JButton sample2 = new JButton("Save astar_medium.txt");

        control.add(loadBtn);
        control.add(ucsBtn);
        control.add(euBtn);
        control.add(maBtn);
        control.add(sample1);
        control.add(sample2);
        control.add(modeLabel);

        statsArea.setEditable(false);
        JScrollPane scroll = new JScrollPane(statsArea);

        setLayout(new BorderLayout());
        add(control, BorderLayout.NORTH);
        add(graphPanel, BorderLayout.CENTER);
        add(scroll, BorderLayout.EAST);

        loadBtn.addActionListener(e -> loadGraph());
        ucsBtn.addActionListener(e -> runAndShow(Heuristics::zero, "UCS"));
        euBtn.addActionListener(e -> runAndShow(Heuristics::euclidean, "A* Euclidean"));
        maBtn.addActionListener(e -> runAndShow(Heuristics::manhattan, "A* Manhattan"));

        sample1.addActionListener(e -> saveSample("aStar/astar_small.txt", sampleSmall()));
        sample2.addActionListener(e -> saveSample("astar_medium.txt", sampleMedium()));
    }

    private void saveSample(String name, String content) {
        JFileChooser fc = new JFileChooser();
        fc.setSelectedFile(new File(name));
        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try (FileWriter fw = new FileWriter(fc.getSelectedFile())) {
                fw.write(content);
                JOptionPane.showMessageDialog(this, "Saved " + fc.getSelectedFile().getAbsolutePath());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }
    }

    private void loadGraph() {
        JFileChooser fc = new JFileChooser();
        fc.setFileFilter(new FileNameExtensionFilter("Text files", "txt"));
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                String content = new String(Files.readAllBytes(fc.getSelectedFile().toPath()));
                graph.copyFrom(Parser.parse(content));
                graphPanel.resetView();
                modeLabel.setText("Mode: -");
                statsArea.setText("Loaded: " + fc.getSelectedFile().getName());
                repaint();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error reading file: " + ex.getMessage());
            }
        }
    }

    private void runAndShow(Heuristic h, String modeName) {
        if (graph.source == null || graph.dest == null) {
            JOptionPane.showMessageDialog(this, "Graph must include S,<id> and D,<id>");
            return;
        }
        modeLabel.setText("Mode: " + modeName);
        AStar.Result res = AStar.search(graph, graph.source, graph.dest, h);
        statsArea.setText(formatResult(modeName, res));
        graphPanel.setResult(res);
        repaint();
    }

    private String formatResult(String modeName, AStar.Result r) {
        StringBuilder sb = new StringBuilder();
        sb.append("MODE: ").append(modeName).append("\n");
        if (r.found) {
            sb.append("Optimal cost: ").append(r.cost).append("\nPath: ").append(r.path).append("\n");
        } else {
            sb.append("Optimal cost: NO PATH\n");
        }
        sb.append("Expanded: ").append(r.expanded)
                .append("\nPushes: ").append(r.pushes)
                .append("\nMax frontier: ").append(r.maxFrontier)
                .append(String.format(Locale.ROOT, "\nRuntime (s): %.6f\n", r.runtimeSeconds));
        return sb.toString();
    }

    private String sampleSmall() {
        return """
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
                S,1
                D,4
                """;
    }

    private String sampleMedium() {
        StringBuilder sb = new StringBuilder("# medium sample\n");
        int id = 1;
        for (int x = 1; x <= 5; x++)
            for (int y = 1; y <= 7; y++)
                sb.append(id++).append(",").append(x * 10 + y).append("\n");
        sb.append("S,1\nD,35\n");
        return sb.toString();
    }
}
