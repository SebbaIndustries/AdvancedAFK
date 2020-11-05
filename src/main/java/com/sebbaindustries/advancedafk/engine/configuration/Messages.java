package com.sebbaindustries.advancedafk.engine.configuration;

import com.sebbaindustries.advancedafk.Core;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @author SebbaIndustries
 * @version 1.0
 */
public class Messages {

    public Properties messages = null;

    public Messages() {
        messages = new Properties();
        try {
            messages.load(new FileInputStream(Core.getPlugin(Core.class).getDataFolder() + "/messages.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets message from the properties file
     * @param name Message "name"
     * @return Un-Formatted message
     */
    public static String get(String name) {
        return Core.gCore().messages.messages.getProperty(name);
    }

}
