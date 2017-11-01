package analysis;

import major.mutation.Config;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.*;

import org.junit.runner.Result;

import org.junit.runner.notification.Failure;
import output.FormatterInterface;
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

    public KillMatrix runAnalysis() {
        int killedCount = 0;
        int mutantNumber = totalMutantNumber();
        KillMatrix matrix = new KillMatrix(mutantNumber);
        for (int i = 1; i <= mutantNumber; i++) {
            boolean killed = false;
            Config.__M_NO = i;
            for (TestMethod test : coverage.keySet()) {
                Integer result = analyzeTest(test, i);
                matrix.addKillResult(test.getName(), result);
                if (result != UNKILLED && result != NOT_COVERED && !killed) {
                    killedCount++;
                    killed = true;
                }
            }
        }
        matrix.setMutationScore((float) killedCount / mutantNumber);
        return matrix;
    }

    public KillMatrix runOptimizedAnalysis() {
        int killedCount = 0;
        int mutantNumber = totalMutantNumber();
        KillMatrix matrix = new KillMatrix(mutantNumber);
        for (int i = 1; i <= mutantNumber; i++) {
            boolean killed = false;
            Config.__M_NO = i;
            for (TestMethod test : coverage.keySet()) {
                if (!killed) {
                    Integer result = analyzeTest(test, i);
                    matrix.addKillResult(test.getName(), result);
                    killedCount++;
                    killed = true;
                } else {
                    matrix.addKillResult(test.getName(), NOT_COVERED);
                }
            }
        }
        matrix.setMutationScore((float) killedCount / mutantNumber);
        return matrix;
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

    private Integer analyzeTest(TestMethod test, int mutantId) {
        if (coverage.get(test).contains(mutantId)) {
            Result result = timeAnalyzer(test);
            if (result == null) {
                return TIMEOUT;
            } else if (result.getFailureCount() != 0) {
                if (isAssertionError(result)) {
                    return ASSERTION_ERROR;
                } else {
                    return GENERAL_EXCEPTION;
                }
            } else {
                return UNKILLED;
            }
        } else {
            return NOT_COVERED;
        }
    }

    private boolean isAssertionError(Result result) {
        List<Failure> failures = result.getFailures();
        for (Failure failure : failures) {
            if (failure.getException() instanceof AssertionError) {
                return true;
            }
        }
        return false;
    }

    private Result timeAnalyzer(TestMethod test) {
        Result result = null;
        ExecutorService executor = Executors.newSingleThreadExecutor();
        TestTask task = new TestTask(testClass, test);
        Future<String> future = executor.submit(task);
        try {
            future.get(test.getExecTime(), TimeUnit.MILLISECONDS);
            result = task.getResult();
        } catch (Exception e) {
            future.cancel(true);
        }
        executor.shutdownNow();
        return result;
    }
}
