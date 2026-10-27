package com.mcq.util;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

/**
 * QuizTimer.java
 * Reusable countdown timer controller built on top of javax.swing.Timer.
 * Executes on the Swing Event Dispatch Thread (EDT) for thread-safe UI updates.
 */
public class QuizTimer {

    /**
     * Functional interface for receiving per-second countdown updates.
     */
    public interface TickCallback {
        void onTick(int secondsRemaining);
    }

    /**
     * Functional interface for receiving timeout notification when countdown reaches 0.
     */
    public interface TimeoutCallback {
        void onTimeout();
    }

    private final int totalSeconds;
    private int secondsRemaining;
    private final Timer swingTimer;
    private final TickCallback tickCallback;
    private final TimeoutCallback timeoutCallback;

    /**
     * Constructs a QuizTimer with the specified countdown duration and callbacks.
     * 
     * @param durationSeconds Total seconds to count down (e.g., 30)
     * @param tickCallback Callback executed every second with current time remaining
     * @param timeoutCallback Callback executed when timer reaches zero
     */
    public QuizTimer(int durationSeconds, TickCallback tickCallback, TimeoutCallback timeoutCallback) {
        this.totalSeconds = durationSeconds;
        this.secondsRemaining = durationSeconds;
        this.tickCallback = tickCallback;
        this.timeoutCallback = timeoutCallback;

        // Create Swing Timer that fires every 1000 milliseconds (1 second)
        this.swingTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                secondsRemaining--;
                
                if (QuizTimer.this.tickCallback != null) {
                    QuizTimer.this.tickCallback.onTick(secondsRemaining);
                }

                if (secondsRemaining <= 0) {
                    swingTimer.stop();
                    if (QuizTimer.this.timeoutCallback != null) {
                        QuizTimer.this.timeoutCallback.onTimeout();
                    }
                }
            }
        });
    }

    /**
     * Starts the countdown timer.
     */
    public void start() {
        if (!swingTimer.isRunning()) {
            if (tickCallback != null) {
                tickCallback.onTick(secondsRemaining);
            }
            swingTimer.start();
        }
    }

    /**
     * Stops the countdown timer.
     */
    public void stop() {
        if (swingTimer.isRunning()) {
            swingTimer.stop();
        }
    }

    /**
     * Resets the remaining time to total duration without starting it.
     */
    public void reset() {
        stop();
        this.secondsRemaining = totalSeconds;
        if (tickCallback != null) {
            tickCallback.onTick(secondsRemaining);
        }
    }

    /**
     * Resets and immediately restarts the countdown.
     */
    public void restart() {
        reset();
        start();
    }

    /**
     * Checks if timer is currently active.
     */
    public boolean isRunning() {
        return swingTimer.isRunning();
    }

    /**
     * Returns remaining seconds.
     */
    public int getSecondsRemaining() {
        return secondsRemaining;
    }
}
