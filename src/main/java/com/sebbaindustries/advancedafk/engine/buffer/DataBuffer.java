package com.sebbaindustries.advancedafk.engine.buffer;

import com.sebbaindustries.advancedafk.Core;
import com.sebbaindustries.advancedafk.engine.buffer.components.BufferedPlayer;
import com.sebbaindustries.advancedafk.engine.configuration.Messages;
import com.sebbaindustries.advancedafk.utils.Color;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author SebbaIndustries
 * @version 1.0
 */
public class DataBuffer {

    private int tpsStrikes = 0;

    /**
     * Tread safe bullshit
     */
    public ConcurrentHashMap<Player, BufferedPlayer> players = new ConcurrentHashMap<>();

    /**
     * Adds player instance and creates BufferedPlayer instance in the hashmap
     * @param player Player instance
     */
    public void add(Player player) {
        players.put(player, new BufferedPlayer(player));
    }

    /**
     * Removes Player and BufferedPlayer instances from the hashmap
     * @param player Player instance
     */
    public void remove(Player player) {
        players.remove(player);
    }

    /**
     * Cleans afk player form the server
     */
    public void clean() {
        // Get current tps
        double tps = Bukkit.getServer().getTPS()[0];
        // if the tps is low add strike to the counter
        if (tps <= Core.gCore().settings.afkKickTPS) tpsStrikes++;
        // if the tps is normal remove strikes from the counter
        if (tps > Core.gCore().settings.afkKickTPS && tpsStrikes > 0) tpsStrikes--;
        // if the tps strikes exceed the limit do a cleanup
        if (tpsStrikes >= Core.gCore().settings.afkKickTPSTime) {
            cleanPlayers(true);
            tpsStrikes = 0;
            return;
        }

        // Normal cleanup
        if (players.size() < Core.gCore().settings.afkKickPlayers) return;
        cleanPlayers(false);
    }

    /**
     * Preforms a cleaning task
     * @param tps Tps flag for message reason
     */
    private void cleanPlayers(boolean tps) {
        players.forEach((player, buffer) -> {
            if (buffer.getAfkTime() < Core.gCore().settings.afkKickTime) {

                // Bypass AFK Kick
                if (buffer.bypassAFK()) {
                    return;
                }

                // Warn messages for the player before the kick
                int timeLeft = Core.gCore().settings.afkKickTime - Core.gCore().settings.afkKickWarn;
                if (timeLeft > 0 && buffer.getAfkTime() >= timeLeft) {
                    player.sendMessage(Color.format(
                            Messages.get("afk_pre_kick").replace("{seconds}", String.valueOf(Core.gCore().settings.afkKickTime - buffer.getAfkTime()))
                            ));
                }
                return;
            }

            // Bukkit async abomination
            Bukkit.getScheduler().runTask(Core.gCore().core, () -> {
                if (tps) {
                    player.kickPlayer(Color.format(Messages.get("afk_kick_tps")));
                    return;
                }
                player.kickPlayer(Color.format(Messages.get("afk_kick_normal")));
            });
        });
    }

    /**
     * Updates players locations in the buffer
     */
    public void update() {
        players.forEach((player, buffer) -> buffer.updateLocation(player));
    }

    /**
     * Magic, math, and some weird fuckery... It works don't touch it!
     */
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

        // Fill the arrays with differences between locations
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

        /*
        calculate points on the side A
         */

        for (int i = 0; i < 20; i++) {
            xPointA += diffX[i];
            yPointA += diffY[i];
            zPointA += diffZ[i];
            yawPointA += diffYaw[i];
            pitchPointA += diffPitch[i];
        }

        /*
        calculate points on the side B
         */

        for (int i = 39; i < 59; i++) {
            xPointB += diffX[i];
            yPointB += diffY[i];
            zPointB += diffZ[i];
            yawPointB += diffYaw[i];
            pitchPointB += diffPitch[i];
        }

        /*
        Avg
         */

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

        // flag the "impostors"

        int total = 0;

        if (Math.abs(xPointA - xPointB) <= Core.gCore().settings.detectionX) total++;
        if (Math.abs(yPointA - yPointB) <= Core.gCore().settings.detectionY) total++;
        if (Math.abs(zPointA - zPointB) <= Core.gCore().settings.detectionZ) total++;
        if (Math.abs(yawPointA - yawPointB) <= Core.gCore().settings.detectionYaw) total++;
        if (Math.abs(pitchPointA - pitchPointB) <= Core.gCore().settings.detectionPitch) total++;

        return total;

    }


}
