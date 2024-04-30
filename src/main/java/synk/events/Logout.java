package synk.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import synk.PlayerTracker;
import synk.functions.CheckPremium;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Logout implements Listener {
    PlayerTracker playerTracker = PlayerTracker.getInstance();
    @EventHandler
    public void logoutUpdate(PlayerQuitEvent event) {
        Player p = event.getPlayer();
        try {
            Connection co = DriverManager.getConnection(playerTracker.dbHost, playerTracker.dbUser, playerTracker.dbPass);
            if (CheckPremium.CheckPremium(p.getName(), p.getUniqueId().toString())) {
                StringBuilder uuidBuilder = new StringBuilder();
                String[] sss = p.getUniqueId().toString().split("-");
                for (String ssss : sss) {
                    uuidBuilder.append(ssss);
                }
                String uuid = uuidBuilder.toString();
                String query = "SELECT join_time FROM premium_players WHERE player_id = ?";
                PreparedStatement prep1 = co.prepareStatement(query);
                prep1.setString(1, uuid);
                ResultSet set = prep1.executeQuery();
                if (set != null) {
                    List<Integer> ints = new ArrayList<>();
                    while (set.next()) {
                        ints.add(set.getInt("join_time"));
                    }
                    int large = 0;
                    for (int i : ints) {
                        if (i>large) {
                            large = i;
                        }
                    }
                    String insert = "UPDATE premium_players SET leave_time = ? WHERE join_time = ? AND player_id = ?";
                    PreparedStatement prep = co.prepareStatement(insert);
                    prep.setLong(1, System.currentTimeMillis()/1000);
                    prep.setInt(2, large);
                    prep.setString(3, uuid);
                    prep.executeUpdate();
                }

            } else {
                String query = "SELECT join_time FROM cracked_players WHERE player_name = ?";
                PreparedStatement prep1 = co.prepareStatement(query);
                prep1.setString(1, p.getName());
                ResultSet set = prep1.executeQuery();
                if (set != null) {
                    List<Integer> ints = new ArrayList<>();
                    while (set.next()) {
                        ints.add(set.getInt("join_time"));
                    }
                    int large = 0;
                    for (int i : ints) {
                        if (i>large) {
                            large = i;
                        }
                    }
                    String insert = "UPDATE cracked_players SET leave_time = ? WHERE join_time = ? AND player_name = ?";
                    PreparedStatement prep = co.prepareStatement(insert);
                    prep.setLong(1, System.currentTimeMillis()/1000);
                    prep.setInt(2, large);
                    prep.setString(3, p.getName());
                    prep.executeUpdate();
                }
            }
            co.close();
        } catch (SQLException e) {
            e.printStackTrace();
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "There was an error logging into the database!");
        }
    }
}
