package in.pinig.ttvrewards;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class Events implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (Main.channels.get(event.getPlayer().getName()) != null) {
            event.getPlayer().sendMessage(Main.config.getString("strings.prefix") + Main.config.getString("strings.connected").replace("{channel}", Main.channels.get(event.getPlayer().getName())));
        }
    }
}
