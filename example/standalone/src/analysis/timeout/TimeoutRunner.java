package analysis.timeout;

import analysis.TestTask;
import org.junit.runner.JUnitCore;
import org.junit.runner.Request;
import org.junit.runner.Result;
import prepass.TestMethod;

import java.util.concurrent.*;

public class TimeoutRunner {

    public static Result runTest(final TestMethod test, long timeout, int mutantId) {
        FutureTask<Result> task = new FutureTask<>(new TestTask(test.getTestClass(), test, mutantId));
        /*FutureTask<Result> task = new FutureTask<>(new Callable<Result>() {
            public Result call() throws Exception {
                try {
                    Request request = Request.method(test.getTestClass(), test.getName());
                    return new JUnitCore().run(request);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        });*/
        Thread thread = new Thread(task, "[thread for " + test.getName() + ", " + mutantId + "]");
        thread.start();
        try {
            return task.get(timeout, TimeUnit.MILLISECONDS);
        } catch (TimeoutException | ExecutionException | InterruptedException e) {
            //thread.interrupt();
            thread.stop();
            return null;
        }
    }
}
