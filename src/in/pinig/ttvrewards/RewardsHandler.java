package in.pinig.ttvrewards;

import com.sun.istack.internal.NotNull;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;


import java.util.Collection;

public class RewardsHandler {
    public static void handleReward(@NotNull String channel, @NotNull String username, @NotNull String rewardId) {
        Player player = null;
        Collection<? extends Player> players = Bukkit.getOnlinePlayers();
        for (Player x: players) {
            String chan = Main.channels.get(x.getName());
            if(channel == null) continue;
            if(channel.equals(chan)) player = x;
        }
        if(player == null) return;

        if (Main.config.getString("rewards." + rewardId + ".name", null) != null) {
            String action = Main.config.getString("rewards." + rewardId + ".action", null);
            if(action == null) {
                System.err.println("Reward " + Main.config.getString("rewards." + rewardId + ".name") + " (" + rewardId + ") + exist, but not have action");
                return;
            }
            String actionStr = "";

            switch(action) {
                case "effect":
                    String effectId = Main.config.getString("rewards." + rewardId + ".effect.id", null);
                    int duration = Main.config.getInt("rewards." + rewardId + ".effect.duration", 0);
                    int amplifier = Main.config.getInt("rewards." + rewardId + ".effect.amplifier", 0);
                    if(effectId == null || duration == 0 || amplifier == 0) {
                        System.err.println("Reward " + Main.config.getString("rewards." + rewardId + ".name") + " (" + rewardId + ") + exist and have action, but not have effect options");
                        return;
                    }

                    PotionEffectType effectType = PotionEffectType.getByName(effectId.toUpperCase());
                    if(effectType == null) {
                        System.err.println("Potion effect "+ effectId + " is not exists.");
                        return;
                    }
                    actionStr = effectType.getName();

                    player.addPotionEffect(new PotionEffect(effectType, duration, amplifier));
                    break;
                default:
                    System.err.println("Unknown action \"" + action + "\"");
            }

            player.sendMessage(Main.config.getString("strings.prefix") + Main.config.getString("strings.receive_reward").replace("{name}", username).replace("{reward_name}", Main.config.getString("rewards." + rewardId + ".name")).replace("{action}", actionStr));
        }
    }
}
