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
            String[] testDirectories = {"bin/triangle/test/"};
            Assert.assertEquals("triangle.test.TriangleTest", TestFinder.loadClasses(testDirectories).get(0).getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getTestMethodsTest() {
        try {
            Class<?> c = new URLClassLoader(new URL[] {
                    new File("bin/").toURI().toURL()
            }).loadClass("triangle.test.TriangleTest");
            TestMethod testMethod = (TestMethod) TestFinder.getTestMethods(c).toArray()[0];
            Assert.assertEquals("test1", testMethod.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
