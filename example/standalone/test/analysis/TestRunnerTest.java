package analysis;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.Result;
import prepass.TestMethod;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

public class TestRunnerTest {

    private static TestMethod testMethod;

    @BeforeClass
    public static void setup() {
        try {
            Class<?> testClass = new URLClassLoader(new URL[]{
                    new File("bin/").toURI().toURL()
            }).loadClass("triangle.test.TriangleTest");
            testMethod = new TestMethod(testClass, "test1");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void runTestTest1() {
        Result result = TestRunner.runTest(testMethod, 1000, 1, true);
        Assert.assertTrue(result != null);
        Assert.assertTrue(result.wasSuccessful());
    }

    @Test
    public void runTestTest2() {
        Result result = TestRunner.runTest(testMethod, 0, 1, true);
        Assert.assertTrue(result == null);
    }

    @Test
    public void runTestTest3() {
        Result result = TestRunner.runTest(testMethod, 1000, 1, false);
        Assert.assertTrue(result != null);
        Assert.assertTrue(result.wasSuccessful());
    }

    @Test
    public void runTestTest4() {
        Result result = TestRunner.runTest(testMethod, 0, 1, false);
        Assert.assertTrue(result == null);
    }
}