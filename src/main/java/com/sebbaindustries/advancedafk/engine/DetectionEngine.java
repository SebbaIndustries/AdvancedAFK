package com.sebbaindustries.advancedafk.engine;

import com.sebbaindustries.advancedafk.Core;
import com.sebbaindustries.advancedafk.engine.components.DataBuffer;

import java.util.concurrent.CompletableFuture;

public class DetectionEngine {

    private volatile boolean running = false;
    private DataBuffer dataBuffer;

    private void detectionLoop() {
        while (running) {
            long currentTime = System.currentTimeMillis();
            dataBuffer.clean();
            dataBuffer.update();
            dataBuffer.compute();
            long passedTime = System.currentTimeMillis();
            long delta = passedTime - currentTime;
            Core.gCore().log(String.valueOf(delta));
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

    public void terminate() {
        if (!running) {
            Core.gCore().logSevere("There was an failed attempt to terminate detection engine!");
            return;
        }
        running = false;
        try {
            Thread.sleep(1500L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Core.gCore().log("Detection engine terminated successfully!");
    }

}
