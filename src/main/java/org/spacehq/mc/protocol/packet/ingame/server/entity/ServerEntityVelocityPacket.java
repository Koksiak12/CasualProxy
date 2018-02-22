// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.protocol.packet.ingame.server.entity;

import org.spacehq.packetlib.io.NetOutput;
import java.io.IOException;
import org.spacehq.packetlib.io.NetInput;
import org.spacehq.packetlib.packet.Packet;

public class ServerEntityVelocityPacket implements Packet
{
    private int entityId;
    private double motX;
    private double motY;
    private double motZ;
    
    private ServerEntityVelocityPacket() {
    }
    
    public ServerEntityVelocityPacket(final int entityId, final double motX, final double motY, final double motZ) {
        this.entityId = entityId;
        this.motX = motX;
        this.motY = motY;
        this.motZ = motZ;
    }
    
    public int getEntityId() {
        return this.entityId;
    }
    
    public double getMotionX() {
        return this.motX;
    }
    
    public double getMotionY() {
        return this.motY;
    }
    
    public double getMotionZ() {
        return this.motZ;
    }
    
    @Override
    public void read(final NetInput in) throws IOException {
        this.entityId = in.readVarInt();
        this.motX = in.readShort() / 8000.0;
        this.motY = in.readShort() / 8000.0;
        this.motZ = in.readShort() / 8000.0;
    }
    
    @Override
    public void write(final NetOutput out) throws IOException {
        out.writeVarInt(this.entityId);
        out.writeShort((int)(this.motX * 8000.0));
        out.writeShort((int)(this.motY * 8000.0));
        out.writeShort((int)(this.motZ * 8000.0));
    }
    
    @Override
    public boolean isPriority() {
        return false;
    }
}
