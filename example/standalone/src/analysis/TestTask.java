package analysis;

import org.junit.runner.JUnitCore;
import org.junit.runner.Request;
import org.junit.runner.Result;
import prepass.TestMethod;

import java.util.concurrent.Callable;

public class TestTask implements Callable<String>{

    private Class<?> testClass;
    private TestMethod test;
    private Result result;

    TestTask(Class<?> testClass, TestMethod test) {
        this.testClass = testClass;
        this.test = test;
    }

    Result getResult() {
        return result;
    }

    @Override
    public String call() throws Exception {
        Request request = Request.method(testClass, test.getName());
        result = new JUnitCore().run(request);
        return "Ready...";
    }
}
