package prepass;

import major.mutation.Config;
import org.junit.runner.JUnitCore;
import org.junit.runner.Request;

import java.util.*;

public class DefaultPrepassAnalyzer implements PrepassAnalyzer {

    private List<Class<?>> testClasses;

    /**
     * Default prepass analyzer constructor to run prepass phase of mutation analysis.
     * @param testClasses suite of test classes to be used for analysis
     */
    public DefaultPrepassAnalyzer(List<Class<?>> testClasses) {
        this.testClasses = testClasses;
    }

    /**
     * Method that runs prepass phase of mutation analysis. Measures execution time of test methods for each test
     * class and retrieves mutant coverage information.
     * @return map of mutant coverage results for each test method
     */
    public HashMap<TestMethod, ArrayList<Integer>> runPrepass() {

        // Test classes are loaded before prepass--coverage information is inaccurate for mutated static variables

        HashMap<TestMethod, ArrayList<Integer>> coverage = new HashMap<>();
        HashSet<Integer> totalCovered = new HashSet<>();
        for (Class<?> testClass : testClasses) {
            Collection<TestMethod> testMethods = TestFinder.getTestMethods(testClass);
            for (TestMethod testMethod : testMethods) {
                Config.__M_NO = 0;
                long start = System.currentTimeMillis();
                Request request = Request.method(testClass, testMethod.getName());
                new JUnitCore().run(request);
                long end = System.currentTimeMillis();
                testMethod.setExecTime(end - start);
                ArrayList<Integer> testCoverage = new ArrayList<>();
                List<Integer> covered = Config.getCoverageList();
                testCoverage.addAll(covered);
                totalCovered.addAll(covered);
                coverage.put(testMethod, testCoverage);
                Config.reset();
            }
        }
        System.out.println("Mutants covered: " + totalCovered.size());
        return coverage;
    }
}
