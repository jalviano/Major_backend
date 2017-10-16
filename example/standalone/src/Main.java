import analysis.MutationAnalyzer;
import prepass.Prepass;
import prepass.TestFinder;
import prepass.TestMethod;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main {

    public static void main(String... args) {
        //String mutatedClassPath = args[0];
        //String mutatedClassName = args[1]
        //String testClassPath = args[2];
        //String testClassName = args[3]
        String mutatedClassPath = "triangle/Triangle.class";
        String mutatedClassName = "triangle.Triangle";
        String testClassPath = "triangle/test/TestSuite.class";
        String testClassName = "triangle.test.TestSuite";
        List<Class<?>> testClasses;
        try {
            testClasses = TestFinder.getAllClasses(mutatedClassPath, mutatedClassName, testClassPath, testClassName);
            Prepass prepass = new Prepass(testClasses);
            HashMap<Method, ArrayList<Integer>> coverage = prepass.runPrepass();
            System.out.println("Covered mutants: ");
            for (Method key : coverage.keySet()) {
                System.out.println(key.getName() + ": " + coverage.get(key).toString());
            }
            MutationAnalyzer analyzer = new MutationAnalyzer(testClasses, coverage);
            int mutationScore = analyzer.runAnalysis();
            System.out.println("Mutation score: " + mutationScore);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
