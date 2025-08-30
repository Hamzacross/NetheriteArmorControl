package me.netheritearmorcontrol.listeners;

import me.netheritearmorcontrol.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

public class ArmorListener implements Listener {

    private final Main plugin;

    public ArmorListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onArmorEquip(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;

        Player player = (Player) event.getWhoClicked();
        ItemStack current = event.getCurrentItem();
        ItemStack cursor = event.getCursor();

        ItemStack checkItem = cursor != null && cursor.getType().name().contains("NETHERITE") ? cursor : current;

        if (checkItem != null && checkItem.getType().name().startsWith("NETHERITE_")) {
            String materialName = checkItem.getType().name();
            boolean allowed = plugin.getConfig().getBoolean("armor-upgrades." + materialName, true);

            if (!allowed) {
                event.setCancelled(true);
                player.sendMessage(plugin.color(plugin.getConfig().getString("equip-message")));
                plugin.addLog(player.getName(), "blockedAttempts");
            }
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        for (ItemStack armor : player.getInventory().getArmorContents()) {
            if (armor != null && armor.getType().name().startsWith("NETHERITE_")) {
                String materialName = armor.getType().name();
                boolean allowed = plugin.getConfig().getBoolean("armor-upgrades." + materialName, true);

                if (!allowed) {
                    player.getInventory().remove(armor);
                    player.sendMessage(plugin.color(plugin.getConfig().getString("equip-message")));
                    plugin.addLog(player.getName(), "blockedAttempts");
                }
            }
        }
    }
}
