// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.protocol.packet.ingame.server.entity;

import org.spacehq.packetlib.io.NetOutput;
import java.io.IOException;
import org.spacehq.mc.protocol.util.NetUtil;
import org.spacehq.packetlib.io.NetInput;
import org.spacehq.mc.protocol.data.game.EntityMetadata;
import org.spacehq.packetlib.packet.Packet;

public class ServerEntityMetadataPacket implements Packet
{
    private int entityId;
    private EntityMetadata[] metadata;
    
    private ServerEntityMetadataPacket() {
    }
    
    public ServerEntityMetadataPacket(final int entityId, final EntityMetadata[] metadata) {
        this.entityId = entityId;
        this.metadata = metadata;
    }
    
    public int getEntityId() {
        return this.entityId;
    }
    
    public EntityMetadata[] getMetadata() {
        return this.metadata;
    }
    
    @Override
    public void read(final NetInput in) throws IOException {
        this.entityId = in.readVarInt();
        this.metadata = NetUtil.readEntityMetadata(in);
    }
    
    @Override
    public void write(final NetOutput out) throws IOException {
        out.writeVarInt(this.entityId);
        NetUtil.writeEntityMetadata(out, this.metadata);
    }
    
    @Override
    public boolean isPriority() {
        return false;
    }
}
