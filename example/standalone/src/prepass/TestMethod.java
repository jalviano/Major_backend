package prepass;

import utils.ChildFirstURLClassLoader;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

public class TestMethod implements Comparable<TestMethod> {

    private final Class<?> testClass;
    private final String name;
    private String mutatedDirectory;
    private static final char SEPARATOR = '#';
    private long execTime;

    /**
     * Test suite test method constructor.
     * @param testClass parent class of test method
     * @param name test method name
     */
    public TestMethod(Class<?> testClass, String name, String mutatedDirectory) {
        this.testClass = testClass;
        this.name = name;
        this.mutatedDirectory = mutatedDirectory;
    }

    /**
     * Reloads and returns test method parent class. If an exception is thrown, returns the original class.
     */
    public Class<?> reloadClass() {
        try {
            URL mainUrl = new File(mutatedDirectory).toURI().toURL();
            URL testUrl = new File(testClass.getProtectionDomain().getCodeSource().getLocation().getFile()).toURI().toURL();
            ChildFirstURLClassLoader classLoader = new ChildFirstURLClassLoader(new URL[] {mainUrl, testUrl},
                    Thread.currentThread().getContextClassLoader());
            return classLoader.loadClass(testClass.getName());
        } catch (Exception e) {
            System.out.println("Error reloading class: " + testClass.getName());
            return testClass;
        }
    }

    /**
     * Retrieves test method parent class.
     */
    public Class<?> getTestClass() {
        return this.testClass;
    }

    /**
     * Retrieves test method name.
     */
    public String getName() {
        return this.name;
    }

    public String getLongName() {
        return testClass.getName() + "[" + name + "]";
    }

    /**
     * Retrieves test method execution time
     */
    public long getExecTime() {
        return execTime;
    }

    /**
     * Method to set initial execution time of test method during prepass phase with all mutants disabled.
     * @param execTime initial test method execution time
     */
    public void setExecTime(long execTime) {
        this.execTime = execTime;
    }

    @Override
    public int hashCode() {
        return 37 * 19 * this.toString().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TestMethod) {
            TestMethod other = (TestMethod) obj;
            return this.toString().equals(other.toString());
        }
        return false;
    }

    @Override
    public int compareTo(TestMethod obj) {
        if (obj instanceof TestMethod) {
            TestMethod other = (TestMethod) obj;
            return this.toString().compareTo(other.toString());
        }
        return -1;
    }

    @Override
    public String toString() {
        return this.testClass.getName() + SEPARATOR + this.name;
    }
}
