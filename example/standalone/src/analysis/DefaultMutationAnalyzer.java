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
import output.DefaultKillMatrix;
import prepass.TestMethod;
import utils.Outcome;

import static utils.Outcome.*;

public class DefaultMutationAnalyzer implements MutationAnalyzer {

    private HashMap<TestMethod, ArrayList<Integer>> coverage;
    private String logFilepath;

    public DefaultMutationAnalyzer(HashMap<TestMethod, ArrayList<Integer>> coverage,
                                   String logFilepath) {
        this.coverage = coverage;
        this.logFilepath = logFilepath;
    }

    public DefaultKillMatrix runCompleteAnalysis() {
        int killedCount = 0;
        int mutantNumber = totalMutantNumber();
        DefaultKillMatrix matrix = new DefaultKillMatrix(mutantNumber);
        for (int i = 1; i <= mutantNumber; i++) {
            boolean killed = false;
            Config.__M_NO = i;
            for (TestMethod test : coverage.keySet()) {
                Outcome result = analyzeTest(test, i);
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

    public DefaultKillMatrix runSparseAnalysis() {
        int killedCount = 0;
        int mutantNumber = totalMutantNumber();
        DefaultKillMatrix matrix = new DefaultKillMatrix(mutantNumber);
        for (int i = 1; i <= mutantNumber; i++) {
            boolean killed = false;
            Config.__M_NO = i;
            for (TestMethod test : coverage.keySet()) {
                if (!killed) {
                    Outcome result = analyzeTest(test, i);
                    matrix.addKillResult(test.getName(), result);
                    killedCount++;
                    killed = true;
                } else {
                    matrix.addKillResult(test.getName(), NOT_TESTED);
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

    private Outcome analyzeTest(TestMethod test, int mutantId) {
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
        TestTask task = new TestTask(test.getTestClass(), test);
        Future<String> future = executor.submit(task);
        try {
            future.get(getTimeout(test), TimeUnit.MILLISECONDS);
            result = task.getResult();
        } catch (Exception e) {
            future.cancel(true);
        }
        executor.shutdownNow();
        return result;
    }

    private long getTimeout(TestMethod test) {
        long offset = 1000;
        int factor = 10;
        long execTime = test.getExecTime();
        return offset + execTime + (execTime / factor);
    }
}