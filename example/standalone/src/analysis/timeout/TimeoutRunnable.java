package analysis.timeout;

import org.junit.runner.JUnitCore;
import org.junit.runner.Request;
import org.junit.runner.Result;
import prepass.TestMethod;

public class TimeoutRunnable implements Runnable {

    private TestMethod test;
    private Result result;
    private boolean interrupted;
    private boolean complete;

    TimeoutRunnable(TestMethod test) {
        this.test = test;
        result = null;
        interrupted = false;
    }

    @Override
    public void run() {
        while (!interrupted) {
            Request request = Request.method(test.getTestClass(), test.getName());
            result = new JUnitCore().run(request);
            complete = true;
            interrupted = true;
        }
    }

    public Result getResult() {
        return result;
    }

    public void interrupt() {
        interrupted = true;
    }

    public boolean isComplete() {
        return complete;
    }
}
