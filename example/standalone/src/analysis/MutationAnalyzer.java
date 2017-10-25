package analysis;

import major.mutation.Config;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.runner.JUnitCore;
import org.junit.runner.Request;
import org.junit.runner.Result;

import output.KillMatrix;
import prepass.TestMethod;
import static utils.Constants.*;

public class MutationAnalyzer {

    private HashMap<TestMethod, ArrayList<Integer>> coverage;
    private String logFilepath;
    private Class<?> testClass;

    public MutationAnalyzer(List<Class<?>> testClasses, HashMap<TestMethod, ArrayList<Integer>> coverage,
                            String logFilepath) {
        this.coverage = coverage;
        this.logFilepath = logFilepath;
        testClass = testClasses.get(1);
    }

    public float runAnalysis() {
        int killedCount = 0;
        int mutantNumber = totalMutantNumber();
        KillMatrix matrix = new KillMatrix();
        for (int i = 1; i <= mutantNumber; i++) {
            boolean killed = false;
            Config.__M_NO = i;
            for (TestMethod test : coverage.keySet()) {
                if (coverage.get(test).contains(i)) {
                    Request request = Request.method(testClass, test.getName());
                    Result result = new JUnitCore().run(request);
                    if (result.getFailureCount() != 0) {
                        // Add kill cell for killed mutant for test case.
                        matrix.addKillResult(test.getName(), KILLED);
                        if (!killed) {
                            killedCount++;
                            killed = true;
                        }
                    } else {
                        // Add kill cell for unkilled mutant for test case.
                        matrix.addKillResult(test.getName(), UNKILLED);
                    }
                } else {
                    // Add empty (N/A) kill cell for test case.
                    matrix.addKillResult(test.getName(), NOT_COVERED);
                }
            }
        }
        matrix.printOutput();
        return (float) killedCount / mutantNumber;
    }

    public float runFastAnalysis() {
        int killedCount = 0;
        int mutantNumber = totalMutantNumber();
        KillMatrix matrix = new KillMatrix();
        for (int i = 1; i <= mutantNumber; i++) {
            boolean killed = false;
            Config.__M_NO = i;
            for (TestMethod test : coverage.keySet()) {
                if (coverage.get(test).contains(i)) {
                    if (!killed) {
                        Request request = Request.method(testClass, test.getName());
                        Result result = new JUnitCore().run(request);
                        if (result.getFailureCount() != 0) {
                            // Add kill cell for killed mutant for test case.
                            matrix.addKillResult(test.getName(), KILLED);
                            killedCount++;
                            killed = true;
                        } else {
                            // Add kill cell for unkilled mutant for test case.
                            matrix.addKillResult(test.getName(), UNKILLED);
                        }
                    } else {
                        // Add kill cell for killed mutant for test case.
                        matrix.addKillResult(test.getName(), KILLED);
                    }
                } else {
                    // Add empty (N/A) kill cell for test case.
                    matrix.addKillResult(test.getName(), NOT_COVERED);
                }
            }
        }
        return (float) killedCount / mutantNumber;
    }

    private int totalMutantNumber() {
        int mutantNumber = 0;
        try {
            FileInputStream stream = new FileInputStream(logFilepath);
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
                String line;
                while (null != (line = reader.readLine())) {
                    mutantNumber++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mutantNumber;
    }
}
