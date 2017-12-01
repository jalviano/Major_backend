package optimization;

import prepass.TestMethod;

import java.util.*;

class TestSorter {

    private static Comparator<TestMethod> comparator = new Comparator<TestMethod>() {
        @Override
        public int compare(TestMethod test1, TestMethod test2) {
            Long ex1 = test1.getExecTime();
            Long ex2 = test2.getExecTime();
            return ex1.compareTo(ex2);
        }
    };

    static ArrayList<TestMethod> sortTests(ArrayList<TestMethod> tests) {
        Collections.sort(tests, comparator);
        return tests;
    }

    static LinkedHashMap<TestMethod, ArrayList<Integer>> sortTests(Map<TestMethod, ArrayList<Integer>> coverage) {
        ArrayList<TestMethod> tests = new ArrayList<>();
        tests.addAll(coverage.keySet());
        Collections.sort(tests, comparator);
        LinkedHashMap<TestMethod, ArrayList<Integer>> sortedCoverage = new LinkedHashMap<>();
        for (TestMethod test : tests) {
            sortedCoverage.put(test, coverage.get(test));
        }
        return sortedCoverage;
    }
}
