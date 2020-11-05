package com.sebbaindustries.advancedafk.commands.components;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author SebbaIndustries
 * @version 1.0
 */
public interface ITab {

    List<String> complete(@NotNull CommandSender sender, @NotNull String[] args);

}
