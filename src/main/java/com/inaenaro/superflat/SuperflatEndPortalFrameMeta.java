package com.inaenaro.superflat;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SuperflatEndPortalFrameMeta {
    public static final ItemMeta superflatEndPortalFrameMeta = getSuperflatEndPortalFrameMeta();

    public static ItemMeta getSuperflatEndPortalFrameMeta() {
        ItemStack item = new ItemStack(Material.END_PORTAL_FRAME, 1);
        ItemMeta meta = item.getItemMeta();
        Objects.requireNonNull(meta).setDisplayName(ChatColor.GREEN + "Super End Portal Frame");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "It requires 5*5 space with this in the center.");
        lore.add(ChatColor.RED + "" + ChatColor.BOLD + "Once you place this, you can't break it!");
        meta.setLore(lore);
        SuperflatEnchantMent enchantment = new SuperflatEnchantMent(new NamespacedKey(Superflat.getInstance(), "superflat"));
        Objects.requireNonNull(meta).addEnchant(enchantment, 1,false);
        return meta;
    }
}
