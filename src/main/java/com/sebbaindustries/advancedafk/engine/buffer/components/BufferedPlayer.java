package com.sebbaindustries.advancedafk.engine.buffer.components;

import com.sebbaindustries.advancedafk.Core;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class BufferedPlayer {

    public BufferedPlayer(Player player) {
        Arrays.fill(locations, new LocationPoint(
                player.getLocation().getX(),
                player.getLocation().getY(),
                player.getLocation().getZ(),
                player.getLocation().getYaw(),
                player.getLocation().getPitch()
        ));
        pause = 10;
        if (player.hasPermission("aafk.bypass.kick")) bypass = true;
    }

    private boolean bypass = false;
    private boolean isAFK = false;
    private int afkTime = 0;
    private int afkTimeBuffer = 0;
    private int pause;
    private final LocationPoint[] locations = new LocationPoint[60];

    public boolean bypassAFK() {
        return bypass;
    }
    public int getPause() {
        return pause;
    }
    public int getAfkTime() {
        return afkTime;
    }
    public void updateTime() {
        pause--;
    }
    public LocationPoint[] getLocations() {
        return locations;
    }

    public void flagAFK() {
        if (afkTimeBuffer < Core.gCore().settings.detectionBufferLimit) {
            afkTimeBuffer++;
            return;
        }
        afkTime++;
        isAFK = true;
    }

    public void reset() {
        isAFK = false;
        if (afkTime != 0) {
            afkTime = 0;
            afkTimeBuffer = 0;
            pause = 10;
            return;
        }
        afkTimeBuffer = 0;
    }

    public void updateLocation(Player p) {
        if (locations.length - 1 >= 0) System.arraycopy(locations, 0, locations, 1, locations.length - 1);
        locations[0] = new LocationPoint(
                p.getLocation().getX(),
                p.getLocation().getY(),
                p.getLocation().getZ(),
                p.getLocation().getYaw(),
                p.getLocation().getPitch()
        );
    }


}
