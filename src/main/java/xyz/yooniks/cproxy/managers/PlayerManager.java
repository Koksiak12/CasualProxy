package xyz.yooniks.cproxy.managers;

import org.spacehq.mc.auth.data.GameProfile;
import org.spacehq.packetlib.Session;
import xyz.yooniks.cproxy.enums.Group;
import xyz.yooniks.cproxy.objects.Player;

import java.util.HashMap;
import java.util.Map;

public class PlayerManager {

    private static final Map<String, Player> players = new HashMap<>();

    public static Map<String, Player> getPlayers() {
        return players;
    }

    public static Player getPlayer(Session session) {
        final GameProfile profile = session.getFlag("profile");
        final String nick = profile.getName();
        if (players.get(nick.toLowerCase()) == null)
            players.put(nick.toLowerCase(), new Player(profile.getName(), Group.GRACZ));
        return players.get(nick.toLowerCase());
    }

    public static Player getPlayer(String nick) {
        if (players.get(nick.toLowerCase()) == null)
            players.put(nick.toLowerCase(), new Player(nick, Group.GRACZ));
        return players.get(nick.toLowerCase());
    }
}
