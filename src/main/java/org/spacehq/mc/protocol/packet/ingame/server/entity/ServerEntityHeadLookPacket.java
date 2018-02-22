// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.protocol.packet.ingame.server.entity;

import org.spacehq.packetlib.io.NetOutput;
import java.io.IOException;
import org.spacehq.packetlib.io.NetInput;
import org.spacehq.packetlib.packet.Packet;

public class ServerEntityHeadLookPacket implements Packet
{
    private int entityId;
    private float headYaw;
    
    private ServerEntityHeadLookPacket() {
    }
    
    public ServerEntityHeadLookPacket(final int entityId, final float headYaw) {
        this.entityId = entityId;
        this.headYaw = headYaw;
    }
    
    public int getEntityId() {
        return this.entityId;
    }
    
    public float getHeadYaw() {
        return this.headYaw;
    }
    
    @Override
    public void read(final NetInput in) throws IOException {
        this.entityId = in.readVarInt();
        this.headYaw = in.readByte() * 360 / 256.0f;
    }
    
    @Override
    public void write(final NetOutput out) throws IOException {
        out.writeVarInt(this.entityId);
        out.writeByte((byte)(this.headYaw * 256.0f / 360.0f));
    }
    
    @Override
    public boolean isPriority() {
        return false;
    }
}
