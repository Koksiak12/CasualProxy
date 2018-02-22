package xyz.yooniks.cproxy.objects;

import org.spacehq.packetlib.Session;
import xyz.yooniks.cproxy.objects.player.Movement;

public class Bot extends Movement {

    private Player owner;

    private Session session;
    private String name;

    public Bot(Player owner, String name, Session session) {
        this.owner = owner;
        this.name = name;
        this.session = session;
    }

    public String getName() {
        return name;
    }

    public Session getSession() {
        return session;
    }
}
