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
                    new File("bin/main/").toURI().toURL(),
                    new File("bin/test/").toURI().toURL()
            }).loadClass("triangle.test.TriangleTest");
            testMethod = new TestMethod(testClass, "test1", "bin/main");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void reloadClassTest1() {
        Assert.assertEquals(testClass.getName(), testMethod.reloadClass().getName());
    }

    @Test(expected=Exception.class)
    public void reloadClassTest2() {
        TestMethod test = new TestMethod(null, "test", null);
        test.reloadClass();
    }

    @Test
    public void getTestClassTest() {
        Assert.assertEquals(testClass.getName(), testMethod.getTestClass().getName());
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

    @Test
    public void equalsTest1() {
        try {
            testClass = new URLClassLoader(new URL[]{
                    new File("bin/main/").toURI().toURL(),
                    new File("bin/test/").toURI().toURL()
            }).loadClass("triangle.test.TriangleTest");
            TestMethod test = new TestMethod(testClass, "test1", "bin/main");
            Assert.assertTrue(testMethod.equals(test));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void equalsTest2() {
        String empty = "";
        Assert.assertFalse(testMethod.equals(empty));
    }

    @Test
    public void compareToTest() {

    }
}
