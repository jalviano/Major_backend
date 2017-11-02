package analysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public interface MatrixInterface {

    int[] getMutantIds();
    Set<String> getTestNames();
    float getMutationScore();
    Integer getKillResult(String test, int mutant);
    HashMap<String, Integer> getTestsForMutant(int mutant);
    ArrayList<Integer> getMutantsForTest(String test);
}
