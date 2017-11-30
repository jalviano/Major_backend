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

    /**
     * Loads test suite classes in provided directory.
     * @param testDirectories array of directories in which to search for test classes
     * @return list of test classes
     */
    public static List<Class<?>> loadClasses(String[] testDirectories) throws IOException {
        List<Class<?>> classes = new ArrayList<>();
        for (String directory : testDirectories) {
            File folder = new File(directory);
            String[] paths = folder.list();
            getClassPaths(directory, paths, classes);
        }
        System.out.println("Test class number: " + classes.size());
        return classes;
    }

    /**
     * Loads test methods for a given class in the test suite.
     * @param cls test suite class from which methods are retrieved
     * @return collection of test methods for class
     */
    static Collection<TestMethod> getTestMethods(Class<?> cls) {
        Vector<TestMethod> tests = new Vector<>();
        for (Description test : Request.aClass(cls).getRunner().getDescription().getChildren()) {
            if (test.getMethodName() == null) {
                for (Method m : cls.getMethods()) {
                    tests.add(new TestMethod(cls, m.getName() + test.getDisplayName()));
                }
            } else if (test.getMethodName().startsWith("test") || test.getMethodName().endsWith("Test")) {
                tests.add(new TestMethod(test.getTestClass(), test.getMethodName()));
            }
        }
        Collections.sort(tests);
        return tests;
    }

    /**
     * Finds full file paths for all test classes in provided root directory. Recursively searches through all sub
     * folders in directory to find all classes in the test suite.
     * @param directory current directory file path
     * @param paths all possible sub folders in current directory
     * @param classes cumulative list of classes found in root directory and its sub folders
     */
    private static void getClassPaths(String directory, String[] paths, List<Class<?>> classes) throws IOException {
        boolean hasSubdirectory = true;
        while (hasSubdirectory) {
            boolean noSubDirectory = true;
            for (String path : paths) {
                String filepath = directory + path;
                File subfolder = new File(filepath);
                if (subfolder.isDirectory()) {
                    noSubDirectory = false;
                    getClassPaths(filepath + "/", subfolder.list(), classes);
                } else if (path.contains("Test.class")) {
                    String classname = getClassName(filepath);
                    String packageBase = filepath.substring(0, filepath.length() - (classname + ".class").length());
                    try {
                        Class<?> cls = new URLClassLoader(new URL[] {
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
                    noSubDirectory = true;
                } else {
                    noSubDirectory = true;
                }
            }
            hasSubdirectory = !noSubDirectory;
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
                }
            }
        }
        return classname;
    }
}
