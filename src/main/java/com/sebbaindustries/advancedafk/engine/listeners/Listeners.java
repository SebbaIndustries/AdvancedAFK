package com.sebbaindustries.advancedafk.engine.listeners;

import com.sebbaindustries.advancedafk.Core;
import org.jetbrains.annotations.NotNull;

public class Listeners {

    public Listeners(@NotNull final Core core) {
        core.getServer().getPluginManager().registerEvents(new Join(), core);
        core.getServer().getPluginManager().registerEvents(new Quit(), core);
    }

}
