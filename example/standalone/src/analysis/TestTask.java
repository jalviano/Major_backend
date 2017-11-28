package analysis;

import org.junit.runner.JUnitCore;
import org.junit.runner.Request;
import org.junit.runner.Result;
import org.junit.runner.notification.RunListener;
import prepass.TestMethod;

import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeoutException;

public class TestTask implements Callable<Result>{

    private Class<?> testClass;
    private TestMethod test;
    private int mutant;

    TestTask(Class<?> testClass, TestMethod test) {
        this.testClass = testClass;
        this.test = test;
    }

    public TestTask(Class<?> testClass, TestMethod test, int mutant) {
        this.testClass = testClass;
        this.test = test;
        this.mutant = mutant;
    }

    @Override
    public Result call() throws TimeoutException, InterruptedException {
        //System.out.println(test.getName() + "," + mutant);
        long start = System.currentTimeMillis();
        JUnitCore jUnitCore = new JUnitCore();
        Request request = Request.method(testClass, test.getName());
        Result result = jUnitCore.run(request);
        /*if (Thread.interrupted()) {
            System.out.println(
                    test.getName() +
                    " / " +
                    mutant +
                    " -- TIME: " +
                    Long.toString(System.currentTimeMillis() - start) +
                    ", TIMEOUT: " +
                    Long.toString(getTimeout(test))
            );
        }*/
        return result;
    }

    private long getTimeout(TestMethod test) {
        long execTime = test.getExecTime();
        return 1000 + execTime * 4;
    }
}
