package optimization;

import prepass.TestMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DefaultOptimizer {

    private Map<TestMethod, ArrayList<Integer>> coverage;
    private boolean sortOptimization;

    public DefaultOptimizer(Map<TestMethod, ArrayList<Integer>> coverage, boolean sortOptimization) {
        this.coverage = coverage;
        this.sortOptimization = sortOptimization;
    }

    public Map<TestMethod, ArrayList<Integer>> runOptimizations() {
        if (sortOptimization) {
            sortTests();
        }
        return coverage;
    }

    private void sortTests() {
        coverage = TestSorter.sortTests(coverage);
    }
}
