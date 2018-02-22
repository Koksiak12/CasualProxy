// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.protocol.packet.ingame.server.entity;

import org.spacehq.packetlib.io.NetOutput;
import java.io.IOException;
import org.spacehq.mc.protocol.data.game.values.MagicValues;
import org.spacehq.packetlib.io.NetInput;
import org.spacehq.mc.protocol.data.game.values.entity.EntityStatus;
import org.spacehq.packetlib.packet.Packet;

public class ServerEntityStatusPacket implements Packet
{
    protected int entityId;
    protected EntityStatus status;
    
    private ServerEntityStatusPacket() {
    }
    
    public ServerEntityStatusPacket(final int entityId, final EntityStatus status) {
        this.entityId = entityId;
        this.status = status;
    }
    
    public int getEntityId() {
        return this.entityId;
    }
    
    public EntityStatus getStatus() {
        return this.status;
    }
    
    @Override
    public void read(final NetInput in) throws IOException {
        this.entityId = in.readInt();
        this.status = MagicValues.key(EntityStatus.class, in.readByte());
    }
    
    @Override
    public void write(final NetOutput out) throws IOException {
        out.writeInt(this.entityId);
        out.writeByte(MagicValues.value(Integer.class, this.status));
    }
    
    @Override
    public boolean isPriority() {
        return false;
    }
}
