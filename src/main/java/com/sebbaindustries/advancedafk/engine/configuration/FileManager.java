package com.sebbaindustries.advancedafk.engine.configuration;

import com.sebbaindustries.advancedafk.Core;

import java.io.File;

public class FileManager {

    public FileManager(Core core) {
        generateConfiguration(core);
        generateMessages(core);
    }

    /*
    configuration.properties
     */
    public File configuration;

    public void generateConfiguration(Core core) {
        if (configuration == null) {
            configuration = new File(core.getDataFolder(), "configuration.properties");
        }
        if (!configuration.exists()) {
            core.saveResource("configuration.properties", false);
        }
    }

     /*
    messages.properties
     */
    public File messages;

    /**
     * Generates messages.properties File
     */
    public final void generateMessages(Core core) {
        if (messages == null) {
            messages = new File(core.getDataFolder(), "messages.properties");
        }
        if (!messages.exists()) {
            core.saveResource("messages.properties", false);
        }
    }

    /**
     * Generates README.md File
     */
    public final void generateREADME(Core core) {
        File README = new File(core.getDataFolder(), "README.md");

        if (!README.exists()) {
            core.saveResource("README.md", false);
        }
    }

}
