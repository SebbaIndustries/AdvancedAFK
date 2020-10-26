package com.sebbaindustries.advancedafk.commands.actions;

import com.sebbaindustries.advancedafk.commands.components.CommandFactory;
import com.sebbaindustries.advancedafk.commands.components.ICmd;
import com.sebbaindustries.advancedafk.commands.components.ITab;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AdvancedAFK extends CommandFactory implements ICmd, ITab {

    @Override
    public String command() {
        return "advancedafk";
    }

    @Override
    public String permission() {
        return "aafk.command";
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String[] args) {

    }

    @Override
    public List<String> complete(@NotNull CommandSender sender, @NotNull String[] args) {
        return null;
    }
}
