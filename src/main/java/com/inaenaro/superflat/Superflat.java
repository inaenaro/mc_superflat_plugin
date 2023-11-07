package com.inaenaro.superflat;

import org.bukkit.plugin.java.JavaPlugin;

public final class Superflat extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("start");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("end");
    }
}
