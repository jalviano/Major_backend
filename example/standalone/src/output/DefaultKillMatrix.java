package output;

import utils.Outcome;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import static utils.Outcome.*;

public class DefaultKillMatrix implements KillMatrix, KillMatrixBuilder {

    private float mutationScore;
    private int mutantNumber;
    private HashMap<String, ArrayList<Outcome>> matrix;

    public DefaultKillMatrix(int mutantNumber) {
        this.mutantNumber = mutantNumber;
        matrix = new HashMap<>();
    }

    public void setMutationScore(float mutationScore) {
        this.mutationScore = mutationScore;
    }

    public void addKillResult(String test, Outcome killResult) {
        if (matrix.containsKey(test)) {
            ArrayList<Outcome> mutants = matrix.get(test);
            mutants.add(killResult);
            matrix.put(test, mutants);
        } else {
            ArrayList<Outcome> mutants = new ArrayList<>();
            mutants.add(killResult);
            matrix.put(test, mutants);
        }
    }

    public int[] getMutantIds() {
        int[] array = new int[mutantNumber];
        for (int i = 0; i < mutantNumber; i++) {
            array[i] = i + 1;
        }
        return array;
    }

    public Set<String> getTestNames() {
        return matrix.keySet();
    }

    public int getMutantNumber() {
        return mutantNumber;
    }

    public float getMutationScore() {
        return mutationScore;
    }

    public Outcome getKillResult(String test, int mutant) {
        ArrayList<Outcome> mutants = matrix.get(test);
        return mutants.get(mutant - 1);
    }

    public HashMap<String, Outcome> getTestsForMutant(int mutant) {
        HashMap<String, Outcome> tests = new HashMap<>();
        for (String test : matrix.keySet()) {
            ArrayList<Outcome> mutants = matrix.get(test);
            Outcome killResult = mutants.get(mutant - 1);
            if (killResult != NOT_COVERED) {
                tests.put(test, killResult);
            }
        }
        return tests;
    }

    public ArrayList<Outcome> getMutantsForTest(String test) {
        return matrix.get(test);
    }

    public HashMap<Integer, Outcome> getMutantsForTest_(String test) {
        ArrayList<Outcome> allMutants = matrix.get(test);
        HashMap<Integer, Outcome> mutants = new HashMap<>();
        for (int i = 0; i < allMutants.size(); i++) {
            Outcome killResult = allMutants.get(i);
            if (killResult != NOT_COVERED) {
                mutants.put(i + 1, killResult);
            }
        }
        return mutants;
    }
}
