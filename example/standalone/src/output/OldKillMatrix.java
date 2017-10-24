package output;

import java.util.ArrayList;

public class OldKillMatrix {

    private ArrayList<TestRow<KillCell>> matrix;
    private TestRow<KillCell> testRow;

    public OldKillMatrix() {
        matrix = new ArrayList<>();
    }

    public void newTestRow(String testName) {
        testRow = new TestRow<>(testName);
    }

    public void addKillCell(KillCell cell) {
        testRow.addMutation(cell);
    }

    public void addTestRow() {
        matrix.add(testRow);
    }

    public void printOutput() {
        for (TestRow<KillCell> test : matrix) {
            System.out.print(test.getTestName() + ": ");
            while (test.hasItems()) {
                KillCell cell = test.getNextMutation();
                System.out.print("[" + cell.getMutationId() + ": " + cell.getKillResult() + "], ");
            }
            System.out.println("");
        }
    }
}
