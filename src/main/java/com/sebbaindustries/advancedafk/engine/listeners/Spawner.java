package com.sebbaindustries.advancedafk.engine.listeners;

import com.sebbaindustries.advancedafk.Core;
import com.sebbaindustries.advancedafk.engine.buffer.components.BufferedPlayer;
import org.bukkit.Location;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.SpawnerSpawnEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class Spawner implements Listener {

    @EventHandler
    public void onMobSpawnerSpawn(SpawnerSpawnEvent e) {

        CreatureSpawner spawner = e.getSpawner();
        Location spawnerLocation = spawner.getLocation();

        Collection<Player> near = spawnerLocation.getNearbyPlayers(16);

        List<Boolean> afkList = new ArrayList<>();

        near.forEach(player -> {
            for (Map.Entry<Player, BufferedPlayer> entry : Core.gCore().detectionEngine.dataBuffer.players.entrySet()) {
                if (!entry.getKey().getName().equals(player.getName())) continue;
                if (player.hasPermission("aafk.bullshit")) {
                    afkList.add(false);
                    break;
                }
                afkList.add(entry.getValue().isAFK());
                break;
            }
        });

        boolean cancelSpawnerEvent = true;
        for (boolean bool : afkList) {
            if (!bool) {
                cancelSpawnerEvent = false;
                break;
            }
        }

        if (cancelSpawnerEvent) e.setCancelled(true);

    }

}
