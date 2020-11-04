package com.sebbaindustries.advancedafk.engine.configuration;

import com.sebbaindustries.advancedafk.Core;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

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

    public static String get(String name) {
        return Core.gCore().messages.messages.getProperty(name);
    }

}
