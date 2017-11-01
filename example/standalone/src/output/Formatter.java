package output;

import analysis.KillMatrix;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.IntStream;

public class Formatter implements FormatterInterface {

    private KillMatrix matrix;

    public Formatter(KillMatrix matrix) {
        this.matrix = matrix;
    }

    public int[] getMutantIds() {
        int[] array = new int[matrix.getMutantNumber()];
        for (int i = 0; i < matrix.getMutantNumber(); i++) {
            array[i] = i + 1;
        }
        return array;
    }

    public Set<String> getTestNames() {
        return matrix.getTests();
    }

    public float getMutationScore() {
        return matrix.getMutationScore();
    }

    public Integer getKillResult(String test, int mutant) {
        return matrix.getKillResult(test, mutant);
    }

    public HashMap<String, Integer> getTestsForMutant(int mutant) {
        return matrix.getTestsForMutant(mutant);
    }

    public ArrayList<Integer> getMutantsForTest(String test) {
        return matrix.getMutantsForTest(test);
    }
}
