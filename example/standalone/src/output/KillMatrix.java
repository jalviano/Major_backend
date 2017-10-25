package output;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

import static utils.Constants.*;

public class KillMatrix {

    private int mutantNumber;
    private HashMap<String, ArrayList<Integer>> matrix;

    public KillMatrix(int mutantNumber) {
        this.mutantNumber = mutantNumber;
        matrix = new HashMap<>();
    }

    Set<String> getTests() {
        return matrix.keySet();
    }

    int getRows() {
        return matrix.size();
    }

    int getColumns() {
        return mutantNumber;
    }

    int getKillResult(String test, int mutant) {
        ArrayList<Integer> mutants = matrix.get(test);
        return mutants.get(mutant - 1);
    }

    public void addKillResult(String test, int killResult) {
        if (matrix.containsKey(test)) {
            ArrayList<Integer> mutants = matrix.get(test);
            mutants.add(killResult);
            matrix.put(test, mutants);
        } else {
            ArrayList<Integer> mutants = new ArrayList<>();
            mutants.add(killResult);
            matrix.put(test, mutants);
        }
    }

    public HashMap<String, Integer> getTestsForMutatant(int mutant) {
        HashMap<String, Integer> tests = new HashMap<>();
        for (String test : matrix.keySet()) {
            ArrayList<Integer> mutants = matrix.get(test);
            int killResult = mutants.get(mutant);
            if (killResult != NOT_COVERED) {
                tests.put(test, killResult);
            }
        }
        return tests;
    }

    public HashMap<Integer, Integer> getMutantsForTest(String test) {
        ArrayList<Integer> allMutants = matrix.get(test);
        HashMap<Integer, Integer> mutants = new HashMap<>();
        for (int i = 0; i < allMutants.size(); i++) {
            int killResult = allMutants.get(i);
            if (killResult != NOT_COVERED) {
                mutants.put(i + 1, killResult);
            }
        }
        return mutants;
    }

    public void printOutput() {
        for (String test : matrix.keySet()) {
            System.out.print(test + ": ");
            ArrayList<Integer> mutants = matrix.get(test);
            for (int i = 0; i < mutants.size(); i++) {
                System.out.print("[" + (i + 1) + ": " + mutants.get(i) + "]");
            }
            System.out.println("");
        }
    }
}
