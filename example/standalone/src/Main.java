import analysis.MutationAnalyzer;
import prepass.Prepass;
import prepass.TestFinder;
import prepass.TestMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.*;

public class Main {

    // TODO: Implement execution time test to kill mutant
    // TODO: Implement output interface and visualization

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
            float mutationScore = analyzer.runAnalysis();
            System.out.println("Mutation score: " + mutationScore);
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
