// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.protocol.packet.ingame.server.entity.player;

import org.spacehq.packetlib.io.NetOutput;
import java.io.IOException;
import org.spacehq.mc.protocol.util.NetUtil;
import org.spacehq.packetlib.io.NetInput;
import org.spacehq.mc.protocol.data.game.Position;
import org.spacehq.packetlib.packet.Packet;

public class ServerPlayerUseBedPacket implements Packet
{
    private int entityId;
    private Position position;
    
    private ServerPlayerUseBedPacket() {
    }
    
    public ServerPlayerUseBedPacket(final int entityId, final Position position) {
        this.entityId = entityId;
        this.position = position;
    }
    
    public int getEntityId() {
        return this.entityId;
    }
    
    public Position getPosition() {
        return this.position;
    }
    
    @Override
    public void read(final NetInput in) throws IOException {
        this.entityId = in.readVarInt();
        this.position = NetUtil.readPosition(in);
    }
    
    @Override
    public void write(final NetOutput out) throws IOException {
        out.writeVarInt(this.entityId);
        NetUtil.writePosition(out, this.position);
    }
    
    @Override
    public boolean isPriority() {
        return false;
    }
}
