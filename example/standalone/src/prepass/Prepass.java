package prepass;

import major.mutation.Config;
import org.junit.runner.JUnitCore;
import org.junit.runner.Request;
import org.junit.runner.Result;
import triangle.Triangle;

import java.lang.reflect.Method;
import java.util.*;

public class Prepass {

    private List<Class<?>> testClasses;

    public Prepass(List<Class<?>> testClasses) {
        this.testClasses = testClasses;
    }

    public HashMap<Method, ArrayList<Integer>> runPrepass() {
        HashMap<Method, ArrayList<Integer>> coverage = new HashMap<>();
        for (Class<?> testClass : testClasses) {
            Method[] testMethods = testClass.getDeclaredMethods();
            for (Method testMethod : testMethods) {
                Config.__M_NO = 0;
                long start = System.currentTimeMillis();
                //Triangle.classify(1, 1, 1);
                Request request = Request.method(testClass, testMethod.getName());
                Result result = new JUnitCore().run(request);
                long end = System.currentTimeMillis();
                //testMethod.setExecTime(end - start);
                ArrayList<Integer> testCoverage = new ArrayList<>();
                List<Integer> covered = Config.getCoverageList();
                testCoverage.addAll(covered);
                coverage.put(testMethod, testCoverage);
                Config.reset();
            }
        }
        return coverage;
    }
}
