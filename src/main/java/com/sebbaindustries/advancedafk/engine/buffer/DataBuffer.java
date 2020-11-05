package com.sebbaindustries.advancedafk.engine.buffer;

import com.sebbaindustries.advancedafk.Core;
import com.sebbaindustries.advancedafk.engine.buffer.components.BufferedPlayer;
import com.sebbaindustries.advancedafk.engine.configuration.Messages;
import com.sebbaindustries.advancedafk.utils.Color;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.concurrent.ConcurrentHashMap;

public class DataBuffer {

    private int tpsStrikes = 0;

    public ConcurrentHashMap<Player, BufferedPlayer> players = new ConcurrentHashMap<>();

    public void add(Player player) {
        players.put(player, new BufferedPlayer(player));
    }

    public void remove(Player player) {
        players.remove(player);
    }

    public void clean() {
        double tps = Bukkit.getServer().getTPS()[0];
        if (tps <= Core.gCore().settings.afkKickTPS) tpsStrikes++;
        if (tps > Core.gCore().settings.afkKickTPS && tpsStrikes > 0) tpsStrikes--;
        if (tpsStrikes >= Core.gCore().settings.afkKickTPSTime) {
            cleanPlayers(true);
            tpsStrikes = 0;
            return;
        }
        if (players.size() < Core.gCore().settings.afkKickPlayers) return;
        cleanPlayers(false);
    }

    private void cleanPlayers(boolean tps) {
        players.forEach((player, buffer) -> {
            if (buffer.getAfkTime() < Core.gCore().settings.afkKickTime) {
                int timeLeft = Core.gCore().settings.afkKickTime - Core.gCore().settings.afkKickWarn;
                if (timeLeft > 0 && buffer.getAfkTime() >= timeLeft) {
                    if (!buffer.bypassAFK()) player.sendMessage(
                            Color.color(
                                    Messages.get("afk_pre_kick").replace("{seconds}", String.valueOf(Core.gCore().settings.afkKickTime - buffer.getAfkTime()))
                            )
                    );
                }
                return;
            }
            if (buffer.bypassAFK()) {
                return;
            }
            Bukkit.getScheduler().runTask(Core.gCore().core, () -> {
                if (tps) {
                    player.kickPlayer(Color.color(Messages.get("afk_kick_tps")));
                    return;
                }
                player.kickPlayer(Color.color(Messages.get("afk_kick_normal")));
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
            if (points >= Core.gCore().settings.detectionPoints) {
                buffer.flagAFK();
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
            diffX[i] = Math.abs(player.getLocations()[i].getX() - player.getLocations()[i + 1].getX());
            diffY[i] = Math.abs(player.getLocations()[i].getY() - player.getLocations()[i + 1].getY());
            diffZ[i] = Math.abs(player.getLocations()[i].getZ() - player.getLocations()[i + 1].getZ());
            diffYaw[i] = Math.abs(player.getLocations()[i].getYaw() - player.getLocations()[i + 1].getYaw());
            diffPitch[i] = Math.abs(player.getLocations()[i].getPitch() - player.getLocations()[i + 1].getPitch());
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
