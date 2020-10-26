package com.sebbaindustries.advancedafk.global;

import com.sebbaindustries.advancedafk.Core;

import java.util.logging.Level;

/**
 * @author <b>SebbaIndustries</b>
 * @version <b>1.0</b>
 */
public class ServerPlugin {

    private Core core;

    private void coreDump() {
        core.getLogger().log(Level.SEVERE, "Plugin core dumped due to illegal access of ServerPlugin class!");
        throw new IllegalAccessError("Plugin core dumped due to illegal access of ServerPlugin class!");
    }

    public static ServerPlugin INSTANCE() {
        return new ServerPlugin();
    }

    public final void load(Core core) {
        this.core = core;
    }

    public final void initialize() {
        if (core == null) {
            coreDump();
            return;
        }
        Core.globalCore = new GlobalCore(core);
    }

    public final void terminate() {
        if (core == null) {
            coreDump();
            return;
        }
        Core.gCore().terminate();
    }

}
