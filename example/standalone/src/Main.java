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

    // 1. Add ability for user to include formatter class file
    // 2. Improve backend build and run method (currently shell script, would be better with build.xml)

    public static void main(String... args) {
        Boolean outputFullKillMatrix = Boolean.parseBoolean(args[0]);
        int offset = Integer.parseInt(args[1]);
        int factor = Integer.parseInt(args[2]);
        String logFilepath = args[3];
        String[] testDirectories = Arrays.copyOfRange(args, 4, args.length);
        List<Class<?>> testClasses;
        try {
            testClasses = TestFinder.loadClasses(testDirectories);
            // 1. PREPASS
            DefaultPrepassAnalyzer prepass = new DefaultPrepassAnalyzer(testClasses);
            HashMap<TestMethod, ArrayList<Integer>> coverage = prepass.runPrepass();
            // printCoverage(coverage);
            // 2. MUTATION ANALYSIS
            DefaultMutationAnalyzer analyzer = new DefaultMutationAnalyzer(coverage, logFilepath, offset, factor);
            DefaultKillMatrix matrix;
            if (outputFullKillMatrix) {
                matrix = analyzer.runCompleteAnalysis();
            } else {
                matrix = analyzer.runSparseAnalysis();
            }
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
