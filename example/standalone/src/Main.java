import optimization.DefaultOptimizer;
import output.DefaultKillMatrix;
import analysis.DefaultMutationAnalyzer;
import output.CSVFormatter;
import prepass.DefaultPrepassAnalyzer;
import prepass.TestFinder;
import prepass.TestMethod;
import utils.PipelineTimer;

import java.io.IOException;
import java.util.*;

public class Main {

    // Benchmark Report:            Standalone      Fixed           Ant

    // Time to load classes:        17"             22"             ---
    // Time to run prepass:         4"              ---             37"
    // Time to run analysis:        21'25"          51'44"          54'06"
    // Time to format output:       2"              2"              --
    // Total time:                  21'48"          52'31"          54'44"
    // Mutant number:               24,607          ------          24,607
    // Mutants covered:             16,332          ----            16,332
    // Mutation score:              47.32%          49.33%          49.32% / 74.31%


    // Defects4j Benchmarks Report:                 Standalone      Ant

    // defects4j checkout -pLang -v1f -wLang-1:
    // Time to run analysis:                        62"             88"
    // Total time:                                  159"            156"
    // Mutation score:                              68.65%          68.65%

    /**
     * Builds backend pipeline to run mutation analysis.
     */
    public static void main(String... args) {
        PipelineTimer timer = new PipelineTimer();
        boolean outputFullKillMatrix = Boolean.parseBoolean(args[0]);
        boolean sortOptimization = Boolean.parseBoolean(args[1]);
        boolean testIsolation = Boolean.parseBoolean(args[2]);
        int offset = Integer.parseInt(args[3]);
        int factor = Integer.parseInt(args[4]);
        String logFilepath = args[5];
        String mutatedDirectory = args[6];
        String[] testDirectories = Arrays.copyOfRange(args, 7, args.length);
        List<Class<?>> testClasses;
        try {
            System.out.println("Loading classes...");
            testClasses = TestFinder.loadClasses(testDirectories);
            timer.logTime("Time to load classes");
            // 1. PREPASS
            System.out.println("Running prepass phase...");
            DefaultPrepassAnalyzer prepass = new DefaultPrepassAnalyzer(testClasses, mutatedDirectory);
            HashMap<TestMethod, ArrayList<Integer>> coverage = prepass.runPrepass();
            printCoverage(coverage);
            timer.logTime("Time to run prepass");
            // 2. OPTIMIZATION
            DefaultOptimizer optimizer = new DefaultOptimizer(coverage, sortOptimization);
            Map<TestMethod, ArrayList<Integer>> optimizedCoverage = optimizer.runOptimizations();
            // 3. MUTATION ANALYSIS
            System.out.println("Running mutation analysis...");
            DefaultMutationAnalyzer analyzer = new DefaultMutationAnalyzer(optimizedCoverage, testIsolation, offset,
                    factor, logFilepath);
            DefaultKillMatrix matrix;
            if (outputFullKillMatrix) {
                matrix = analyzer.runFullAnalysis();
            } else {
                matrix = analyzer.runSparseAnalysis();
            }
            timer.logTime("Time to run analysis");
            // 4. RESULTS OUTPUT
            System.out.println("Formatting output...");
            CSVFormatter csvFormatter = new CSVFormatter();
            csvFormatter.drawOutput(matrix);
            timer.logTime("Time to format output");
            timer.finalTime("Total time");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    private static void printCoverage(HashMap<TestMethod, ArrayList<Integer>> coverage) {
        System.out.println("Covered mutants: ");
        for (TestMethod key : coverage.keySet()) {
            if (key.getName().equals("testConstants")
                    || key.getName().equals("test_toIntegerObject_Boolean")
                    || key.getName().equals("test_toIntegerObject_boolean")) {
                System.out.println(key.getName() + ": " + coverage.get(key).toString());
            }
        }
    }
}
