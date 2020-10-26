package com.sebbaindustries.advancedafk.global;

import com.sebbaindustries.advancedafk.Core;
import com.sebbaindustries.advancedafk.commands.CommandManager;
import com.sebbaindustries.advancedafk.engine.DetectionEngine;

import java.util.logging.Level;

/**
 * @author <b>SebbaIndustries</b>
 * @version <b>1.0</b>
 */
public class GlobalCore {

    private final Core core;

    //public FileManager fileManager;
    //public Setting setting;
    //public Message message;
    public CommandManager commandManager;
    public DetectionEngine detectionEngine;

    public GlobalCore(Core core) {
        this.core = core;

        //this.fileManager = new FileManager(core);

        //this.message = new Message();
        //this.setting = new Setting();
        this.commandManager = new CommandManager(core);
        detectionEngine = new DetectionEngine();
        detectionEngine.initialize();
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
