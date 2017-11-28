package analysis;

import org.junit.runner.Result;
import prepass.TestMethod;

import java.util.concurrent.*;

class TestRunner {

    static Result runTest(final TestMethod test, long timeout, int mutantId) {
        FutureTask<Result> task = new FutureTask<>(new TestTask(test));
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
