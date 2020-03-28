package in.pinig.ttvrewards;

/*
    This class contains function for adding new strings to config.yml if plugin was updated
 */

public class Updater {
    public static void updateStrings() {
        int changed = 0;
        if(Main.config.getString("strings.err_unknownsound", null) == null) {
            Main.config.set("strings.err_unknownsound", "§c[Error]§f Sound §3{sound_name}§f does not exists");
            changed++;
        }

        if(changed > 0) {
            Main.getPlugin(Main.class).saveConfig();
            System.out.println("[TTVRewards] Added " + changed + " new string to config.yml");
        }
    }
}
