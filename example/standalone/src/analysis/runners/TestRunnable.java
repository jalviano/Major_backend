package analysis.runners;

public class TestRunnable implements Runnable {

    private WorkOrder workOrder;
    private Outcome outcome;

    TestRunnable(WorkOrder workOrder) {
        this.workOrder = workOrder;
        outcome = null;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                outcome = TestRunner.isolatedBlockingRunTest(workOrder);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Outcome getOutcome() {
        return outcome;
    }
}
