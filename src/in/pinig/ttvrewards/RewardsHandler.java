package in.pinig.ttvrewards;

import com.sun.istack.internal.NotNull;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;


import java.util.Collection;

public class RewardsHandler extends BukkitRunnable {
    String username;
    String channel;
    String rewardId;
    String message;

    RewardsHandler(@NotNull String channel, @NotNull String username, @NotNull String rewardId, String message) {
        this.username = username;
        this.channel = channel;
        this.rewardId = rewardId;
        this.message = message;
    }

    public void run() {
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

                    player.addPotionEffect(new PotionEffect(effectType, duration, amplifier));
                    break;
                case "kill":
                    player.setHealth(0.0);
                    break;
                case "spawn":
                    String mobName = Main.config.getString("rewards." + rewardId + ".spawn.mob", null);
                    if(mobName == null) {
                        player.sendMessage(Main.config.getString("strings.prefix") + Main.config.getString("strings.err_missarg").replace("{reward_name}", Main.config.getString("rewards." + rewardId + ".name")));
                        return;
                    }

                    try {
                        EntityType entityType = EntityType.valueOf(mobName.toUpperCase());

                        Location playerLoc = player.getLocation();
                        playerLoc.setY(playerLoc.getY()+1.0);
                        playerLoc.setZ(playerLoc.getX()+2.0);
                        player.getWorld().spawnEntity(playerLoc, entityType);
                    } catch(IllegalArgumentException ex) {
                        player.sendMessage(Main.config.getString("strings.prefix") + Main.config.getString("strings.err_unknownmob").replace("{mob_name}", mobName));
                        ex.printStackTrace();
                    }
                    break;
                case "jump":
                    int min = Main.config.getInt("rewards." + rewardId + ".jump.min", -1);
                    int max = Main.config.getInt("rewards." + rewardId + ".jump.max", -1);
                    if(min == -1 || max == -1) {
                        player.sendMessage(Main.config.getString("strings.prefix") + Main.config.getString("strings.err_missarg").replace("{reward_name}", Main.config.getString("rewards." + rewardId + ".name")));
                        return;
                    }

                    Location location = player.getLocation();
                    // From https://javarush.ru/groups/posts/1256-generacija-sluchaynogo-chisla-v-zadannom-diapazone
                    max -= min;
                    int randomed = (int) (Math.random() * ++max) + min;
                    // ---

                    location.setY(location.getY()+randomed);
                    player.teleport(location);
                    break;
                default:
                    System.err.println("Unknown action \"" + action + "\"");
                    return;
            }

            player.sendMessage(Main.config.getString("strings.prefix") + Main.config.getString("strings.receive_reward").replace("{name}", username).replace("{reward_name}", Main.config.getString("rewards." + rewardId + ".name")));
            if(message != null)
                player.sendMessage(Main.config.getString("strings.prefix") + Main.config.getString("strings.receive_message").replace("{message}", message));
        }
    }
}
