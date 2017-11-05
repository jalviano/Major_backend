package output;

import utils.Outcome;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public interface KillMatrix {

    int[] getMutantIds();
    Set<String> getTestNames();
    float getMutationScore();
    Outcome getKillResult(String test, int mutant);
    HashMap<String, Outcome> getTestsForMutant(int mutant);
    ArrayList<Outcome> getMutantsForTest(String test);
}
