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
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

public class AdvancedAFK extends CommandFactory implements ICmd, ITab {

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
        switch (args[0]) {
            case "list" -> list();
            case "clean" -> clean();
            case "lookup" -> lookup();
            case "reload" -> reload();
            default -> help();
        }
    }

    private CommandSender sender;
    private String[] args;

    private void help() {
        // TODO Add help message
        String s = "Help Message";
        sender.sendMessage(Color.color(s));
    }

    private void list() {
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
        message.deleteCharAt((message.length()-1));
        message.deleteCharAt((message.length()-1));
        sender.sendMessage(Color.color(message.toString()));
    }

    private void clean() {
        boolean useForce = false;
        for (var s : args) if (s.equalsIgnoreCase("--force")) { useForce = true;break; }
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

        sender.sendMessage(Color.color("[AdvancedAFK] Removed " + i.toString() + " players!"));
    }

    private void lookup() {
        if (args.length < 1) {
            sender.sendMessage("IDIOT you FORGOT the FUCKING player!");
            return;
        }
        String playerName = args[1];
        boolean verbose = false;
        for (var s : args) if (s.equalsIgnoreCase("-v")) { verbose = true;break; }
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
            sender.sendMessage("[AdvancedAFK] Player not found!");
        }
    }

    private boolean found = false;

    private void nonVerboseLookup(Player player, BufferedPlayer buffer) {
        found = true;
        sender.sendMessage(Color.color("Player lookup: " + player.getName() + "(Non-Verbose)"));
        sender.sendMessage(Color.color("AFK time: " + buffer.getAfkTime()));
        sender.sendMessage(Color.color("Bypass AFK: " + buffer.bypassAFK()));
    }

    private void verboseLookup(Player player, BufferedPlayer buffer) {
        found = true;
        sender.sendMessage(Color.color("Player lookup: " + player.getName() + "(Verbose)"));
        sender.sendMessage(ObjectUtils.deserializeObjectToString(buffer));
    }

    private void reload() {
        Core.gCore().settings = new Settings();
        Core.gCore().messages = new Messages();
        sender.sendMessage("[AdvancedAFK] Reload finished!");
    }

    @Override
    public List<String> complete(@NotNull CommandSender sender, @NotNull String[] args) {
        return null;
    }
}
