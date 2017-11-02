import analysis.KillMatrix;
import analysis.MutationAnalyzer;
import output.CSVMatrix;
import output.Formatter;
import prepass.Prepass;
import prepass.TestFinder;
import prepass.TestMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main {

    // There is an inconsistency in test execution time measurement. Should timeout be measured against original
    // execution time or a constant (for example, 1 second)?
    // Should formatter interface be implemented by main method, analyzer object or kill matrix object? Do we need a
    // specific formatter object, or can the kill matrix just implement the interface?
    // Do "not covered" and "unkilled" results need to be separate? For the sake of matrix sparsity, making them both
    // null would be better.

    // Check for error via JUnit (is it an assert in the program or general runtime error?)
    // Configure whether sparse vs fully populated matrix is built (could depend on formatter or configuration)
    // Plugin for formatter configuration

    public static void main(String... args) {
        String mutatedClasspath = args[0];
        String testClasspath = args[1];
        String logFilepath = args[2];
        List<Class<?>> testClasses;
        try {
            String[] classpaths = {mutatedClasspath, testClasspath};
            testClasses = TestFinder.loadClasses(classpaths);
            Prepass prepass = new Prepass(testClasses);
            HashMap<TestMethod, ArrayList<Integer>> coverage = prepass.runPrepass();
            //printCoverage(coverage);
            MutationAnalyzer analyzer = new MutationAnalyzer(testClasses, coverage, logFilepath);
            KillMatrix matrix = analyzer.runAnalysis();
            CSVMatrix csvFormatter = new CSVMatrix();
            csvFormatter.setFormatter(new Formatter(matrix));
            csvFormatter.drawOutput();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void printCoverage(HashMap<TestMethod, ArrayList<Integer>> coverage) {
        System.out.println("Covered mutants: ");
        for (TestMethod key : coverage.keySet()) {
            System.out.println(key.getName() + ": " + coverage.get(key).toString());
        }
    }
}
