// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.protocol.packet.ingame.server.entity;

import org.spacehq.packetlib.io.NetOutput;
import java.io.IOException;
import org.spacehq.packetlib.io.NetInput;
import org.spacehq.packetlib.packet.Packet;

public class ServerEntityTeleportPacket implements Packet
{
    private int entityId;
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;
    private boolean onGround;
    
    private ServerEntityTeleportPacket() {
    }
    
    public ServerEntityTeleportPacket(final int entityId, final double x, final double y, final double z, final float yaw, final float pitch, final boolean onGround) {
        this.entityId = entityId;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.onGround = onGround;
    }
    
    public int getEntityId() {
        return this.entityId;
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
    
    public float getYaw() {
        return this.yaw;
    }
    
    public float getPitch() {
        return this.pitch;
    }
    
    public boolean isOnGround() {
        return this.onGround;
    }
    
    @Override
    public void read(final NetInput in) throws IOException {
        this.entityId = in.readVarInt();
        this.x = in.readInt() / 32.0;
        this.y = in.readInt() / 32.0;
        this.z = in.readInt() / 32.0;
        this.yaw = in.readByte() * 360 / 256.0f;
        this.pitch = in.readByte() * 360 / 256.0f;
        this.onGround = in.readBoolean();
    }
    
    @Override
    public void write(final NetOutput out) throws IOException {
        out.writeVarInt(this.entityId);
        out.writeInt((int)(this.x * 32.0));
        out.writeInt((int)(this.y * 32.0));
        out.writeInt((int)(this.z * 32.0));
        out.writeByte((byte)(this.yaw * 256.0f / 360.0f));
        out.writeByte((byte)(this.pitch * 256.0f / 360.0f));
        out.writeBoolean(this.onGround);
    }
    
    @Override
    public boolean isPriority() {
        return false;
    }
}
