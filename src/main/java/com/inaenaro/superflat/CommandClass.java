package com.inaenaro.superflat;

import com.inaenaro.superflat.events.Data;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.inaenaro.superflat.SuperflatEndPortalFrameMeta.superflatEndPortalFrameMeta;
import static com.inaenaro.superflat.events.Data.getData;
import static com.inaenaro.superflat.events.Data.saveData;

public class CommandClass implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("end-portal")) {
            if (!(sender instanceof Player)) {
                return false;
            }
            Player player = (Player) sender;
            if (args.length == 0) {
                return false;
            }
            if (args[0].equalsIgnoreCase("give")) {
                ItemStack item = new ItemStack(Material.END_PORTAL_FRAME, 1);
                item.setItemMeta(superflatEndPortalFrameMeta);
                player.getInventory().addItem(item);
                player.sendMessage("Gave 1 [" + ChatColor.GREEN + "Super End Portal Frame" + ChatColor.WHITE + "] to " + player.getName());
                return true;
            } else if (args[0].equalsIgnoreCase("locate")) {
                Data data = getData();
                if (data == null || data.coordinates == null) {
                    player.sendMessage("There are no super end portal frames");
                    return true;
                }
                sender.sendMessage("The super end portal frame is at " + ChatColor.GREEN + "[" + data.coordinates.getBlockX() + ", " + data.coordinates.getBlockY() + ", " + data.coordinates.getBlockZ() + "]");
                return true;
            } else if (args[0].equalsIgnoreCase("reset")) {
                Data data = getData();
                if (data == null || data.coordinates == null) {
                    player.sendMessage("There are no super end portal frames");
                    return true;
                }
                saveData(null);
                sender.sendMessage("Reset coordinates of the super end portal frame");
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (command.getName().equalsIgnoreCase("end-portal")) {
            if (args.length == 1) {
                List<String> strings = new ArrayList<>(Arrays.asList("give", "locate", "reset"));
                strings.removeIf(s -> !s.startsWith(args[0]));
                return strings;
            }
        }
        return null;
    }
}
