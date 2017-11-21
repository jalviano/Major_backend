package analysis.runners;

import major.mutation.Config;

import org.junit.runner.JUnitCore;
import org.junit.runner.Request;
import org.junit.runner.Result;

import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.concurrent.*;

public class TestRunner {

    private static Outcome simpleBlockingRunTestInOtherClassLoader(ClassLoader classLoader, WorkOrder workOrder) throws
            ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Config.reset();
        Config.__M_NO = workOrder.mutantId;
        Result result = (new JUnitCore()).run(Request.method(workOrder.test.getTestClass(), workOrder.test.getName()));
        Outcome outcome = new Outcome(result, "");
        Config.reset();
        return outcome;
    }

    private static Outcome _simpleBlockingRunTestInOtherClassLoader(ClassLoader classLoader, WorkOrder workOrder) throws
            ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Class<?> theirWorkOrderClass = classLoader.loadClass(WorkOrder.class.getCanonicalName());
        Class<?> theirTestRunnerClass = classLoader.loadClass(TestRunner.class.getCanonicalName());
        Object theirWorkOrder = theirWorkOrderClass.getDeclaredMethod("fromString", String.class).invoke(
                null, workOrder.toString());
        Object theirOutcome = theirTestRunnerClass.getDeclaredMethod("simpleBlockingRunTest", theirWorkOrderClass)
                .invoke(null, theirWorkOrder);
        System.out.println(theirOutcome.toString());
        return Outcome.fromString(theirOutcome.toString());
    }

    public static Outcome isolatedBlockingRunTest(WorkOrder workOrder) throws ClassNotFoundException,
            NoSuchMethodException, IllegalAccessException, InvocationTargetException, MalformedURLException {
        ClassLoader theirClassLoader = new IsolatingClassLoader();
        Thread.currentThread().setContextClassLoader(theirClassLoader);
        Long t0 = System.currentTimeMillis();
        Outcome outcome = simpleBlockingRunTestInOtherClassLoader(theirClassLoader, workOrder);
        Long t1 = System.currentTimeMillis();
        outcome.runTime = t1 - t0;
        return outcome;
    }

    private static void killThreadGroup(ThreadGroup group) {
        Thread[] activeThreads = new Thread[group.activeCount()];
        for (int i = 0; i < group.enumerate(activeThreads); ++i) {
            activeThreads[i].interrupt();
        }
    }

    private static Outcome runTestWithoutCatchingStdStreams(final WorkOrder workOrder) {
        FutureTask<Outcome> futureOutcome = new FutureTask<>(new Callable<Outcome>() {
            public Outcome call() throws Exception {
                try {
                    return TestRunner.isolatedBlockingRunTest(workOrder);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        });
        //final TestRunnable runnable = new TestRunnable(workOrder);
        //FutureTask futureOutcome = new FutureTask(runnable, runnable.getOutcome());
        ThreadGroup group = new ThreadGroup("[test thread group for " + workOrder + "]");
        Thread thread = new Thread(group, futureOutcome, "[test thread for " + workOrder + "]");
        //thread.setDaemon(true);
        //group.setDaemon(true);
        thread.start();
        try {
            return futureOutcome.get(workOrder.timeout, TimeUnit.MILLISECONDS);
        } catch (TimeoutException|ExecutionException|InterruptedException e) {
            killThreadGroup(group);
            thread.stop();
            /*if (group.activeCount() == 0) {
                return Outcome.createTimeout(workOrder);
            }
            try {
                Thread.sleep(20);

            } catch (InterruptedException e2) {
                System.exit(1);
            }
            if (group.activeCount() == 0) {
                return Outcome.createTimeout(workOrder);
            }
            System.exit(1);
            return null;*/
            return Outcome.createTimeout(workOrder);
        }
    }

    public static Outcome runTest(WorkOrder workOrder) {
        return runTestWithoutCatchingStdStreams(workOrder);
        /*PrintStream originalStdout = System.out;
        PrintStream originalStderr = System.err;
        DeadEndDigestOutputStream fakeStdout = new DeadEndDigestOutputStream();
        DeadEndDigestOutputStream fakeStderr = new DeadEndDigestOutputStream();
        System.setOut(new PrintStream(fakeStdout));
        System.setErr(new PrintStream(fakeStderr));
        try {
            Outcome outcome = runTestWithoutCatchingStdStreams(workOrder);
            outcome.digest = fakeStdout.getDigestString() + fakeStderr.getDigestString();
            return outcome;
        } finally {
            System.setOut(originalStdout);
            System.setErr(originalStderr);
        }*/
    }
}
