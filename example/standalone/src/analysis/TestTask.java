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

    TestTask(Class<?> testClass, TestMethod test) {
        this.testClass = testClass;
        this.test = test;
    }

    @Override
    public Result call() throws Exception {
        JUnitCore jUnitCore = new JUnitCore();
        Request request = Request.method(testClass, test.getName());
        Result result = jUnitCore.run(request);
        return result;
        //Thread.sleep(10000);
        //return null;
    }
}
