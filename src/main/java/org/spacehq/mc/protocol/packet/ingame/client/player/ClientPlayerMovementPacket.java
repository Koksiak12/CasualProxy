// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.protocol.packet.ingame.client.player;

import org.spacehq.packetlib.io.NetOutput;
import java.io.IOException;
import org.spacehq.packetlib.io.NetInput;
import org.spacehq.packetlib.packet.Packet;

public class ClientPlayerMovementPacket implements Packet
{
    protected double x;
    protected double y;
    protected double z;
    protected float yaw;
    protected float pitch;
    protected boolean onGround;
    protected boolean pos;
    protected boolean rot;
    
    protected ClientPlayerMovementPacket() {
        this.pos = false;
        this.rot = false;
    }
    
    public ClientPlayerMovementPacket(final boolean onGround) {
        this.pos = false;
        this.rot = false;
        this.onGround = onGround;
    }
    
    public double getX() {
        return this.x;
    }
    
    public double getY() {
        return this.y;
    }
    
    public double getZ() {
        return this.z;
    }
    
    public double getYaw() {
        return this.yaw;
    }
    
    public double getPitch() {
        return this.pitch;
    }
    
    public boolean isOnGround() {
        return this.onGround;
    }
    
    @Override
    public void read(final NetInput in) throws IOException {
        if (this.pos) {
            this.x = in.readDouble();
            this.y = in.readDouble();
            this.z = in.readDouble();
        }
        if (this.rot) {
            this.yaw = in.readFloat();
            this.pitch = in.readFloat();
        }
        this.onGround = in.readBoolean();
    }
    
    @Override
    public void write(final NetOutput out) throws IOException {
        if (this.pos) {
            out.writeDouble(this.x);
            out.writeDouble(this.y);
            out.writeDouble(this.z);
        }
        if (this.rot) {
            out.writeFloat(this.yaw);
            out.writeFloat(this.pitch);
        }
        out.writeBoolean(this.onGround);
    }
    
    @Override
    public boolean isPriority() {
        return false;
    }
}
