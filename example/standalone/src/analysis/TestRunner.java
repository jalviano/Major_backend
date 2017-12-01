package analysis;

import org.junit.runner.Result;
import prepass.TestMethod;

import java.util.concurrent.*;

class TestRunner {

    /**
     * Runs JUnit test with mutant enabled in new thread. Terminates test and stops thread once timeout is reached.
     * @param test JUnit test method to be run
     * @param timeout length of time to run test before killing thread
     * @param mutantId mutant enabled during test
     * @return JUnit result for test method (null if test times out)
     */
    static Result runTest(final TestMethod test, long timeout, int mutantId) {
        FutureTask<Result> task = new FutureTask<>(new TestTask(test));
        ThreadGroup group = new ThreadGroup("[thread group for " + test.getName() + ", " + mutantId + "]");
        Thread thread = new Thread(group, task, "[thread for " + test.getName() + ", " + mutantId + "]");
        thread.start();
        try {
            return task.get(timeout, TimeUnit.MILLISECONDS);
        } catch (TimeoutException | ExecutionException | InterruptedException e) {
            killThreadGroup(group);
            thread.stop();
            return null;
        }
    }

    /**
     * Iterates over all threads in provided group and interrupts each thread.
     */
    private static void killThreadGroup(ThreadGroup group) {
        Thread[] activeThreads = new Thread[group.activeCount()];
        for (int i = 0; i < group.enumerate(activeThreads); ++i) {
            activeThreads[i].interrupt();
        }
    }
}
