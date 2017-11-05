import analysis.DefaultKillMatrix;
import analysis.MutationAnalyzer;
import output.CSVMatrix;
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

    // 2. Configure whether sparse vs fully populated matrix is built (could depend on formatter or configuration)
    // 3. Add pipeline step interfaces
    // 4. Add ability for user to include formatter class file
    // 5. Add configuration for test runtime constant limit vs factor of execution time (configure offset, factor)
    // 7. For benchmarking, report analysis time and kill matrix CSV

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
            MutationAnalyzer analyzer = new MutationAnalyzer(testClasses, coverage, logFilepath);
            DefaultKillMatrix matrix = analyzer.runAnalysis();
            CSVMatrix csvFormatter = new CSVMatrix();
            csvFormatter.drawOutput(matrix);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
