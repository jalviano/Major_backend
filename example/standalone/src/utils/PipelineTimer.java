package utils;

public class PipelineTimer {

    private long start;
    private long last;

    public PipelineTimer() {
        start = System.currentTimeMillis();
        last = start;
    }

    public void logTime(String message) {
        long now = System.currentTimeMillis();
        System.out.println(message + ": " + (now - last) / 1000 + "s");
        last = now;
    }

    public void finalTime(String message) {
        long end = System.currentTimeMillis();
        System.out.println(message + ": " + (end - start) / 1000 + "s");
    }
}
