package in.pinig.ttvrewards;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.Map;

public class TMI implements Runnable {
    public static Socket sock;
    private static BufferedReader in;
    private static PrintWriter out;

    Callback call;
    TMI(Callback c) {
        this.call = c;
    }

    public void run() {
        System.out.println("Initializing TMI...");
        try {
            sock = new Socket("irc.chat.twitch.tv", 6667);
            in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            out = new PrintWriter(sock.getOutputStream(), true);

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
                String rawTags = str.split(" :")[0];
                str = str.replace(rawTags, "");

                String[] preParseParams = str.split(" ");
                if (preParseParams[0].equals("PING")) {
                    out.println("PONG " + preParseParams[1]);
                    continue;
                }

                String[] args = str.split(":", 3);
                args[0] = args[0].replace(" ", "");
                str = String.join(":", args);
                String[] params = str.split(" ");
                if (params.length > 2 && params[1].equals("PRIVMSG")) {
                    String channel = params[2].replace("#", "");
                    String username = args[1].split("!")[0];
                    String message = args[2];

                    Map<String, String> tags = Utils.parseTags(rawTags.replace("@", ""));
                    String displayName = Main.config.getBoolean("options.useDisplayName") ? tags.get("display-name") : username;
                    if (tags.get("custom-reward-id") != null) {
                        System.out.println("Found message with reward");
                        this.call.call(channel, displayName, tags.get("custom-reward-id"), message);
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
        finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            out.close();
        }
    }
}
