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
        String testDirectory = args[7];
        List<Class<?>> testClasses;
        try {
            System.out.println("Loading classes...");
            testClasses = TestFinder.loadClasses(testDirectory);
            timer.logTime("Time to load classes");
            // 1. PREPASS
            System.out.println("Running prepass phase...");
            DefaultPrepassAnalyzer prepass = new DefaultPrepassAnalyzer(testClasses, mutatedDirectory);
            HashMap<TestMethod, ArrayList<Integer>> coverage = prepass.runPrepass();
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
}
