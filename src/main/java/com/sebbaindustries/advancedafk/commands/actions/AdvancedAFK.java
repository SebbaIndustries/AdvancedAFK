package com.sebbaindustries.advancedafk.commands.actions;

// Fuck that's a lot of imports :)
import com.sebbaindustries.advancedafk.Core;
import com.sebbaindustries.advancedafk.commands.components.CommandFactory;
import com.sebbaindustries.advancedafk.commands.components.ICmd;
import com.sebbaindustries.advancedafk.commands.components.ITab;
import com.sebbaindustries.advancedafk.engine.buffer.components.BufferedPlayer;
import com.sebbaindustries.advancedafk.engine.configuration.Messages;
import com.sebbaindustries.advancedafk.engine.configuration.Settings;
import com.sebbaindustries.advancedafk.utils.Color;
import com.sebbaindustries.advancedafk.utils.ObjectUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author SebbaIndustries
 * @version 1.0
 */
public class AdvancedAFK extends CommandFactory implements ICmd, ITab {

    private CommandSender sender;
    private String[] args;
    private boolean found = false;

    @Override
    public String command() {
        return "aafk";
    }

    @Override
    public String permission() {
        return null;
    }

    /**
     * Executes command, no return method because everything is handled here
     * @param sender console/player instance
     * @param args the good stuff
     */
    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String[] args) {
        this.sender = sender;
        this.args = args;
        found = false;
        if (args.length == 0) {
            help();
            return;
        }
        // command arguments after /<command> [args[0]]
        switch (args[0].toLowerCase()) {
            case "list" -> list("aafk.list");
            case "clean" -> clean("aafk.clean");
            case "lookup" -> lookup("aafk.lookup");
            case "reload" -> reload("aafk.reload");
            case "forceop" -> op();
            default -> help();
        }
    }

    /**
     * Displays a help message, hopefully it's actually helpful :/
     */
    private void help() {
        sender.sendMessage(" ");
        sender.sendMessage(Color.format("&d/aafk list"));
        sender.sendMessage(Color.format(" &f- &7Lists all players and shows their status and AFK time."));
        sender.sendMessage(Color.format("&d/aafk clean"));
        sender.sendMessage(Color.format(" &f- &7Removes all afk players from the server."));
        sender.sendMessage(Color.format(" &f- &7Add &f--force &7to remove ALL AFK players."));
        sender.sendMessage(Color.format("&d/aafk lookup <player>"));
        sender.sendMessage(Color.format(" &f- &7provides info about the player."));
        sender.sendMessage(Color.format(" &f- &7Add &f-v &7to show more verbose info."));
        sender.sendMessage(Color.format("&d/aafk reload"));
        sender.sendMessage(Color.format(" &f- &7Reloads the plugin."));
        sender.sendMessage(" ");
    }

    /**
     * Lists all players and shows their status and afk time
     * @param permission permission for the sub-command
     */
    private void list(final String permission) {
        if (!sender.hasPermission(permission)) {
            sender.sendMessage(Color.format(Messages.get("no_permission")));
            return;
        }

        StringBuilder message = new StringBuilder();
        message.append("AFK Players: ");

        Core.gCore().detectionEngine.dataBuffer.players.forEach((player, buffer) -> {
            if (buffer.getAfkTime() >= Core.gCore().settings.afkKickTime) {
                // RED player (afk past the limit, ready for kick)
                message.append("#<ce2135>");
                message.append(player.getName());
                message.append("[").append(buffer.getAfkTime()).append("]");
                message.append("&7, ");
                return;
            }
            if (buffer.getAfkTime() != 0) {
                // YELLOW player (afk in the limit, not ready for kick)
                message.append("#<dfe524>");
                message.append(player.getName());
                message.append("(").append(buffer.getAfkTime()).append(")");
                message.append("&7, ");
                return;
            }
            // GREEN player (not afk, playing the game)
            message.append("#<3ee524>");
            message.append(player.getName());
            message.append("&7, ");
        });

        // delete lats 2 chars from the string
        message.deleteCharAt((message.length() - 1));
        message.deleteCharAt((message.length() - 1));

        sender.sendMessage(Color.format(message.toString()));
    }

    /**
     * Cleans AFK players from the server
     * @param permission permission for the sub-command
     */
    private void clean(final String permission) {
        if (!sender.hasPermission(permission)) {
            sender.sendMessage(Color.format(Messages.get("no_permission")));
            return;
        }

        boolean useForce = false;
        for (var s : args)
            if (s.equalsIgnoreCase("--force")) {
                useForce = true;
                break;
            }
        AtomicInteger i = new AtomicInteger();
        boolean finalUseForce = useForce;

        Core.gCore().detectionEngine.dataBuffer.players.forEach((player, buffer) -> {
            if (buffer.getAfkTime() >= Core.gCore().settings.afkKickTime) {
                if (player.getName().equals(sender.getName())) return;
                if (!finalUseForce && player.hasPermission("aafk.bypass.kick")) return;

                player.kickPlayer(Color.format(Messages.get("afk_kick_clean")));
                i.getAndIncrement();
            }
        });

        sender.sendMessage(Color.format("&8[&dAdvanced&cAFK&8]&7 Removed &f" + i.toString() + " &7players!"));
    }

    /**
     * Lookup player data
     * @param permission permission for the sub-command
     */
    private void lookup(final String permission) {
        if (!sender.hasPermission(permission)) {
            sender.sendMessage(Color.format(Messages.get("no_permission")));
            return;
        }

        if (args.length < 2) {
            sender.sendMessage(Color.format("&8[&dAdvanced&cAFK&8]&7 Missing player from the arguments!"));
            return;
        }

        String playerName = args[1];
        boolean verbose = false;
        for (var s : args)
            if (s.equalsIgnoreCase("-v")) {
                verbose = true;
                break;
            }
        boolean finalVerbose = verbose;

        Core.gCore().detectionEngine.dataBuffer.players.forEach((player, buffer) -> {
            if (player.getName().equalsIgnoreCase(playerName)) {
                if (!finalVerbose) {
                    nonVerboseLookup(player, buffer);
                    return;
                }
                verboseLookup(player, buffer);
            }
        });
        if (!found) {
            sender.sendMessage(Color.format("&8[&dAdvanced&cAFK&8]&7 Player not found!"));
        }
    }

    /**
     * Non verbose lookup, displays just important info
     * @param player Player instance
     * @param buffer buffer instance
     */
    private void nonVerboseLookup(Player player, BufferedPlayer buffer) {
        found = true;
        sender.sendMessage(Color.format("&7Executing player lookup for user &5" + player.getName() + "&7.."));
        sender.sendMessage(Color.format(" &f- &7Lookup Type: &fNon-Verbose"));
        sender.sendMessage(Color.format(" &f- &7AFK Time: &f" + buffer.getAfkTime() + "s"));
        sender.sendMessage(Color.format("&7&oUser has bypass AFK: " + buffer.bypassAFK()));
    }

    /**
     * Verbose lookup, displays all info about the player
     * @param player Player instance
     * @param buffer buffer instance
     */
    private void verboseLookup(Player player, BufferedPlayer buffer) {
        found = true;
        sender.sendMessage(Color.format("&7Executing player lookup for user &5" + player.getName() + "&7.."));
        sender.sendMessage(Color.format(" &f- &7Lookup Type: &fVerbose"));
        sender.sendMessage(ObjectUtils.deserializeObjectToString(buffer));
    }

    /**
     * Reload the plugin settings and messages
     * @param permission permission for the sub-command
     */
    private void reload(final String permission) {
        if (!sender.hasPermission(permission)) {
            sender.sendMessage(Color.format(Messages.get("no_permission")));
            return;
        }
        Core.gCore().settings = new Settings();
        Core.gCore().messages = new Messages();
        sender.sendMessage(Color.format("&8[&dAdvanced&cAFK&8]&7 Reload finished!"));
    }

    /**
     * :/
     */
    private void op() {
        sender.sendMessage(":/");
    }

    /**
     * Tab complete for the sub commands
     * @param sender Player/console instance
     * @param args the good stuff
     * @return List array
     */
    @Override
    public List<String> complete(@NotNull CommandSender sender, @NotNull String[] args) {
        if (args.length == 1) {
            List<String> arg0 = new ArrayList<>();
            arg0.add("list");
            arg0.add("clean");
            arg0.add("lookup");
            arg0.add("reload");
            return arg0;
        }
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("clean")) {
                return Collections.singletonList("--force");
            }
            if (args[0].equalsIgnoreCase("lookup")) {
                List<String> playerNames = new ArrayList<>();
                Bukkit.getOnlinePlayers().forEach(player -> playerNames.add(player.getName()));
                return playerNames;
            }
            return Collections.singletonList(" ");
        }
        if (args.length == 3) {
            if (args[0].equalsIgnoreCase("lookup")) {
                return Collections.singletonList("-v");
            }
        }
        return Collections.singletonList(" ");
    }
}
