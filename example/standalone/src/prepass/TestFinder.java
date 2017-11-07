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

    public static List<Class<?>> loadClasses(String[] testDirectories) throws IOException {
        List<Class<?>> classes = new ArrayList<>();
        for (String directory : testDirectories) {
            File folder = new File(directory);
            String[] paths = folder.list();
            getClassPaths(directory, paths, classes);
        }
        //System.out.println("Test class number: " + classes.size());
        return classes;
    }

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
                } else if (path.endsWith(".class")) {
                    String classname = getClassName(filepath);
                    String packageBase = filepath.substring(0, filepath.length() - (classname + ".class").length());
                    try {
                        Class<?> cls = new URLClassLoader(new URL[]{
                                new File(packageBase).toURI().toURL()
                        }).loadClass(classname);
                        classes.add(cls);
                    } catch (ClassNotFoundException e) {
                        System.out.println("ClassNotFoundException: " + filepath);
                    } catch (NullPointerException e) {
                        System.out.println("NullPointerException: " + filepath);
                    }
                    noSubDirectory = true;
                }
            }
            hasSubdirectory = !noSubDirectory;
        }
    }

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
}
