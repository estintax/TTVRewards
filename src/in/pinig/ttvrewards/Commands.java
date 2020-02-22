package in.pinig.ttvrewards;

import org.bukkit.command.*;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

public class Commands implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!label.equals("ttvrewards")) return true;
        if(args.length > 0) {
            switch(args[0]) {
                case "reload":
                    Plugin plugin = Main.getPlugin(Main.class);
                    plugin.reloadConfig();
                    Main.config = plugin.getConfig();
                    sender.sendMessage(Main.config.getString("strings.prefix") + Main.config.getString("strings.cmd_reloaded"));
                    break;
                case "about":
                    PluginDescriptionFile desc = Main.getPlugin(Main.class).getDescription();
                    sender.sendMessage(Main.config.getString("strings.prefix") + Main.config.getString("strings.cmd_version") + desc.getVersion());
                    sender.sendMessage(Main.config.getString("strings.prefix") + Main.config.getString("strings.cmd_author") + desc.getAuthors());
                    sender.sendMessage(Main.config.getString("strings.prefix") + Main.config.getString("strings.cmd_description") + desc.getDescription());
                    break;
                case "test":
                    if(args.length > 2) {
                        String channel = args[1];
                        String reward = args[2];
                        sender.sendMessage(Main.config.getString("strings.prefix") + Main.config.getString("strings.cmd_test_execute"));
                        new RewardsHandler(channel, "kraken_test_user", reward, "Reward testing").runTask(Main.getPlugin(Main.class));
                    } else {
                        sender.sendMessage(Main.config.getString("strings.prefix") + Main.config.getString("strings.cmd_usage_test_in"));
                    }
                    break;
            }
            return true;
        } else {
            sender.sendMessage(Main.config.getString("strings.prefix") + Main.config.getString("strings.cmd_usage"));
            sender.sendMessage(Main.config.getString("strings.prefix") + Main.config.getString("strings.cmd_usage_reload"));
            sender.sendMessage(Main.config.getString("strings.prefix") + Main.config.getString("strings.cmd_usage_about"));
            sender.sendMessage(Main.config.getString("strings.prefix") + Main.config.getString("strings.cmd_usage_test"));
            return true;
        }
    }
}
