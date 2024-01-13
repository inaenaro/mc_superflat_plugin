package com.inaenaro.superflat.events;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Objects;

import static com.inaenaro.superflat.SuperflatEndPortalFrameMeta.superflatEndPortalFrameMeta;
import static com.inaenaro.superflat.events.Data.saveData;
import static com.inaenaro.superflat.events.Data.getData;

public class Events implements Listener {

    @EventHandler
    public static void onEntitySpawn(EntitySpawnEvent event) {
        if (event.getEntityType() == EntityType.MAGMA_CUBE) {
            if (event.getLocation().getBlock().getBiome() == Biome.BASALT_DELTAS && ((MagmaCube) event.getEntity()).getSize() == 4) {
                double random = Math.random();
                if (random < 0.2) {
                    event.setCancelled(true);
                    Objects.requireNonNull(event.getLocation().getWorld()).spawnEntity(event.getLocation(), EntityType.BLAZE);
                }
            }
            return;
        }
        if (event.getEntityType() == EntityType.SKELETON) {
            if (event.getLocation().getBlock().getBiome() == Biome.SOUL_SAND_VALLEY) {
                double random = Math.random();
                if (random < 0.1) {
                    event.setCancelled(true);
                    Objects.requireNonNull(event.getLocation().getWorld()).spawnEntity(event.getLocation(), EntityType.WITHER_SKELETON);
                }
            }
        }
    }

    @EventHandler
    public static void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity() instanceof Enderman && event.getEntity().getKiller() != null) {
            BlockData blockData = ((Enderman) event.getEntity()).getCarriedBlock();
            if (blockData != null && blockData.equals(Material.END_PORTAL_FRAME.createBlockData())) {
                ItemStack item = new ItemStack(Material.END_PORTAL_FRAME, 1);
                item.setItemMeta(superflatEndPortalFrameMeta);
                event.getDrops().add(item);
            } else {
                World.Environment environment = event.getEntity().getWorld().getEnvironment();
                if (environment.equals(World.Environment.NORMAL) || environment.equals(World.Environment.NETHER)) {
                    double random = Math.random();
                    if (random < 0.01) {
                        Enderman specialEnderman = (Enderman) event.getEntity().getWorld().spawnEntity(event.getEntity().getLocation(), EntityType.ENDERMAN);
                        specialEnderman.setCarriedBlock(Material.END_PORTAL_FRAME.createBlockData());
                        for (Player p : Bukkit.getOnlinePlayers()) {
                            p.sendTitle(ChatColor.RED + "Special Enderman!", "", 10, 70, 20);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public static void onBlockPlace(BlockPlaceEvent event) {
        ItemMeta itemMeta = event.getItemInHand().getItemMeta();
        if (itemMeta != null) {
            if (itemMeta.equals(superflatEndPortalFrameMeta)) {
                if (event.getPlayer().getWorld().getEnvironment() != World.Environment.NORMAL) {
                    event.setCancelled(true);
                    event.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.RED + "You can only place this in the Overworld!"));
                    return;
                }
                Data data = getData();
                if (data != null && data.coordinates != null) {
                    event.setCancelled(true);
                    event.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.RED + "There is already an super end portal frame! (" + data.coordinates.getBlockX() + ", " + data.coordinates.getBlockY() + ", " + data.coordinates.getBlockZ() + ")"));
                    return;
                }
                Location location = event.getBlock().getLocation();
                World world = location.getWorld();
                assert world != null;
                for (int dx = -2; dx <= 2; dx++) {
                    for (int dz = -2; dz <= 2; dz++) {
                        if (dx == 0 && dz == 0) {
                            continue;
                        }
                        Location location_copy = location.clone();
                        Block block = world.getBlockAt(location_copy.add(dx, 0, dz));
                        if (!block.isEmpty()) {
                            event.setCancelled(true);
                            event.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.RED + "You can't place this here!"));
                            return;
                        }
                    }
                }
                setEndPortalFrame(location, -2, -1, BlockFace.EAST);
                setEndPortalFrame(location, -2, 0, BlockFace.EAST);
                setEndPortalFrame(location, -2, 1, BlockFace.EAST);
                setEndPortalFrame(location, -1, -2, BlockFace.SOUTH);
                setEndPortalFrame(location, 0, -2, BlockFace.SOUTH);
                setEndPortalFrame(location, 1, -2, BlockFace.SOUTH);
                setEndPortalFrame(location, -1, 2, BlockFace.NORTH);
                setEndPortalFrame(location, 0, 2, BlockFace.NORTH);
                setEndPortalFrame(location, 1, 2, BlockFace.NORTH);
                setEndPortalFrame(location, 2, -1, BlockFace.WEST);
                setEndPortalFrame(location, 2, 0, BlockFace.WEST);
                setEndPortalFrame(location, 2, 1, BlockFace.WEST);
                setSpawner(location, -2, -2);
                setSpawner(location, -2, 2);
                setSpawner(location, 2, -2);
                setSpawner(location, 2, 2);
                event.getBlock().setType(Material.AIR);
                saveData(event.getBlock().getLocation());
            }
        }
    }

    private static void setEndPortalFrame(Location loc, int dx, int dz, BlockFace facing) {
        Location location = loc.clone();
        World world = location.getWorld();
        assert world != null;
        Block block = world.getBlockAt(location.add(dx, 0, dz));
        block.setType(Material.END_PORTAL_FRAME);
        Directional data = (Directional) block.getBlockData();
        data.setFacing(facing);
        block.setBlockData(data);
    }

    private static void setSpawner(Location loc, int dx, int dz) {
        Location location = loc.clone();
        World world = location.getWorld();
        assert world != null;
        Block block = world.getBlockAt(location.add(dx, 0, dz));
        block.setType(Material.SPAWNER);
        CreatureSpawner blockState = (CreatureSpawner) block.getState();
        blockState.setSpawnedType(EntityType.SILVERFISH);
        blockState.update();
    }

    @EventHandler
    public static void onEntityChangeBlock(EntityChangeBlockEvent event) {
        if (event.getEntityType() == EntityType.ENDERMAN && event.getTo() == Material.END_PORTAL_FRAME) {
            event.setCancelled(true);
        }
    }
}
