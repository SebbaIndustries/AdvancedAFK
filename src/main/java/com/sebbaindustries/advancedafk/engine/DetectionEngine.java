package com.sebbaindustries.advancedafk.engine;

import com.sebbaindustries.advancedafk.Core;
import com.sebbaindustries.advancedafk.engine.buffer.DataBuffer;

import java.util.concurrent.CompletableFuture;

/**
 * @author SebbaIndustries
 * @version 1.0
 */
public class DetectionEngine {

    public DataBuffer dataBuffer;
    private volatile boolean running = false;

    private void detectionLoop() {
        while (running) {
            long currentTime = System.currentTimeMillis();
            // Detection loop
            dataBuffer.update();
            dataBuffer.compute();
            dataBuffer.clean();
            long passedTime = System.currentTimeMillis();

            // Speed check
            long delta = passedTime - currentTime;
            Core.gCore().timings.addDelta(delta);
            if (delta >= 1000L) {
                Core.gCore().logSevere("AdvancedAFK engine thread is running slow. Detection time took " + delta + "ms!");
                continue;
            }

            try {
                Thread.sleep((1000L - delta));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Creates an instance for AdvancedAFK engine
     */
    public void initialize() {
        running = true;
        dataBuffer = new DataBuffer();
        CompletableFuture.supplyAsync(() -> {
            detectionLoop();
            return null;
        }).exceptionally(e -> {
            e.printStackTrace();
            terminate();
            return null;
        });
    }

    /**
     * Terminates AdvancedAFK instance
     */
    public void terminate() {
        // If engine is not running don't try to terminate it
        if (!running) {
            Core.gCore().logSevere("There was an failed attempt to terminate detection engine!");
            return;
        }
        running = false;
        // Engine shutdown can take up to 1000ms, we are pausing the main thread so engine can shutdown normaly
        try {
            Thread.sleep(1500L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Core.gCore().log("Detection engine terminated successfully!");
    }

}
