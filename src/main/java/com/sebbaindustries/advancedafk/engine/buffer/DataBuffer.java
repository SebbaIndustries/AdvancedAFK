package com.sebbaindustries.advancedafk.engine.buffer;

import com.sebbaindustries.advancedafk.Core;
import com.sebbaindustries.advancedafk.engine.buffer.components.BufferedPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class DataBuffer {

    public HashMap<Player, BufferedPlayer> players = new HashMap<>();

    public void add(Player player) {
        players.put(player, new BufferedPlayer(player));
    }

    public void remove(Player player) {
        players.remove(player);
    }

    public void clean() {
       players.forEach((player, buffer) -> {
           if (buffer.getAfkTime() < Core.gCore().settings.afkKickTime) {
               return;
           }
           if (buffer.bypassAFK()) {
               return;
           }
           Bukkit.getScheduler().runTask(Core.gCore().core, () -> {
               // TODO: Add kick message
               player.kickPlayer("Yeet!");
           });
       });
    }

    public void update() {
        players.forEach((player, buffer) -> buffer.updateLocation(player));
    }

    public void compute() {
        players.forEach((player, buffer) -> {
            if (buffer.getPause() > 0) {
                buffer.updateTime();
                return;
            }
            int points = difference(buffer);
            // TODO: Remove this
            System.out.println("Points: " + points);
            if (points >= Core.gCore().settings.detectionPoints) {
                buffer.flagAFK();
                // TODO: Remove this
                player.sendMessage("AFK " + buffer.getAfkTime());
                return;
            }
            buffer.reset();
        });
    }

    private int difference(BufferedPlayer player) {
        double[] diffX = new double[59];
        double[] diffY = new double[59];
        double[] diffZ = new double[59];
        float[] diffYaw = new float[59];
        float[] diffPitch = new float[59];

        for (int i = 0; i < 59; i++) {
            diffX[i] = Math.abs(player.getLocations()[i].getX() - player.getLocations()[i+1].getX());
            diffY[i] = Math.abs(player.getLocations()[i].getY() - player.getLocations()[i+1].getY());
            diffZ[i] = Math.abs(player.getLocations()[i].getZ() - player.getLocations()[i+1].getZ());
            diffYaw[i] = Math.abs(player.getLocations()[i].getYaw() - player.getLocations()[i+1].getYaw());
            diffPitch[i] = Math.abs(player.getLocations()[i].getPitch() - player.getLocations()[i+1].getPitch());
        }

        double xPointA = 0.0;
        double xPointB = 0.0;


        double yPointA = 0.0;
        double yPointB = 0.0;

        double zPointA = 0.0;
        double zPointB = 0.0;

        float yawPointA = 0.0f;
        float yawPointB = 0.0f;

        float pitchPointA = 0.0000f;
        float pitchPointB = 0.0000f;

        for (int i = 0; i < 20; i++) {
            xPointA += diffX[i];
            yPointA += diffY[i];
            zPointA += diffZ[i];
            yawPointA += diffYaw[i];
            pitchPointA += diffPitch[i];
        }

        for (int i = 39; i < 59; i++) {
            xPointB += diffX[i];
            yPointB += diffY[i];
            zPointB += diffZ[i];
            yawPointB += diffYaw[i];
            pitchPointB += diffPitch[i];
        }

        xPointA /= 20;
        xPointB /= 20;

        yPointA /= 20;
        yPointB /= 20;

        zPointA /= 20;
        zPointB /= 20;

        yawPointA /= 20;
        yawPointB /= 20;

        pitchPointA /= 20;
        pitchPointB /= 20;

        int total = 0;

        if (Math.abs(xPointA - xPointB) <= Core.gCore().settings.detectionX) total++;
        if (Math.abs(yPointA - yPointB) <= Core.gCore().settings.detectionY) total++;
        if (Math.abs(zPointA - zPointB) <= Core.gCore().settings.detectionZ) total++;
        if (Math.abs(yawPointA - yawPointB) <= Core.gCore().settings.detectionYaw) total++;
        if (Math.abs(pitchPointA - pitchPointB) <= Core.gCore().settings.detectionPitch) total++;

        return total;

    }


}
