package analysis.timeout;

import major.mutation.Config;

import org.junit.runner.JUnitCore;
import org.junit.runner.Request;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import output.DefaultKillMatrix;
import prepass.TestMethod;
import utils.Outcome;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.*;

import static utils.Outcome.*;

public class TimeoutThread {

    private HashMap<TestMethod, ArrayList<Integer>> coverage;

    public TimeoutThread(HashMap<TestMethod, ArrayList<Integer>> coverage) {
        this.coverage = coverage;
    }

    public DefaultKillMatrix runAnalysis() {
        final ExecutorService pool = Executors.newCachedThreadPool();
        final ExecutorCompletionService<WorkOrder> completionService = new ExecutorCompletionService<>(pool);
        int killedCount = 0;
        int mutantNumber = 160;
        DefaultKillMatrix matrix = new DefaultKillMatrix(mutantNumber);
        for (int i = 1; i <= mutantNumber; i++) {
            final int mutantId = i;
            Config.__M_NO = i;
            for (final TestMethod test : coverage.keySet()) {
                if (coverage.get(test).contains(i)) {
                    completionService.submit(new Callable<WorkOrder>() {
                        @Override
                        public WorkOrder call() throws Exception {
                            Request request = Request.method(test.getTestClass(), test.getName());
                            Result result = new JUnitCore().run(request);
                            return new WorkOrder(mutantId, test, result);
                        }
                    });
                }
            }
        }
        for (int i = 1; i <= mutantNumber; i++) {
            boolean killed = false;
            for (final TestMethod test : coverage.keySet()) {
                try {
                    final Future<WorkOrder> future = completionService.take();
                    final WorkOrder workOrder = future.get();
                    Result result = workOrder.getResult();
                    Outcome outcome;
                    if (coverage.get(workOrder.getTest()).contains(workOrder.getMutantId())) {
                        if (result == null) {
                            outcome = TIMEOUT;
                        } else if (result.getFailureCount() != 0) {
                            if (isAssertionError(result)) {
                                outcome = ASSERTION_ERROR;
                            } else {
                                outcome = GENERAL_EXCEPTION;
                            }
                        } else {
                            outcome = UNKILLED;
                        }
                    } else {
                        outcome = NOT_COVERED;
                    }
                    matrix.addKillResult(workOrder.getTest().getName(), outcome);
                    if (outcome != UNKILLED && outcome != NOT_COVERED && !killed) {
                        killedCount++;
                        killed = true;
                    }
                } catch (ExecutionException ee) {
                    System.out.println("Execution exception...");
                    ee.printStackTrace();
                } catch (InterruptedException ie) {
                    System.out.println("Interrupted exception...");
                    ie.printStackTrace();
                }
            }
        }
        matrix.setMutationScore((float) killedCount / mutantNumber);
        return matrix;
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

    private class WorkOrder {

        int mutantId;
        TestMethod test;
        Result result;

        WorkOrder(int mutantId, TestMethod test, Result result) {
            this.mutantId = mutantId;
            this.test = test;
            this.result = result;
        }

        public int getMutantId() {
            return mutantId;
        }

        public TestMethod getTest() {
            return test;
        }

        public Result getResult() {
            return result;
        }
    }
}
