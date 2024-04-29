package synk.functions;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CheckPremium {
    public static Boolean CheckPremium(String name, String uuid) {
        Boolean b = false;
        try {
            URL url = new URL("https://api.mojang.com/users/profiles/minecraft/"+name);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            reader.close();
            conn.disconnect();
            String jsonRep = builder.toString();
            String id = jsonRep.split("\"id\" : \"")[1].split("\"")[0];
            if (id.equalsIgnoreCase(uuid)) {
                b = true;
            } else if (id == null) {
                b = false;
            }
        } catch (IOException ex) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED+"There was an error while checking "+name+"'s premium status.");
        }
        return b;
    }
}
