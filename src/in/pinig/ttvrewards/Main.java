package in.pinig.ttvrewards;

import com.sun.istack.internal.NotNull;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

interface Callback {
        public void call(String channel, String username, String reward, String message);
}

public class Main extends JavaPlugin {
    public static FileConfiguration config;
    public static Map<String, String>channels;
    public static ArrayList<String> joinedChannels;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        config = getConfig();

        this.getServer().getPluginManager().registerEvents(new Events(), this);

        channels = new HashMap<>();
        joinedChannels = new ArrayList<>();

        Utils.loadChannelsFromConfig();
        Callback c = new Callback() {
            @Override
            public void call(String channel, String username, String reward, String message) {
                RewardsHandler.handleReward(channel, username, reward, message);
            }
        };
        Bukkit.getScheduler().runTaskAsynchronously(this, new TMI(c));

        System.err.println("Warning! This is currently experimental plugin");
        System.err.println("Find a bug? Send it to pinigin(at)mapicom.ru");
        System.out.println("Based on ttvmc v1.3.2.1");
    }

    @Override
    public void onDisable() {
        try {
            TMI.sock.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void onRewardReceived(@NotNull String channel, @NotNull String username, @NotNull String reward, String message) {
        RewardsHandler.handleReward(channel, username, reward, message);
    }
}
