package prepass;

import org.junit.runner.Description;
import org.junit.runner.Request;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class TestFinder {

    static Collection<TestMethod> getTestMethods(Class<?> cls) {
        Vector<TestMethod> tests = new Vector<>();
        for (Description test : Request.aClass(cls).getRunner().getDescription().getChildren()) {
            if (test.getMethodName() == null) {
                for (Method m : cls.getMethods()) {
                    tests.add(new TestMethod(cls, m.getName() + test.getDisplayName()));
                }
            } else {
                tests.add(new TestMethod(test.getTestClass(), test.getMethodName()));
            }
        }
        Collections.sort(tests);
        return tests;
    }

    public static Class getClassFromFile(String classPath, String className) throws MalformedURLException,
            ClassNotFoundException {
        URLClassLoader loader = new URLClassLoader(new URL[] {
                new URL("file://" + classPath)
        });
        return loader.loadClass(className);
    }

    public static List<Class<?>> getAllClasses(String mutatedClassPath, String mutatedClassName, String testClassPath,
                                               String testClassName) throws MalformedURLException {
        List<Class<?>> classes = new ArrayList<>();
        URLClassLoader loader = new URLClassLoader(new URL[] {
                new URL("file://" + mutatedClassPath),
                new URL("file://" + testClassPath)
        });
        try {
            classes.add(loader.loadClass(mutatedClassName));
            classes.add(loader.loadClass(testClassName));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return classes;
    }
}
