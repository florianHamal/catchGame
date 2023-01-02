package at.flori4n.catchgame.main;

import at.flori4n.catchgame.data.DatabaseHandler;
import at.flori4n.catchgame.listeners.PlayerDamagedListener;
import at.flori4n.catchgame.listeners.PlayerJoinListener;
import at.flori4n.catchgame.listeners.PlayerQuitListener;
import at.flori4n.catchgame.gameManager.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    private static Main plugin;
    GameManager gameManager = new GameManager();
    @Override
    public void onEnable(){
        plugin=this;
        PluginManager pluginManager =  Bukkit.getPluginManager();
        pluginManager.registerEvents(new PlayerQuitListener(gameManager),this);
        pluginManager.registerEvents(new PlayerJoinListener(gameManager),plugin);
        pluginManager.registerEvents(new PlayerDamagedListener(gameManager),this);
        saveDefaultConfig();
        DatabaseHandler.getInstance().setupConnection(
                getConfig().getString("ip"),
                getConfig().getInt("port"),
                getConfig().getString("user"),
                getConfig().getString("password"),
                getConfig().getString("database"),
                getConfig().getString("tableName"));
    }
    public static Main getPlugin(){
        return plugin;
    }
}
