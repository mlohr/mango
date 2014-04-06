package net.mloehr.mango;

public class Timer {

    public static final int MILLISECONDS_BETWEEN_ELEMENT_CHECK = 500;
    public static final int TIMEOUT_IN_SECONDS = 5;

    private long expiryTime;
    private int seconds;

    public Timer(int seconds) {
        this.seconds = seconds;
        reset();
    }

    public void reset() {
        expiryTime = currentTimePlus(seconds);
    }

    public static void waitFor(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isExpired() {
        return currentTime() >= expiryTime;
    }

    public boolean isNotExpired() {
        return !isExpired();
    }

    private long currentTimePlus(int seconds) {
        return currentTime() + milli(seconds);
    }

    private long milli(int seconds) {
        return seconds * 1000;
    }

    private long currentTime() {
        return System.currentTimeMillis();
    }

}