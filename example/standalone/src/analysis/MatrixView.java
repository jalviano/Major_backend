package analysis;

import javax.swing.*;
import java.awt.*;
import static utils.Constants.*;

public class MatrixView {

    private JFrame gui;
    private JPanel results;
    private KillMatrix matrix;
    private float mutationScore;

    public MatrixView(KillMatrix matrix, float mutationScore) {
        this.matrix = matrix;
        this.mutationScore = mutationScore;
        gui = new JFrame("Mutation Analysis");
        draw();
        gui.setVisible(true);
    }

    private void draw() {
        gui.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        gui.setSize(new Dimension(1000, 600));
        gui.setResizable(true);
        JPanel gridPanel = new JPanel(new FlowLayout());
        results = new JPanel(new GridLayout(matrix.getRows() + 1, matrix.getColumns() + 1));
        JPanel score = new JPanel(new FlowLayout());
        gridPanel.add(results, BorderLayout.CENTER);
        score.add(new JTextArea("Mutation score: " + Float.toString(mutationScore)));
        gui.add(gridPanel, BorderLayout.NORTH);
        gui.add(score, BorderLayout.SOUTH);
        populateResults();
    }

    private void populateResults() {
        final int WIDTH = 8;
        final int HEIGHT = 8;
        JLabel empty = new JLabel();
        empty.setText("");
        empty.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        empty.setMinimumSize(new Dimension(WIDTH, HEIGHT));
        results.add(empty);
        for (int i = 1; i <= matrix.getColumns(); i++) {
            JLabel result = new JLabel();
            result.setText(Integer.toString(i));
            result.setPreferredSize(new Dimension(WIDTH, HEIGHT));
            result.setMinimumSize(new Dimension(WIDTH, HEIGHT));
            result.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 8));
            results.add(result);
        }
        for (String test : matrix.getTests()) {
            JLabel testResult = new JLabel();
            testResult.setText(test);
            testResult.setPreferredSize(new Dimension(WIDTH, HEIGHT));
            testResult.setMinimumSize(new Dimension(WIDTH, HEIGHT));
            testResult.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 8));
            results.add(testResult);
            for (int i = 1; i <= matrix.getColumns(); i++) {
                Integer killResult = matrix.getKillResult(test, i);
                JLabel result = new JLabel();
                result.setText("");
                result.setOpaque(true);
                result.setPreferredSize(new Dimension(WIDTH, HEIGHT));
                result.setMinimumSize(new Dimension(WIDTH, HEIGHT));
                if (killResult == ASSERTION_ERROR) {
                    result.setBackground(Color.RED);
                } else if (killResult == UNKILLED) {
                    result.setBackground(Color.GREEN);
                } else if (killResult == TIMEOUT) {
                    result.setBackground(Color.YELLOW);
                } else if (killResult == GENERAL_EXCEPTION) {
                    result.setBackground(Color.BLUE);
                } else {
                    result.setBackground(Color.WHITE);
                }
                results.add(result);
            }
        }
    }
}
