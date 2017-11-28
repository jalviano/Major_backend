package utils;

public class PipelineTimer {

    private long start;
    private long last;

    /**
     * Timer to measure time to execute phases of mutation analysis pipeline.
     */
    public PipelineTimer() {
        start = System.currentTimeMillis();
        last = start;
    }

    /**
     * Logs time between last log and current time.
     * @param message label for log output
     */
    public void logTime(String message) {
        long now = System.currentTimeMillis();
        System.out.println(message + ": " + (now - last) / 1000 + "s");
        last = now;
    }

    /**
     * Logs total time measured by timer.
     * @param message label for log output
     */
    public void finalTime(String message) {
        long end = System.currentTimeMillis();
        System.out.println(message + ": " + (end - start) / 1000 + "s");
    }
}
