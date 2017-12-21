package prepass;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

public class TestFinderTest {

    @Test
    public void loadClassesTest() {
        try {
            String testDirectory = "bin/test/triangle/test/";
            Assert.assertEquals("triangle.test.TriangleTest",
                    TestFinder.loadClasses(testDirectory).get(0).getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getTestMethodsTest() {
        try {
            Class<?> c = new URLClassLoader(new URL[] {
                    new File("bin/main/").toURI().toURL(),
                    new File("bin/test/").toURI().toURL()
            }).loadClass("triangle.test.TriangleTest");
            TestMethod testMethod = (TestMethod) TestFinder.getTestMethods(c, "bin/main").toArray()[0];
            Assert.assertEquals("test1", testMethod.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
