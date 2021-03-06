package prepass;

import org.junit.runner.Description;
import org.junit.runner.Request;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

public class TestFinder {

    /**
     * Loads test suite classes in provided directory.
     * @param testDirectory project directory in which to search for test classes
     * @return list of test classes
     */
    public static List<Class<?>> loadClasses(String testDirectory) throws IOException {
        List<Class<?>> classes = new ArrayList<>();
        getClassPaths(testDirectory, classes);
        System.out.println("Test class number: " + classes.size());
        return classes;
    }

    /**
     * Loads test methods for a given class in the test suite.
     * @param cls test suite class from which methods are retrieved
     * @return collection of test methods for class
     */
    static Collection<TestMethod> getTestMethods(Class<?> cls, String mutatedDirectory) {
        Vector<TestMethod> tests = new Vector<>();
        for (Description test : Request.aClass(cls).getRunner().getDescription().getChildren()) {
            if (test.getMethodName() == null) {
                for (Method m : cls.getMethods()) {
                    if (looksLikeTest(m)) {
                        tests.add(new TestMethod(cls, m.getName() + test.getDisplayName(), mutatedDirectory));
                    }
                }
            } else {
                tests.add(new TestMethod(test.getTestClass(), test.getMethodName(), mutatedDirectory));
            }
        }
        Collections.sort(tests);
        return tests;
    }

    /**
     * Finds full file paths for all test classes in provided root directory. Recursively searches through all sub
     * folders in directory to find all classes in the test suite.
     * @param directoryName current directory file path
     * @param classes cumulative list of classes found in root directory and its sub folders
     */
    private static void getClassPaths(String directoryName, List<Class<?>> classes) {
        File directory = new File(directoryName);
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    String filepath = file.getPath();
                    if (filepath.contains("Test.class") || filepath.contains("Tests.class")) {
                        try {
                            String classname = getClassName(filepath);
                            String packageBase = filepath.substring(0, filepath.length() - (classname + ".class").length());
                            Class<?> cls = new URLClassLoader(new URL[]{
                                    new File(packageBase).toURI().toURL()
                            }).loadClass(classname);
                            classes.add(cls);
                        } catch (ClassNotFoundException e) {
                            System.out.println("ClassNotFoundException: " + filepath);
                        } catch (NullPointerException e) {
                            System.out.println("NullPointerException: " + filepath);
                        } catch (Exception e) {
                            System.out.println("Exception: " + filepath);
                        }
                    }
                } else if (file.isDirectory()) {
                    getClassPaths(file.getAbsolutePath(), classes);
                }
            }
        }
    }

    /**
     * Gets full name (including package) for test class
     * @param pathToClassFile file path for test class to be named
     * @return full name of test class
     */
    private static String getClassName(String pathToClassFile) throws IOException {
        ProcessBuilder builder = new ProcessBuilder("javap", pathToClassFile);
        Process process = builder.start();
        String classname = null;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while (null != (line = reader.readLine())) {
                if (line.startsWith("public class")) {
                    if (line.contains("<")) {
                        String pre = line.split(" ")[2];
                        classname = pre.split("<")[0];
                        break;
                    } else {
                        classname = line.split(" ")[2];
                        break;
                    }
                } else if (line.startsWith("public final class")) {
                    if (line.contains("<")) {
                        String pre = line.split(" ")[3];
                        classname = pre.split("<")[0];
                        break;
                    } else {
                        classname = line.split(" ")[3];
                        break;
                    }
                }
            }
        }
        return classname;
    }

    private static boolean looksLikeTest(Method m) {
        return (m.isAnnotationPresent(org.junit.Test.class)
                || (m.getParameterTypes().length == 0
                    && m.getReturnType().equals(Void.TYPE)
                    && Modifier.isPublic(m.getModifiers())
                    && (m.getName().startsWith("test") || m.getName().endsWith("Test")
                        || m.getName().startsWith("Test") || m.getName().endsWith("test"))));
    }
}
