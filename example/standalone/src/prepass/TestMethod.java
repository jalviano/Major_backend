package prepass;

public class TestMethod implements Comparable<TestMethod> {

    private final Class<?> testClass;
    private final String name;
    private static final char SEPARATOR = '#';
    private long execTime;

    /**
     * Test suite test method constructor.
     * @param testClass parent class of test method
     * @param name test method name
     */
    TestMethod(Class<?> testClass, String name) {
        this.testClass = testClass;
        this.name = name;
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
    void setExecTime(long execTime) {
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
