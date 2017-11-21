package analysis.timeout;

import org.junit.runner.Result;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class TestBuilder {

    private FutureTask<Result> task;
    private volatile Thread blinker;

    TestBuilder(FutureTask<Result> task) {
        this.task = task;
    }

    public void start() {
        blinker = new Thread(task);
        blinker.start();
    }

    public void stop() {
        blinker = null;
    }

    public Result run(long timeout) throws TimeoutException, ExecutionException, InterruptedException {
        Thread thisThread = Thread.currentThread();
        while (blinker == thisThread) {
            return task.get(timeout, TimeUnit.MILLISECONDS);
        }
        return null;
    }
}
