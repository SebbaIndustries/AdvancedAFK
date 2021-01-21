package com.sebbaindustries.advancedafk.engine.buffer.components;

import com.sebbaindustries.advancedafk.Core;
import org.bukkit.entity.Player;

import java.util.Arrays;

/**
 * @author SebbaIndustries
 * @version 1.0
 */
public class BufferedPlayer {

    private final LocationPoint[] locations = new LocationPoint[60];
    private boolean bypass = false;
    private boolean isAFK = false;
    private int afkTime = 0;
    private int afkTimeBuffer = 0;
    private int pause;

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

    /**
     * Check is player has permission to bypass normal kick
     * @return boolean for permission
     */
    public boolean bypassAFK() {
        return bypass;
    }

    /**
     * @return Pause time
     */
    public int getPause() {
        return pause;
    }

    /**
     * @return Players AFK time
     */
    public int getAfkTime() {
        return afkTime;
    }

    /**
     * Updates pause time
     */
    public void updateTime() {
        pause--;
    }

    public boolean isAFK() {
        return isAFK;
    }

    /**
     * Location array
     * @return locations
     */
    public LocationPoint[] getLocations() {
        return locations;
    }

    /**
     * flags players as afk
     */
    public void flagAFK() {
        if (afkTimeBuffer < Core.gCore().settings.detectionBufferLimit) {
            afkTimeBuffer++;
            return;
        }
        afkTime++;
        isAFK = true;
    }

    /**
     * Resets afk timer
     */
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

    /**
     * Update first location in the array and shift old ones by 1
     * @param p player instance
     */
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
