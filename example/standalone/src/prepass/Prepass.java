package prepass;

import major.mutation.Config;
import org.junit.runner.JUnitCore;
import org.junit.runner.Request;

import java.util.*;

public class Prepass {

    private Class<?> testClass;

    public Prepass(List<Class<?>> testClasses) {
        testClass = testClasses.get(1);
    }

    public HashMap<TestMethod, ArrayList<Integer>> runPrepass() {
        HashMap<TestMethod, ArrayList<Integer>> coverage = new HashMap<>();
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
            coverage.put(testMethod, testCoverage);
            Config.reset();
        }
        return coverage;
    }
}
