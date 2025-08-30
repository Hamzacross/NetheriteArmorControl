package me.netheritearmorcontrol.listeners;

import me.netheritearmorcontrol.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareSmithingEvent;
import org.bukkit.inventory.ItemStack;

public class SmithingListener implements Listener {

    private final Main plugin;

    public SmithingListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onSmithingPrepare(PrepareSmithingEvent event) {
        if (!(event.getView().getPlayer() instanceof Player)) return;

        Player player = (Player) event.getView().getPlayer();
        ItemStack result = event.getResult();
        if (result == null) return;

        if (result.getType().name().startsWith("NETHERITE_")) {
            String materialName = result.getType().name();
            boolean allowed = plugin.getConfig().getBoolean("armor-upgrades." + materialName, true);

            if (!allowed) {
                plugin.addLog(player.getName(), "blockedAttempts");
                event.setResult(null); // Red X
            } else {
                plugin.addLog(player.getName(), "crafted");
            }
        }
    }
}
