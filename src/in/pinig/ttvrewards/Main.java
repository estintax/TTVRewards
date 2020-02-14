package in.pinig.ttvrewards;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    public static FileConfiguration config;
    @Override
    public void onEnable() {
        saveDefaultConfig();
        config = getConfig();

        System.err.println("Warning! This is currently experimental plugin");
        System.err.println("Find a bug? Send it to pinigin(at)mapicom.ru");
        System.out.println("Based on ttvmc v1.3.2");
    }
}
