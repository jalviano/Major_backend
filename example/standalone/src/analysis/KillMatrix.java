package analysis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

import static utils.Constants.*;

public class KillMatrix {

    private float mutationScore;
    private int mutantNumber;
    private HashMap<String, ArrayList<Integer>> matrix;

    KillMatrix(int mutantNumber) {
        this.mutantNumber = mutantNumber;
        matrix = new HashMap<>();
    }

    void setMutationScore(float mutationScore) {
        this.mutationScore = mutationScore;
    }

    int getRows() {
        return matrix.size();
    }

    int getColumns() {
        return mutantNumber;
    }

    void addKillResult(String test, Integer killResult) {
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

    public Set<String> getTests() {
        return matrix.keySet();
    }

    public int getMutantNumber() {
        return mutantNumber;
    }

    public float getMutationScore() {
        return mutationScore;
    }

    public Integer getKillResult(String test, int mutant) {
        ArrayList<Integer> mutants = matrix.get(test);
        return mutants.get(mutant - 1);
    }

    public HashMap<String, Integer> getTestsForMutant(int mutant) {
        HashMap<String, Integer> tests = new HashMap<>();
        for (String test : matrix.keySet()) {
            ArrayList<Integer> mutants = matrix.get(test);
            Integer killResult = mutants.get(mutant);
            if (killResult != NOT_COVERED) {
                tests.put(test, killResult);
            }
        }
        return tests;
    }

    public ArrayList<Integer> getMutantsForTest(String test) {
        return matrix.get(test);
    }

    public HashMap<Integer, Integer> getMutantsForTest_(String test) {
        ArrayList<Integer> allMutants = matrix.get(test);
        HashMap<Integer, Integer> mutants = new HashMap<>();
        for (int i = 0; i < allMutants.size(); i++) {
            Integer killResult = allMutants.get(i);
            if (killResult != NOT_COVERED) {
                mutants.put(i + 1, killResult);
            }
        }
        return mutants;
    }
}
