package in.pinig.ttvrewards;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.Map;

public class TMI extends Thread {
    public static Socket sock;

    @Override
    public void run() {
        System.out.println("Initializing TMI...");
        try {
            sock = new Socket("irc.chat.twitch.tv", 6667);
            BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            PrintWriter out = new PrintWriter(sock.getOutputStream(), true);

            // Auth, joining channels and request cap
            out.println("NICK justinfan42");
            out.println("CAP REQ :twitch.tv/tags");
            for (Map.Entry<String, String> e: Main.channels.entrySet()) {
                String channel = e.getValue();
                if(Main.joinedChannels.contains(channel)) continue;
                System.out.println("[ttvmc] Joining to #" + channel + " for " + e.getKey());
                out.println("JOIN #" + channel);
                Main.joinedChannels.add(channel);
            }

            while (!sock.isClosed()) {
                String str = in.readLine();
                if (str == null) continue;

                String[] preParseParams = str.split(" ");
                if (preParseParams[0].equals("PING")) {
                    out.println("PONG " + preParseParams[1]);
                    continue;
                }

                String[] args = str.split(":", 3);
                args[0] = args[0].replace(" ", "");
                str = String.join(":", args);
                String[] params = str.split(" ");
                if (params[1].equals("PRIVMSG")) {
                    String channel = params[2].replace("#", "");
                    String username = args[1].split("!")[0];
                    String message = args[2];

                    Map<String, String> tags = Utils.parseTags(params[0].replace("@", ""));
                    String displayName = Main.config.getBoolean("options.useDisplayName") ? tags.get("display-name") : username;
                    if (tags.get("custom-reward-id") != null) {
                        System.out.println("Found message with reward");
                    }
                }
            }
        }
        catch (NullPointerException ex) {
            System.err.println("Some strings equals null.");
            ex.printStackTrace();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
