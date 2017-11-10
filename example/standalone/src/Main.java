import output.DefaultKillMatrix;
import analysis.DefaultMutationAnalyzer;
import output.CSVFormatter;
import prepass.DefaultPrepassAnalyzer;
import prepass.TestFinder;
import prepass.TestMethod;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Main {

    // Why are some threads not being killed?
    // Are tests loaded never reloading underlying class?
    // Check number of timeouts

    // 1. Add ability for user to include formatter class file
    // 2. Fix build.xml file so it can run analysis

    // Benchmark Report:            Standalone      Optimized       Ant

    // Time to load classes:        83"             ---             ---
    // Time to run prepass:         77"             ---             ---
    // Time to run analysis:        ----            ----            ----
    // Time to format output:       --              --              --
    // Total time:                  -----           -----           -----
    // Test number:                 ---             ---             ---
    // Mutant number:               32,681          ------          32,681
    // Mutants covered:             ----            ----            ----
    // Mutation score:              ------          ------          ------

    public static void main(String... args) {
        long start = System.currentTimeMillis();
        Boolean outputFullKillMatrix = Boolean.parseBoolean(args[0]);
        int offset = Integer.parseInt(args[1]);
        int factor = Integer.parseInt(args[2]);
        String logFilepath = args[3];
        String[] testDirectories = Arrays.copyOfRange(args, 4, args.length);
        List<Class<?>> testClasses;
        try {
            System.out.println("Loading classes...");
            testClasses = TestFinder.loadClasses(testDirectories);
            long loadTime = System.currentTimeMillis();
            System.out.println("Time to load classes: " + (loadTime - start) / 1000 + "s");
            // 1. PREPASS
            System.out.println("Running prepass phase...");
            DefaultPrepassAnalyzer prepass = new DefaultPrepassAnalyzer(testClasses);
            HashMap<TestMethod, ArrayList<Integer>> coverage = prepass.runPrepass();
            long prepassTime = System.currentTimeMillis();
            System.out.println("Time to run prepass: " + (prepassTime - loadTime) / 1000 + "s");
            // 2. MUTATION ANALYSIS
            System.out.println("Running mutation analysis...");
            DefaultMutationAnalyzer analyzer = new DefaultMutationAnalyzer(coverage, logFilepath, offset, factor);
            DefaultKillMatrix matrix;
            if (outputFullKillMatrix) {
                matrix = analyzer.runCompleteAnalysis();
            } else {
                matrix = analyzer.runSparseAnalysis();
            }
            long analysisTime = System.currentTimeMillis();
            System.out.println("Time to run analysis: " + (analysisTime - prepassTime) / 1000 + "s");
            // 3. RESULTS OUTPUT
            System.out.println("Formatting output...");
            CSVFormatter csvFormatter = new CSVFormatter();
            csvFormatter.drawOutput(matrix);
            long end = System.currentTimeMillis();
            System.out.println("Time to format output: " + (end - analysisTime) / 1000 + "s");
            System.out.println("Total time: " + (end - start) / 1000 + "s");
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
