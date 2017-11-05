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

    // There is an inconsistency in test execution time measurement. Should timeout be measured against original
    // execution time or a constant (for example, 1 second)?
    // Should formatter interface be implemented by main method, analyzer object or kill matrix object? Do we need a
    // specific formatter object, or can the kill matrix just implement the interface?
    // Do "not covered" and "unkilled" results need to be separate? For the sake of matrix sparsity, making them both
    // null would be better.

    // 2. Configure whether sparse vs fully populated matrix is built (could depend on formatter or configuration)
    // 4. Add ability for user to include formatter class file
    // 5. Add configuration for test runtime constant limit vs factor of execution time (configure offset, factor)
    // 7. For benchmarking, report analysis time and kill matrix CSV

    public static void main(String... args) {
        String logFilepath = args[0];
        String mutatedClasspath = args[1];
        String testClasspath = args[2];
        //String[] testClasspaths = Arrays.copyOfRange(args, 2, args.length);
        List<Class<?>> testClasses;
        try {
            String[] classpaths = {testClasspath};
            testClasses = TestFinder.loadClasses(classpaths);
            // 1. PREPASS
            DefaultPrepassAnalyzer prepass = new DefaultPrepassAnalyzer(testClasses);
            HashMap<TestMethod, ArrayList<Integer>> coverage = prepass.runPrepass();
            // 2. MUTATION ANALYSIS
            DefaultMutationAnalyzer analyzer = new DefaultMutationAnalyzer(testClasses, coverage, logFilepath);
            DefaultKillMatrix matrix = analyzer.runCompleteAnalysis();
            // 3. RESULTS OUTPUT
            CSVMatrix csvFormatter = new CSVMatrix();
            csvFormatter.drawOutput(matrix);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
