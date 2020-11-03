package com.sebbaindustries.advancedafk.engine.listeners;

import com.sebbaindustries.advancedafk.Core;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * @author sebbaindustries
 * @version 1.0
 */
public final class Join implements Listener {

    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent e) {
        Core.gCore().detectionEngine.dataBuffer.add(e.getPlayer());
    }

}
