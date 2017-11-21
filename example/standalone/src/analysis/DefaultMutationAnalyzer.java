package analysis;

import analysis.runners.TestRunner;
import analysis.runners.WorkOrder;
import analysis.timeout.TimeoutRunner;
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
import utils.TimeoutAnalyzer;

import static utils.Outcome.*;

public class DefaultMutationAnalyzer implements MutationAnalyzer {

    private HashMap<TestMethod, ArrayList<Integer>> coverage;
    private String logFilepath;
    private int offset = 0;
    private int factor = 1;

    public DefaultMutationAnalyzer(HashMap<TestMethod, ArrayList<Integer>> coverage,
                                   String logFilepath, int offset, int factor) {
        this.coverage = coverage;
        this.logFilepath = logFilepath;
        this.offset = offset;
        this.factor = factor;
    }

    public DefaultKillMatrix runFullAnalysis() {
        int killedCount = 0;
        int mutantNumber = totalMutantNumber();
        DefaultKillMatrix matrix = new DefaultKillMatrix(mutantNumber);
        for (int i = 1; i <= mutantNumber; i++) {
            boolean killed = false;
            Config.__M_NO = i;
            for (TestMethod test : coverage.keySet()) {
                Outcome result = analyzeTestStop(test, i);
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
                    Outcome result = analyzeTestDefault(test, i);
                    matrix.addKillResult(test.getName(), result);
                    if (result != UNKILLED && result != NOT_COVERED) {
                        killedCount++;
                        killed = true;
                    }
                } else {
                    matrix.addKillResult(test.getName(), NOT_TESTED);
                }
            }
        }
        matrix.setMutationScore((float) killedCount / mutantNumber);
        return matrix;
    }

    // Default analyzeTest() method -- main thread does not exit if tests timeout
    private Outcome analyzeTestDefault(TestMethod test, int mutantId) {
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

    // Creates new thread when running tests and uses Thread.stop() to kill threads that timeout -- deprecated method
    private Outcome analyzeTestStop(TestMethod test, int mutantId) {
        if (coverage.get(test).contains(mutantId)) {
            Result result = TimeoutRunner.runTest(test, getTimeout(test));
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

    // Uses reflection and isolated thread to run tests -- problem accessing test classes via reflection
    private Outcome analyzeTestIsolated(TestMethod test, int mutantId) {
        if (coverage.get(test).contains(mutantId)) {
            analysis.runners.Outcome result = TestRunner.runTest(new WorkOrder(test, mutantId, getTimeout(test)));
            if (result.type == analysis.runners.Outcome.Type.TIMEOUT) {
                System.out.println(test.getName() + " timed out...");
                return TIMEOUT;
            } else if (result.type == analysis.runners.Outcome.Type.FAIL) {
                //System.out.println(test.getName() + " failed...");
                return ASSERTION_ERROR;
            } else if (result.type == analysis.runners.Outcome.Type.CRASH) {
                //System.out.println(test.getName() + " crashed...");
                return GENERAL_EXCEPTION;
            } else {
                //System.out.println(test.getName() + " passed...");
                return UNKILLED;
            }
        } else {
            return NOT_COVERED;
        }
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
        try {
            result = TimeoutAnalyzer.runWithTimeout(new TestTask(test.getTestClass(), test), getTimeout(test), TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            System.out.println(test.getName() + " timed out...");
        }
        return result;
    }

    private long getTimeout(TestMethod test) {
        long execTime = test.getExecTime();
        return offset * 1000 + execTime * factor;
    }
}
