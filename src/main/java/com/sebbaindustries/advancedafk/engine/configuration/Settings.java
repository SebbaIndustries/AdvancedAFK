package com.sebbaindustries.advancedafk.engine.configuration;

import com.sebbaindustries.advancedafk.Core;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Settings {

    public double detectionX = 0.5;
    public double detectionY = 0.3;
    public double detectionZ = 0.5;
    public float detectionYaw = 5.0f;
    public float detectionPitch = 1.0f;
    public int detectionBufferLimit = 16;
    public int detectionPoints = 4;
    public int afkKickTime = 600;
    public int afkKickWarn = 10;
    public int afkKickPlayers = 20;
    public double afkKickTPS = 16.4;
    public int afkKickTPSTime = 10;
    private Properties configuration = null;

    public Settings() {
        configuration = new Properties();
        try {
            configuration.load(new FileInputStream(Core.getPlugin(Core.class).getDataFolder() + "/configuration.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        detectionX = Double.parseDouble(configuration.getProperty("detection.x"));
        detectionY = Double.parseDouble(configuration.getProperty("detection.y"));
        detectionZ = Double.parseDouble(configuration.getProperty("detection.z"));
        detectionYaw = Float.parseFloat(configuration.getProperty("detection.yaw"));
        detectionPitch = Float.parseFloat(configuration.getProperty("detection.pitch"));
        detectionBufferLimit = Integer.parseInt(configuration.getProperty("detection.buffer.limit"));
        detectionPoints = Integer.parseInt(configuration.getProperty("detection.points"));

        afkKickTime = Integer.parseInt(configuration.getProperty("afk.kick.time"));
        afkKickPlayers = Integer.parseInt(configuration.getProperty("afk.kick.players"));
        afkKickTPS = Double.parseDouble(configuration.getProperty("afk.kick.tps"));
        afkKickTPSTime = Integer.parseInt(configuration.getProperty("afk.kick.tps.time"));
        afkKickWarn = Integer.parseInt(configuration.getProperty("afk.kick.warn"));
    }

}
