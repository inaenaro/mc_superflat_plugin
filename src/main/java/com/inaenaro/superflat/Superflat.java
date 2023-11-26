package com.inaenaro.superflat;

import com.inaenaro.superflat.events.Events;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.Objects;

public final class Superflat extends JavaPlugin {

    private static Superflat instance;
    @Override
    public void onEnable() {
        instance = this;
        registerSuperflatEnchantment();
        getServer().getPluginManager().registerEvents(new Events(), this);
        Objects.requireNonNull(getCommand("end-portal")).setExecutor(new CommandClass());
    }

    @Override
    public void onDisable() {
    }

    public static Superflat getInstance() {
        return instance;
    }

    public void registerSuperflatEnchantment() {
        try {
            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, true);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        try {
            NamespacedKey id = new NamespacedKey(this, "superflat");
            SuperflatEnchantMent enchantment = new SuperflatEnchantMent(id);
            Enchantment.registerEnchantment(enchantment);
        }
        catch (IllegalArgumentException ignored){
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
