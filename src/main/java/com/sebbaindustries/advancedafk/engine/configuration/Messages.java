package com.sebbaindustries.advancedafk.engine.configuration;

import com.sebbaindustries.advancedafk.Core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
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
            messages.load(new InputStreamReader(new FileInputStream(new File(Core.getPlugin(Core.class).getDataFolder() + "/messages.properties")), StandardCharsets.UTF_8));
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
