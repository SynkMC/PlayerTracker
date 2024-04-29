package synk.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import synk.PlayerTracker;
import synk.functions.CheckPremium;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Login implements Listener {
    PlayerTracker playerTracker = PlayerTracker.getInstance();
    @EventHandler
    public void loginInsert(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        try {
            Connection co = DriverManager.getConnection(playerTracker.dbHost, playerTracker.dbUser, playerTracker.dbPass);
            if (CheckPremium.CheckPremium(p.getName(), p.getUniqueId().toString())) {
                String insert = "INSERT INTO premium_players (server_name, server_ip, player_name, player_id, join_time, leave_time) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement prep = co.prepareStatement(insert);
                prep.setString(1, playerTracker.serverName);
                prep.setString(2, playerTracker.serverIP);
                prep.setString(3, p.getName());
                prep.setString(4, p.getUniqueId().toString());
                prep.setInt(5, (int) System.currentTimeMillis()/1000);
                prep.setInt(6, 0);
                prep.executeUpdate();
            } else {
                String insert = "INSERT INTO cracked_players (server_name, server_ip, player_name, join_time, leave_time) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement prep = co.prepareStatement(insert);
                prep.setString(1, playerTracker.serverName);
                prep.setString(2, playerTracker.serverIP);
                prep.setString(3, p.getName());
                prep.setInt(4, (int) System.currentTimeMillis()/1000);
                prep.setInt(5, 0);
                prep.executeUpdate();
            }
            co.close();
        } catch (SQLException e) {
            e.printStackTrace();
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "There was an error logging into the database!");
        }
    }

    @EventHandler
    public void noConfig(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        if (playerTracker.serverIP.equalsIgnoreCase("default") || playerTracker.serverName.equalsIgnoreCase("default")) {
            if (p.hasPermission("playertracker.admin")) {
                p.sendMessage(playerTracker.prefix+ChatColor.RED+"The plugin has not been setup yet! Please change the values in config.yml.");
            }
        }
    }
}
