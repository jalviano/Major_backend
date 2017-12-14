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

    /**
     * Data structure for mutation analysis results. Data is stored as a map of test method names and a list of outcomes
     * for each test method key. Mutant id enabled for test run is identified by index of list of outcomes plus one.
     * @param mutantNumber total number of mutants in mutants.log file
     */
    public DefaultKillMatrix(int mutantNumber) {
        this.mutantNumber = mutantNumber;
        matrix = new HashMap<>();
    }

    /**
     * Sets final mutation score value for kill matrix.
     */
    public void setMutationScore(float mutationScore) {
        this.mutationScore = mutationScore;
    }

    /**
     * Adds new outcome to kill matrix for provided test method. The enabled mutant id is identified by the index in the
     * list of outcomes plus one.
     * @param test name of test method
     * @param killResult outcome of JUnit test
     */
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

    /**
     * Retrieves array of all mutant id integers.
     */
    public int[] getMutantIds() {
        int[] array = new int[mutantNumber];
        for (int i = 0; i < mutantNumber; i++) {
            array[i] = i + 1;
        }
        return array;
    }

    /**
     * Retrieves set of all test names in test suite.
     */
    public Set<String> getTestNames() {
        return matrix.keySet();
    }

    /**
     * Retrieves total mutant number in mutants.log file.
     */
    public int getMutantNumber() {
        return mutantNumber;
    }

    /**
     * Retrieves mutation score value for kill matrix.
     */
    public float getMutationScore() {
        return mutationScore;
    }

    /**
     * Retrieves outcome from kill matrix for test and mutant id pair.
     * @param test name of test method
     * @param mutant id of mutant
     * @return outcome when running provided test with mutant enabled
     */
    public Outcome getKillResult(String test, int mutant) {
        ArrayList<Outcome> mutants = matrix.get(test);
        return mutants.get(mutant - 1);
    }

    /**
     * Retrieves map of all test methods that cover provided mutant and their outcomes when mutant is enabled.
     */
    public HashMap<String, Outcome> getTestsForMutant(int mutant) {
        HashMap<String, Outcome> tests = new HashMap<>();
        for (String test : matrix.keySet()) {
            ArrayList<Outcome> mutants = matrix.get(test);
            Outcome killResult = mutants.get(mutant - 1);
            if (killResult != NOT_COVERED && killResult != NOT_TESTED) {
                tests.put(test, killResult);
            }
        }
        return tests;
    }

    /**
     * Retrieves list of outcomes for provided test where enabled mutant id is represented by index in list plus one.
     */
    public ArrayList<Outcome> getMutantsForTest(String test) {
        return matrix.get(test);
    }

    /**
     * Retrieves map of all mutants covered by provided test and their outcomes when mutant is enabled.
     */
    public HashMap<Integer, Outcome> getMutantsForTestSparse(String test) {
        ArrayList<Outcome> allMutants = matrix.get(test);
        HashMap<Integer, Outcome> mutants = new HashMap<>();
        for (int i = 0; i < allMutants.size(); i++) {
            Outcome killResult = allMutants.get(i);
            if (killResult != NOT_COVERED && killResult != NOT_TESTED) {
                mutants.put(i + 1, killResult);
            }
        }
        return mutants;
    }
}
