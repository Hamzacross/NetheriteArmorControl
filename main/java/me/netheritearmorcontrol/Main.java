package me.netheritearmorcontrol;

import me.netheritearmorcontrol.commands.ReloadCommand;
import me.netheritearmorcontrol.listeners.ArmorListener;
import me.netheritearmorcontrol.listeners.SmithingListener;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class Main extends JavaPlugin {

    private static Main instance;
    private File logsFile;
    private FileConfiguration logsConfig;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        createLogsFile();

        getServer().getPluginManager().registerEvents(new ArmorListener(this), this);
        getServer().getPluginManager().registerEvents(new SmithingListener(this), this);

        getCommand("nrw").setExecutor(new ReloadCommand(this));

        getLogger().info("NetheriteArmorControl enabled!");
    }

    @Override
    public void onDisable() {
        saveLogs();
        getLogger().info("NetheriteArmorControl disabled!");
    }

    public static Main getInstance() {
        return instance;
    }

    public void enforceRestrictionsOnAllPlayers() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            for (ItemStack armor : player.getInventory().getArmorContents()) {
                if (armor != null && armor.getType().name().startsWith("NETHERITE_")) {
                    String materialName = armor.getType().name();
                    boolean allowed = getConfig().getBoolean("armor-upgrades." + materialName, true);

                    if (!allowed) {
                        player.getInventory().remove(armor);
                        player.sendMessage(color(getConfig().getString("equip-message")));
                        addLog(player.getName(), "blockedAttempts");
                    }
                }
            }
        }
    }

    public String color(String message) {
        return message != null ? message.replace("&", "§") : "";
    }

    // LOGS
    private void createLogsFile() {
        logsFile = new File(getDataFolder(), "logs.yml");
        if (!logsFile.exists()) {
            try {
                logsFile.getParentFile().mkdirs();
                logsFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        logsConfig = YamlConfiguration.loadConfiguration(logsFile);
    }

    public FileConfiguration getLogsConfig() {
        return logsConfig;
    }

    public void saveLogs() {
        try {
            logsConfig.save(logsFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addLog(String playerName, String type) {
        String path = playerName + "." + type;
        int current = logsConfig.getInt(path, 0);
        logsConfig.set(path, current + 1);
        saveLogs();
    }
}
