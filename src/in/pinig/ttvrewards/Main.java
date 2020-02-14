package in.pinig.ttvrewards;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
        Thread thread = new TMI();
        thread.start();

        System.err.println("Warning! This is currently experimental plugin");
        System.err.println("Find a bug? Send it to pinigin(at)mapicom.ru");
        System.out.println("Based on ttvmc v1.3.2");
    }
}
