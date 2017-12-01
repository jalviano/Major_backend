package analysis;

import org.junit.runner.JUnitCore;
import org.junit.runner.Request;
import org.junit.runner.Result;
import prepass.TestMethod;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeoutException;

public class TestTask implements Callable<Result>{

    private String classpath;
    private TestMethod test;

    /**
     * Constructor for task to run JUnit test.
     */
    TestTask(String classpath, TestMethod test) {
        this.classpath = classpath;
        this.test = test;
    }

    /**
     * Callable method to run JUnit test and return result.
     */
    @Override
    public Result call() throws TimeoutException, InterruptedException {
        try {
            String classname = test.getTestClass().getName();
            Class<?> c = new URLClassLoader(new URL[]{
                    new File(classpath).toURI().toURL()
            }).loadClass(classname);
            Request request = Request.method(c, test.getName());
            return new JUnitCore().run(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
