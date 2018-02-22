package xyz.yooniks.cproxy.objects.player;

import org.spacehq.mc.protocol.data.game.Position;

public class Movement {

    private Position position;
    private double yaw, pitch;

    public double getPitch() {
        return pitch;
    }

    public void setPitch(double pitch) {
        this.pitch = pitch;
    }

    public double getYaw() {
        return yaw;
    }

    public void setYaw(double yaw) {
        this.yaw = yaw;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
}
