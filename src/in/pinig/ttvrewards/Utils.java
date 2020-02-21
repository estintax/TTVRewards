package in.pinig.ttvrewards;

import com.sun.istack.internal.NotNull;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utils {
    public static void broadcastMessageToAllPlayersWhoCanReadThis(String chan, @NotNull String message) {
        if(chan == null) chan = "none";

        Collection<? extends Player> players = Bukkit.getOnlinePlayers();
        for(Player x: players) {
            String channel = Main.channels.get(x.getName());
            if(channel == null) continue;
            if(channel.equals(chan) || chan.equals("none")) {
                x.sendMessage(message);
            }
        }
    }

    public static void loadChannelsFromConfig() {
        List<String> playerToChannel = Main.config.getStringList("players");
        for (String x: playerToChannel) {
            if(!x.contains(":")) {
                System.err.println("TTVRewards config: line \"" + x + "\" is incorrect");
                continue;
            }

            String[] splitted = x.split(":", 2);
            Main.channels.put(splitted[0], splitted[1]);
        }
    }

    public static Map<String, String> parseTags(@NotNull String raw) {
        Map<String, String> result = new HashMap<>();

        String[] splitted = raw.split(";");
        for (String x : splitted) {
            String[] keyAndValue = x.split("=", 2);
            result.put(keyAndValue[0], keyAndValue.length == 2 ? keyAndValue[1] : null);
        }

        return result;
    }

    public static int getRandomInt(int min, int max) {
        // From https://javarush.ru/groups/posts/1256-generacija-sluchaynogo-chisla-v-zadannom-diapazone
        max -= min;
        return (int) (Math.random() * ++max) + min;
        // ---
    }
}
