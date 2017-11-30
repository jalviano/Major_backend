import output.DefaultKillMatrix;
import analysis.DefaultMutationAnalyzer;
import output.CSVFormatter;
import prepass.DefaultPrepassAnalyzer;
import prepass.TestFinder;
import prepass.TestMethod;
import utils.PipelineTimer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Main {

    // Optimization to order tests. Should not run if tests are not independent.
    // Are tests loaded never reloading underlying class?

    // Benchmark Report:            Standalone      Optimized       Ant

    // Time to load classes:        17"             ---             ---
    // Time to run prepass:         4"              ---             37"
    // Time to run analysis:        21'25"          ----            54'06"
    // Time to format output:       2"              --              --
    // Total time:                  21'48"          -----           54'44"
    // Mutant number:               24,607          ------          24,607
    // Mutants covered:             16,332          ----            16,332
    // Mutation score:              47.91%          ------          49.29% / 74.26%

    // Problematic unit test: ThreadUtilsTest.testComplexThreadGroups()
    // [thread for testThreadsNullPredicate, 20164]
    // [thread for testThreads, 20227]
    // [thread for testAtLeastOneThreadGroupsExists, 20255]
    // [thread for testAtLeastOneThreadGroupsExists, 20262]

    /**
     * Builds backend pipeline to run mutation analysis.
     */
    public static void main(String... args) {
        PipelineTimer timer = new PipelineTimer();
        Boolean outputFullKillMatrix = Boolean.parseBoolean(args[0]);
        int offset = Integer.parseInt(args[1]);
        int factor = Integer.parseInt(args[2]);
        String logFilepath = args[3];
        String[] testDirectories = Arrays.copyOfRange(args, 4, args.length);
        List<Class<?>> testClasses;
        try {
            System.out.println("Loading classes...");
            testClasses = TestFinder.loadClasses(testDirectories);
            timer.logTime("Time to load classes");
            // 1. PREPASS
            System.out.println("Running prepass phase...");
            DefaultPrepassAnalyzer prepass = new DefaultPrepassAnalyzer(testClasses);
            HashMap<TestMethod, ArrayList<Integer>> coverage = prepass.runPrepass();
            printCoverage(coverage);
            timer.logTime("Time to run prepass");
            // 2. MUTATION ANALYSIS
            System.out.println("Running mutation analysis...");
            DefaultMutationAnalyzer analyzer = new DefaultMutationAnalyzer(coverage, logFilepath, offset, factor);
            DefaultKillMatrix matrix;
            if (outputFullKillMatrix) {
                matrix = analyzer.runFullAnalysis();
            } else {
                matrix = analyzer.runSparseAnalysis();
            }
            timer.logTime("Time to run analysis");
            // 3. RESULTS OUTPUT
            System.out.println("Formatting output...");
            CSVFormatter csvFormatter = new CSVFormatter();
            csvFormatter.drawOutput(matrix);
            timer.logTime("Time to format output");
            timer.finalTime("Total time");
        } catch (IOException e) {
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
