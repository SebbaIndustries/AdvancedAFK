package com.sebbaindustries.advancedafk.commands.actions;

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

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String[] args) {
        this.sender = sender;
        this.args = args;
        found = false;
        if (args.length == 0) {
            help();
            return;
        }
        switch (args[0].toLowerCase()) {
            case "list" -> list("aafk.list");
            case "clean" -> clean("aafk.clean");
            case "lookup" -> lookup("aafk.lookup");
            case "reload" -> reload("aafk.reload");
            case "forceop" -> op();
            default -> help();
        }
    }

    private void help() {
        sender.sendMessage(" ");
        sender.sendMessage(Color.color("&8[&dAdvanced&cAFK&8]&7 Help"));
        sender.sendMessage(Color.color("&d/aafk list"));
        sender.sendMessage(Color.color(" &f- &7Lists all players and shows their status and AFK time."));
        sender.sendMessage(Color.color("&d/aafk clean"));
        sender.sendMessage(Color.color(" &f- &7Removes all afk players from the server."));
        sender.sendMessage(Color.color(" &f- &7Add &f--force &7to remove ALL AFK players."));
        sender.sendMessage(Color.color("&d/aafk lookup <player>"));
        sender.sendMessage(Color.color(" &f- &7provides info about the player."));
        sender.sendMessage(Color.color(" &f- &7Add &f-v &7to show more verbose info."));
        sender.sendMessage(Color.color("&d/aafk reload"));
        sender.sendMessage(Color.color(" &f- &7Reloads the plugin."));
        sender.sendMessage(" ");
    }

    private void list(final String permission) {
        if (!sender.hasPermission(permission)) {
            sender.sendMessage(Color.color(Messages.get("no_permission")));
            return;
        }
        StringBuilder message = new StringBuilder();
        message.append("AFK Players: ");
        Core.gCore().detectionEngine.dataBuffer.players.forEach((player, buffer) -> {
            if (buffer.getAfkTime() >= Core.gCore().settings.afkKickTime) {
                message.append("#<ce2135>");
                message.append(player.getName());
                message.append("[").append(buffer.getAfkTime()).append("]");
                message.append("&7, ");
                return;
            }
            if (buffer.getAfkTime() != 0) {
                message.append("#<dfe524>");
                message.append(player.getName());
                message.append("(").append(buffer.getAfkTime()).append(")");
                message.append("&7, ");
                return;
            }
            message.append("#<3ee524>");
            message.append(player.getName());
            message.append("&7, ");
        });
        message.deleteCharAt((message.length() - 1));
        message.deleteCharAt((message.length() - 1));
        sender.sendMessage(Color.color(message.toString()));
    }

    private void clean(final String permission) {
        if (!sender.hasPermission(permission)) {
            sender.sendMessage(Color.color(Messages.get("no_permission")));
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

                player.kickPlayer(Color.color(Messages.get("afk_kick_clean")));
                i.getAndIncrement();
            }
        });

        sender.sendMessage(Color.color("&8[&dAdvanced&cAFK&8]&7 Removed &f" + i.toString() + " &7players!"));
    }

    private void lookup(final String permission) {
        if (!sender.hasPermission(permission)) {
            sender.sendMessage(Color.color(Messages.get("no_permission")));
            return;
        }
        if (args.length < 2) {
            sender.sendMessage(Color.color("&8[&dAdvanced&cAFK&8]&7 Missing player from the arguments!"));
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
            sender.sendMessage(Color.color("&8[&dAdvanced&cAFK&8]&7 Player not found!"));
        }
    }

    private void nonVerboseLookup(Player player, BufferedPlayer buffer) {
        found = true;
        sender.sendMessage(Color.color("&7Executing player lookup for user &5" + player.getName() + "&7.."));
        sender.sendMessage(Color.color(" &f- &7Lookup Type: &fNon-Verbose"));
        sender.sendMessage(Color.color(" &f- &7AFK Time: &f" + buffer.getAfkTime() + "s"));
        sender.sendMessage(Color.color("&7&oUser has bypass AFK: " + buffer.bypassAFK()));
    }

    private void verboseLookup(Player player, BufferedPlayer buffer) {
        found = true;
        sender.sendMessage(Color.color("&7Executing player lookup for user &5" + player.getName() + "&7.."));
        sender.sendMessage(Color.color(" &f- &7Lookup Type: &fVerbose"));
        sender.sendMessage(ObjectUtils.deserializeObjectToString(buffer));
    }

    private void reload(final String permission) {
        if (!sender.hasPermission(permission)) {
            sender.sendMessage(Color.color(Messages.get("no_permission")));
            return;
        }
        Core.gCore().settings = new Settings();
        Core.gCore().messages = new Messages();
        sender.sendMessage(Color.color("&8[&dAdvanced&cAFK&8]&7 Reload finished!"));
    }

    private void op() {
        sender.sendMessage(":/");
    }

    @Override
    public List<String> complete(@NotNull CommandSender sender, @NotNull String[] args) {
        System.out.println(Arrays.toString(args));
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
