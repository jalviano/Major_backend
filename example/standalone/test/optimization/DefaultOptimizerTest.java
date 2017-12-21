package optimization;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import prepass.TestMethod;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class DefaultOptimizerTest {

    private static Map<TestMethod, ArrayList<Integer>> coverage;
    private static LinkedHashMap<TestMethod, ArrayList<Integer>> sorted;

    @BeforeClass
    public static void setup() {
        try {
            String mutated = "bin/main";
            coverage = new HashMap<>();
            sorted = new LinkedHashMap<>();
            Class<?> testClass = new URLClassLoader(new URL[]{
                    new File("bin/main/").toURI().toURL(),
                    new File("bin/test/").toURI().toURL()
            }).loadClass("triangle.test.TriangleTest");
            TestMethod testMethod1 = new TestMethod(testClass, "test1", mutated);
            testMethod1.setExecTime(3000);
            coverage.put(testMethod1, null);
            TestMethod testMethod2 = new TestMethod(testClass, "test2", mutated);
            testMethod2.setExecTime(100);
            coverage.put(testMethod2, null);
            TestMethod testMethod3 = new TestMethod(testClass, "test3", mutated);
            testMethod3.setExecTime(400);
            coverage.put(testMethod3, null);
            TestMethod testMethod4 = new TestMethod(testClass, "test4", mutated);
            testMethod4.setExecTime(0);
            coverage.put(testMethod4, null);
            TestMethod testMethod5 = new TestMethod(testClass, "test5", mutated);
            testMethod5.setExecTime(60000);
            coverage.put(testMethod5, null);
            sorted.put(testMethod4, null);
            sorted.put(testMethod2, null);
            sorted.put(testMethod3, null);
            sorted.put(testMethod1, null);
            sorted.put(testMethod5, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void runOptimizationsTest1() {
        DefaultOptimizer optimizer = new DefaultOptimizer(coverage, true);
        Assert.assertEquals(sorted, optimizer.runOptimizations());
    }

    @Test
    public void runOptimizationsTest2() {
        DefaultOptimizer optimizer = new DefaultOptimizer(coverage, false);
        Assert.assertEquals(coverage, optimizer.runOptimizations());
    }
}