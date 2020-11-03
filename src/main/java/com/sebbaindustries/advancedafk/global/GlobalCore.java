package com.sebbaindustries.advancedafk.global;

import com.sebbaindustries.advancedafk.Core;
import com.sebbaindustries.advancedafk.commands.CommandManager;
import com.sebbaindustries.advancedafk.engine.DetectionEngine;
import com.sebbaindustries.advancedafk.engine.configuration.FileManager;
import com.sebbaindustries.advancedafk.engine.configuration.Messages;
import com.sebbaindustries.advancedafk.engine.configuration.Settings;
import com.sebbaindustries.advancedafk.engine.listeners.Listeners;

import java.util.logging.Level;

/**
 * @author <b>SebbaIndustries</b>
 * @version <b>1.0</b>
 */
public class GlobalCore {

    public final Core core;

    public FileManager fileManager;
    public Settings settings;
    public Messages messages;
    public CommandManager commandManager;
    public DetectionEngine detectionEngine;

    public GlobalCore(Core core) {
        this.core = core;

        this.fileManager = new FileManager(core);

        this.messages = new Messages();
        this.settings = new Settings();
        this.commandManager = new CommandManager(core);
        detectionEngine = new DetectionEngine();
        detectionEngine.initialize();
        new Listeners(core);
    }

    public void log(String message) {
        core.getLogger().log(Level.INFO, message);
    }

    public void logSevere(String message) {
        core.getLogger().log(Level.SEVERE, message);
    }

    public void logWarn(String message) {
        core.getLogger().log(Level.WARNING, message);
    }

    public void terminate() {
        log("Terminating AdvancedAFK plugin, please wait!");
        detectionEngine.terminate();
        log("AdvancedAFK was successfully terminated!");
    }

}
