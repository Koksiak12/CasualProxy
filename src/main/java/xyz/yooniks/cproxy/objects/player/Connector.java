package xyz.yooniks.cproxy.objects.player;

import org.spacehq.packetlib.Session;

public class Connector extends Movement {

    protected Session sessionConnect;
    private String lastPacket;
    private Long lastPacketMs;
    private boolean connected;

    public Long getLastPacketMs() {
        return lastPacketMs;
    }

    public void setLastPacketMs(Long lastPacketMs) {
        this.lastPacketMs = lastPacketMs;
    }

    public String getLastPacket() {
        return lastPacket;
    }

    public void setLastPacket(String lastPacket) {
        this.lastPacket = lastPacket;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public Session getSessionConnect() {
        return sessionConnect;
    }

    public void setSessionConnect(Session sessionConnect) {
        this.sessionConnect = sessionConnect;
    }
}
