package prepass;

import org.junit.runner.Description;
import org.junit.runner.Request;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

public class TestFinder {

    public static List<Class<?>> loadClasses(String[] classpaths) throws ClassNotFoundException, IOException {
        List<Class<?>> classes = new ArrayList<>();
        for (String classpath : classpaths) {
            String classname = getClassName(classpath);
            String packageBase = classpath.substring(0, classpath.length() - (classname + ".class").length());
            Class<?> cls = new URLClassLoader(new URL[] {
                    new File(packageBase).toURI().toURL()
            }).loadClass(classname);
            classes.add(cls);
        }
        return classes;
    }

    private static String getClassName(String pathToClassFile) throws IOException {
        ProcessBuilder builder = new ProcessBuilder("javap", pathToClassFile);
        Process process = builder.start();
        String classname = null;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while (null != (line = reader.readLine())) {
                if (line.startsWith("public class")) {
                    classname = line.split(" ")[2];
                    break;
                }
            }
        }
        return classname;
    }

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
}
