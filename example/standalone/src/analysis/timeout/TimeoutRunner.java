package analysis.timeout;

import org.junit.runner.JUnitCore;
import org.junit.runner.Request;
import org.junit.runner.Result;
import prepass.TestMethod;

import java.util.concurrent.*;

public class TimeoutRunner {

    public static Result runTest(final TestMethod test, long timeout) {
        FutureTask<Result> task = new FutureTask<>(new Callable<Result>() {
            public Result call() throws Exception {
                try {
                    Request request = Request.method(test.getTestClass(), test.getName());
                    return new JUnitCore().run(request);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        });
        Thread thread = new Thread(task, "[test thread for " + test.getName() + "]");
        thread.start();
        try {
            return task.get(timeout, TimeUnit.MILLISECONDS);
        } catch (TimeoutException | ExecutionException | InterruptedException e) {
            System.out.println(test.getName() + " timed out...");
            //thread.interrupt();
            thread.stop();
            return null;
        }
    }
}
