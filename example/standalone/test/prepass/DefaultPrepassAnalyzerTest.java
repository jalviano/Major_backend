package prepass;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class DefaultPrepassAnalyzerTest {

    private static DefaultPrepassAnalyzer prepassAnalyzer;

    @BeforeClass
    public static void setup() {
        try {
            List<Class<?>> testClasses = new ArrayList<>();
            Class<?> testClass = new URLClassLoader(new URL[]{
                    new File("bin/").toURI().toURL()
            }).loadClass("triangle.test.TriangleTest");
            testClasses.add(testClass);
            prepassAnalyzer = new DefaultPrepassAnalyzer(testClasses);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void runPrepassTest() {
        ArrayList<Integer> test16Coverage = new ArrayList<>(
                Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 20, 21, 22, 23, 24));
        HashMap<TestMethod, ArrayList<Integer>> coverage = prepassAnalyzer.runPrepass();
        //printCoverage(coverage);
        Assert.assertTrue(coverage.containsValue(test16Coverage));
    }

    private void printCoverage(HashMap<TestMethod, ArrayList<Integer>> coverage) {
        System.out.println("Covered mutants: ");
        for (TestMethod key : coverage.keySet()) {
            System.out.println(key.getName() + ": " + coverage.get(key).toString());
        }
    }
}
