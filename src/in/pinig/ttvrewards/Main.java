package in.pinig.ttvrewards;

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
    public static Map<String, Boolean> cooldown;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        config = getConfig();

        this.getServer().getPluginManager().registerEvents(new Events(), this);
        this.getCommand("ttvrewards").setExecutor(new Commands());

        channels = new HashMap<>();
        joinedChannels = new ArrayList<>();
        cooldown = new HashMap<>();

        Utils.loadChannelsFromConfig();
        Callback c = new Callback() {
            @Override
            public void call(String channel, String username, String reward, String message) {
                new RewardsHandler(channel, username, reward, message).runTask(Main.this);
            }
        };
        Bukkit.getScheduler().runTaskAsynchronously(this, new TMI(c));

        System.out.println("[TTVRewards] Copyright (c) 2020, Maksim Pinigin");
        System.err.println("[TTVRewards] Warning! This is currently experimental plugin");
        System.err.println("[TTVRewards] Find a bug? Send it to pinigin(at)mapicom.ru");
        System.out.println("[TTVRewards] Based on ttvmc v1.3.2.3");
    }

    @Override
    public void onDisable() {
        try {
            TMI.sock.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
