package com.github.qpcrummer.processing;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public final class Timer {
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    public static void wait(long milliseconds, Runnable task) {
        // Complete the CompletableFuture after the specified delay
        scheduler.schedule(task, milliseconds, TimeUnit.MILLISECONDS);
    }
}
