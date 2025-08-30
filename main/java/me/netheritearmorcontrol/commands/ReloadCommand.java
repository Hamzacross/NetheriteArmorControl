package me.netheritearmorcontrol.commands;

import me.netheritearmorcontrol.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadCommand implements CommandExecutor {

    private final Main plugin;

    public ReloadCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("nar.reload")) {
            sender.sendMessage("§cYou don't have permission to do that.");
            return true;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            plugin.reloadConfig();
            plugin.enforceRestrictionsOnAllPlayers();
            sender.sendMessage(plugin.color(plugin.getConfig().getString("reload-message")));
        } else {
            sender.sendMessage("§eUsage: /nrw reload");
        }
        return true;
    }
}
