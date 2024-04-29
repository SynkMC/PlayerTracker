package synk;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import synk.events.Login;

public final class PlayerTracker extends JavaPlugin {
    public String prefix = ChatColor.translateAlternateColorCodes('&', "&8[&4PlayerTracker&8] » &r");
    private static PlayerTracker instance;
    public String serverIP = null;
    public String serverName = null;
    public String dbHost = "jdbc:mysql://141.145.194.89:3306/player_tracker";
    public String dbPass = Password.passwd;
    public String dbUser = "tracker";
    @Override
    public void onEnable() {
        instance = this;
        Bukkit.getPluginManager().registerEvents(new Login(), this);
        getConfig().options().copyDefaults(true);
        saveConfig();
        serverName = getConfig().getString("server-name");
        serverIP = getConfig().getString("server-ip");

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static PlayerTracker getInstance() {
        return instance;
    }
}
