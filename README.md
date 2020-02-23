# TTVRewards
Experimental plugin for Bukkit. This use Channel Points rewards to interact with MC world.

Russian translation is available [here](https://pastebin.com/7c0E7F1c)

If you know English and Russian well, I would be happy if you translated DOCS_RU.txt

## How to build
1. Download and install IntelliJIDEA;
2. Clone this repository via `git clone`;
3. Download BuildTools.jar from [here](https://hub.spigotmc.org/jenkins/job/BuildTools/) to empty directory;
4. Build Spigot. See [this](https://www.spigotmc.org/wiki/buildtools) for instructions;
5. Find `spigot-api-1.15.2-R0.1-SNAPSHOT-shaded.jar` in `*BuildTools directory*/Spigot/Spigot-API/target`;
6. Put this file into ttvmc's repository root;
7. Run IntelliJIDEA and open ttvmc project;
8. Just build project using Ctrl+F9 or click on hammer in IDE;
9. Find plugin in `out/artifacts/TTVRewards_jar`.

## How to install
1. Drop .jar to `plugins` on your server;
2. Start or restart the server;
3. Configure plugin in `plugins/TTVRewards/plugin.yml`;
4. Restart the server.
