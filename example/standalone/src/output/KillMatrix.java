package output;

import java.util.ArrayList;
import java.util.HashMap;
import static utils.Constants.*;

public class KillMatrix {

    private HashMap<String, ArrayList<Integer>> matrix;

    public KillMatrix() {
        matrix = new HashMap<>();
    }

    public void addKillResult(String test, int killResult) {
        if (matrix.containsKey(test)) {
            ArrayList<Integer> mutations = matrix.get(test);
            mutations.add(killResult);
            matrix.put(test, mutations);
        } else {
            ArrayList<Integer> mutations = new ArrayList<>();
            mutations.add(killResult);
            matrix.put(test, mutations);
        }
    }

    public HashMap<String, Integer> getTestsForMutation(int mutation) {
        // TODO: Test if implementation is correct
        HashMap<String, Integer> tests = new HashMap<>();
        for (String test : matrix.keySet()) {
            ArrayList<Integer> mutations = matrix.get(test);
            int killResult = mutations.get(mutation);
            if (killResult != NOT_COVERED) {
                tests.put(test, killResult);
            }
        }
        return tests;
    }

    public HashMap<Integer, Integer> getMutationsForTest(String test) {
        // TODO: Test if implementation is correct
        ArrayList<Integer> allMutations = matrix.get(test);
        HashMap<Integer, Integer> mutations = new HashMap<>();
        for (int i = 0; i < allMutations.size(); i++) {
            int killResult = allMutations.get(i);
            if (killResult != NOT_COVERED) {
                mutations.put(i + 1, killResult);
            }
        }
        return mutations;
    }

    public int getKillResult(String test, int mutation) {
        ArrayList<Integer> mutations = matrix.get(test);
        return mutations.get(mutation);
    }

    public void printOutput() {
        for (String test : matrix.keySet()) {
            System.out.print(test + ": ");
            ArrayList<Integer> mutations = matrix.get(test);
            for (int i = 0; i < mutations.size(); i++) {
                System.out.print("[" + (i + 1) + ": " + mutations.get(i) + "]");
            }
            System.out.println("");
        }
    }
}
