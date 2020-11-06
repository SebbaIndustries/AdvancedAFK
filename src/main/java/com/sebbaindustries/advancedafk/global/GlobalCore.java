package com.sebbaindustries.advancedafk.global;

import com.sebbaindustries.advancedafk.Core;
import com.sebbaindustries.advancedafk.commands.CommandManager;
import com.sebbaindustries.advancedafk.engine.DetectionEngine;
import com.sebbaindustries.advancedafk.engine.configuration.FileManager;
import com.sebbaindustries.advancedafk.engine.configuration.Messages;
import com.sebbaindustries.advancedafk.engine.configuration.Settings;
import com.sebbaindustries.advancedafk.engine.listeners.Listeners;
import com.sebbaindustries.advancedafk.engine.profiler.Timings;

import java.util.logging.Level;

/**
 * @author SebbaIndustries
 * @version 1.0
 */
public class GlobalCore {

    public final Core core;

    public FileManager fileManager;
    public Settings settings;
    public Messages messages;
    public CommandManager commandManager;
    public DetectionEngine detectionEngine;
    public Timings timings;

    public GlobalCore(Core core) {
        this.core = core;

        this.fileManager = new FileManager(core);

        this.messages = new Messages();
        this.settings = new Settings();
        this.timings = new Timings();
        this.commandManager = new CommandManager(core);
        detectionEngine = new DetectionEngine();
        new Listeners(core);
    }

    /**
     * Logs normal #INFO style message to the console
     * @param message String message, color will be default
     */
    public void log(String message) {
        core.getLogger().log(Level.INFO, message);
    }

    /**
     * Logs normal #ERROR style message to the console
     * @param message String message, color will be red
     */
    public void logSevere(String message) {
        core.getLogger().log(Level.SEVERE, message);
    }

    /**
     * Logs normal #WARN style message to the console
     * @param message String message, color will be yellow
     */
    public void logWarn(String message) {
        core.getLogger().log(Level.WARNING, message);
    }

    /**
     * Terminates AdvancedAFK detection engine
     */
    public void terminate() {
        log("Terminating AdvancedAFK plugin, please wait!");
        detectionEngine.terminate();
        log("AdvancedAFK was successfully terminated!");
    }

}
