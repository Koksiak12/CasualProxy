// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.protocol.packet.ingame.server.entity;

import org.spacehq.packetlib.io.NetOutput;
import java.io.IOException;
import org.spacehq.packetlib.io.NetInput;
import org.spacehq.packetlib.packet.Packet;

public class ServerDestroyEntitiesPacket implements Packet
{
    private int[] entityIds;
    
    private ServerDestroyEntitiesPacket() {
    }
    
    public ServerDestroyEntitiesPacket(final int... entityIds) {
        this.entityIds = entityIds;
    }
    
    public int[] getEntityIds() {
        return this.entityIds;
    }
    
    @Override
    public void read(final NetInput in) throws IOException {
        this.entityIds = new int[in.readVarInt()];
        for (int index = 0; index < this.entityIds.length; ++index) {
            this.entityIds[index] = in.readVarInt();
        }
    }
    
    @Override
    public void write(final NetOutput out) throws IOException {
        out.writeVarInt(this.entityIds.length);
        for (final int entityId : this.entityIds) {
            out.writeVarInt(entityId);
        }
    }
    
    @Override
    public boolean isPriority() {
        return false;
    }
}
