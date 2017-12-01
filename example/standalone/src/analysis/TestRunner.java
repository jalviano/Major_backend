package analysis;

import org.junit.runner.Result;
import prepass.TestMethod;

import java.util.HashMap;
import java.util.concurrent.*;

class TestRunner {

    /**
     * Runs JUnit test with mutant enabled in new thread. Terminates test and stops thread once timeout is reached.
     * @param test JUnit test method to be run
     * @param timeout length of time to run test before killing thread
     * @param mutantId mutant enabled during test
     * @return JUnit result for test method (null if test times out)
     */
    static Result runTest(HashMap<String, String> classes, TestMethod test, long timeout, int mutantId) {
        FutureTask<Result> task = new FutureTask<>(new TestTask(classes.get(test.getTestClass().getName()), test));
        Thread thread = new Thread(task, "[thread for " + test.getName() + ", " + mutantId + "]");
        thread.start();
        try {
            return task.get(timeout, TimeUnit.MILLISECONDS);
        } catch (TimeoutException | ExecutionException | InterruptedException e) {
            thread.stop();
            return null;
        }
    }
}
