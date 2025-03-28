package com.example.stopwatch;
import android.os.SystemClock;

public class StopWatch {
    private long startTime = 0L;
    private long timeSwapBuff = 0L;
    private long updateTime = 0L;
    private boolean running = false;

    public void start() {
        startTime = SystemClock.uptimeMillis();
        running = true;
    }

    public void stop() {
        timeSwapBuff += SystemClock.uptimeMillis() - startTime;
        running = false;
    }

    public void reset() {
        startTime = 0L;
        timeSwapBuff = 0L;
        updateTime = 0L;
        running = false;
    }

    public long getUpdateTime() {
        if (running) {
            updateTime = timeSwapBuff + (SystemClock.uptimeMillis() - startTime);
        } else {
            updateTime = timeSwapBuff;
        }
        return updateTime;
    }

    public boolean isRunning() {
        return running;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getTimeSwapBuff() {
        return timeSwapBuff;
    }

    public void setState(long startTime, long timeSwapBuff, boolean isRunning) {
        this.startTime = startTime;
        this.timeSwapBuff = timeSwapBuff;
        this.running = isRunning;
    }
}

