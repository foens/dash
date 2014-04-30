package dk.au.cs.dash.util;

public class Timer {
    private long lastTime;

    public Timer() {
        this.lastTime = System.currentTimeMillis();
    }

    public long tick() {
        long now = System.currentTimeMillis();
        long dt = now - lastTime;
        lastTime = now;
        return dt;
    }
}
