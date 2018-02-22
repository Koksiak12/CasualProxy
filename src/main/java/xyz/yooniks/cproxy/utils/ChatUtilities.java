package xyz.yooniks.cproxy.utils;

import org.spacehq.mc.protocol.packet.ingame.server.ServerChatPacket;
import org.spacehq.packetlib.Session;
import xyz.yooniks.cproxy.CasualProxy;

public class ChatUtilities {

    public static String fixColor(String text) {
        return text.replace("&", "§").replace(">>","»")
                .replace("$p", "§8[§2Casual§aProxy§8]");
    }

    public static void broadcast(String text) {
        for (Session s : CasualProxy.getPlayers())
            s.send(new ServerChatPacket(fixColor(text)));
    }
}
