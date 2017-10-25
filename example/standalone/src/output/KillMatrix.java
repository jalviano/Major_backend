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

    public int getKillResult(String test, int mutation) {
        ArrayList<Integer> mutations = matrix.get(test);
        return mutations.get(mutation);
    }

    public HashMap<String, Integer> getTestsForMutatant(int mutatant) {
        HashMap<String, Integer> tests = new HashMap<>();
        for (String test : matrix.keySet()) {
            ArrayList<Integer> mutatants = matrix.get(test);
            int killResult = mutatants.get(mutatant);
            if (killResult != NOT_COVERED) {
                tests.put(test, killResult);
            }
        }
        return tests;
    }

    public HashMap<Integer, Integer> getMutantsForTest(String test) {
        ArrayList<Integer> allMutants = matrix.get(test);
        HashMap<Integer, Integer> mutatants = new HashMap<>();
        for (int i = 0; i < allMutants.size(); i++) {
            int killResult = allMutants.get(i);
            if (killResult != NOT_COVERED) {
                mutatants.put(i + 1, killResult);
            }
        }
        return mutatants;
    }

    public void printOutput() {
        for (String test : matrix.keySet()) {
            System.out.print(test + ": ");
            ArrayList<Integer> mutatants = matrix.get(test);
            for (int i = 0; i < mutatants.size(); i++) {
                System.out.print("[" + (i + 1) + ": " + mutatants.get(i) + "]");
            }
            System.out.println("");
        }
    }
}
