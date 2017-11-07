import output.DefaultKillMatrix;
import analysis.DefaultMutationAnalyzer;
import output.CSVMatrix;
import prepass.DefaultPrepassAnalyzer;
import prepass.TestFinder;
import prepass.TestMethod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Main {

    // In standalone implementation, the kill matrix provides data for each test method, not test class. The existing
    // implementation only shows data for test classes though. Which is needed?

    // 1. Configure whether sparse vs fully populated matrix is built (could depend on formatter or configuration)
    // 2. Add ability for user to include formatter class file
    // 3. Add configuration for test runtime constant limit vs factor of execution time (configure offset, factor)
    // 4. Improve main method arguments and filepath needs

    public static void main(String... args) {
        String logFilepath = args[0];
        String mutatedClasspath = args[1];
        String[] testClasspaths = Arrays.copyOfRange(args, 2, args.length);
        List<Class<?>> testClasses;
        try {
            testClasses = TestFinder.loadClasses(testClasspaths);
            // 1. PREPASS
            DefaultPrepassAnalyzer prepass = new DefaultPrepassAnalyzer(testClasses);
            HashMap<TestMethod, ArrayList<Integer>> coverage = prepass.runPrepass();
            // printCoverage(coverage);
            // 2. MUTATION ANALYSIS
            DefaultMutationAnalyzer analyzer = new DefaultMutationAnalyzer(coverage, logFilepath);
            DefaultKillMatrix matrix = analyzer.runCompleteAnalysis();
            // 3. RESULTS OUTPUT
            CSVMatrix csvFormatter = new CSVMatrix();
            csvFormatter.drawOutput(matrix);
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
