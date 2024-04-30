package synk.functions;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CheckPremium {
    public static Boolean CheckPremium(String name, String uuid) {
        Boolean b = false;
        try {
            URL url = new URL("https://api.mojang.com/users/profiles/minecraft/"+name);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            List<String> rep = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                rep.add(line);
            }
            reader.close();
            conn.disconnect();
            String s = rep.get(1);
            String id = null;
            if (s.contains("\"id\"")) {
                String[] ss;
                ss = s.split("\"");
                id = ss[3];
                Bukkit.getConsoleSender().sendMessage("Id found: "+s+ ", "+id);
            }
            String[] ss = uuid.split("-");
            StringBuilder builder = new StringBuilder();
            for (String st : ss) {
                builder.append(st);
            }
            uuid = builder.toString();
            if (id.equalsIgnoreCase(uuid)) {
                b = true;
            }
        } catch (IOException ex) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED+"There was an error while checking "+name+"'s premium status.");
        }
        return b;
    }
}
