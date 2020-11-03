package com.sebbaindustries.advancedafk.engine.listeners;

import com.sebbaindustries.advancedafk.Core;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * @author sebbaindustries
 * @version 1.0
 */
public final class Quit implements Listener {

    /**
     * Activates when player quits
     *
     * @param e Event
     */
    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent e) {
        Core.gCore().detectionEngine.dataBuffer.remove(e.getPlayer());
    }

}
