package analysis;

import org.junit.runner.JUnitCore;
import org.junit.runner.Request;
import org.junit.runner.Result;
import prepass.TestMethod;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeoutException;

public class TestTask implements Callable<Result>{

    private TestMethod test;

    /**
     * Constructor for task to run JUnit test.
     */
    TestTask(TestMethod test) {
        this.test = test;
    }

    /**
     * Callable method to run JUnit test and return result.
     */
    @Override
    public Result call() throws TimeoutException, InterruptedException {
        Request request = Request.method(test.getTestClass(), test.getName());
        return new JUnitCore().run(request);
    }
}
