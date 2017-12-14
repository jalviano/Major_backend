package prepass;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

public class TestMethodTest {

    private static Class<?> testClass;
    private static TestMethod testMethod;

    @BeforeClass
    public static void setup() {
        try {
            testClass = new URLClassLoader(new URL[]{
                    new File("bin/").toURI().toURL()
            }).loadClass("triangle.test.TriangleTest");
            testMethod = new TestMethod(testClass, "test1");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void reloadClassTest() {
        Assert.assertEquals(testClass.getName(), testMethod.reloadClass().getName());
    }

    @Test
    public void getTestClassTest() {
        Assert.assertEquals(testClass, testMethod.getTestClass());
    }

    @Test
    public void getNameTest() {
        Assert.assertEquals("test1", testMethod.getName());
    }

    @Test
    public void getLongNameTest() {
        Assert.assertEquals("triangle.test.TriangleTest[test1]", testMethod.getLongName());

    }

    @Test
    public void execTimeTest() {
        testMethod.setExecTime(1000);
        Assert.assertEquals(1000, testMethod.getExecTime());
    }
}
