package analysis;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import prepass.TestMethod;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

public class TestTaskTest {

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
    public void callTest1() {
        try {
            Assert.assertTrue(new TestTask(testMethod, true).call().wasSuccessful());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void callTest2() {
        try {
            Assert.assertTrue(new TestTask(testMethod, false).call().wasSuccessful());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}