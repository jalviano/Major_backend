import analysis.DefaultMutationAnalyzer;
import optimization.DefaultOptimizer;
import org.junit.Test;
import output.CSVFormatter;
import output.DefaultKillMatrix;
import prepass.DefaultPrepassAnalyzer;
import prepass.TestFinder;
import prepass.TestMethod;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainTest {

    @Test
    public void mainTest() {
        boolean outputFullKillMatrix = true;
        boolean sortOptimization = true;
        boolean testIsolation = true;
        int offset = 1000;
        int factor = 16;
        String logFilepath = "mutants.log";
        String mutatedDirectory = "bin/main";
        String testDirectory = "bin/test/triangle/test/";
        List<Class<?>> testClasses;
        try {
            testClasses = TestFinder.loadClasses(testDirectory);
            DefaultPrepassAnalyzer prepass = new DefaultPrepassAnalyzer(testClasses, mutatedDirectory);
            HashMap<TestMethod, ArrayList<Integer>> coverage = prepass.runPrepass();
            DefaultOptimizer optimizer = new DefaultOptimizer(coverage, sortOptimization);
            Map<TestMethod, ArrayList<Integer>> optimizedCoverage = optimizer.runOptimizations();
            DefaultMutationAnalyzer analyzer = new DefaultMutationAnalyzer(optimizedCoverage, testIsolation, offset,
                    factor, logFilepath);
            DefaultKillMatrix matrix;
            if (outputFullKillMatrix) {
                matrix = analyzer.runFullAnalysis();
            } else {
                matrix = analyzer.runSparseAnalysis();
            }
            CSVFormatter csvFormatter = new CSVFormatter();
            csvFormatter.drawOutput(matrix);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}