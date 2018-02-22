// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.protocol.packet.ingame.server.entity.spawn;

import org.spacehq.packetlib.io.NetOutput;
import java.io.IOException;
import org.spacehq.mc.protocol.data.game.values.MagicValues;
import org.spacehq.packetlib.io.NetInput;
import org.spacehq.mc.protocol.data.game.values.entity.GlobalEntityType;
import org.spacehq.packetlib.packet.Packet;

public class ServerSpawnGlobalEntityPacket implements Packet
{
    private int entityId;
    private GlobalEntityType type;
    private int x;
    private int y;
    private int z;
    
    private ServerSpawnGlobalEntityPacket() {
    }
    
    public ServerSpawnGlobalEntityPacket(final int entityId, final GlobalEntityType type, final int x, final int y, final int z) {
        this.entityId = entityId;
        this.type = type;
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public int getEntityId() {
        return this.entityId;
    }
    
    public GlobalEntityType getType() {
        return this.type;
    }
    
    public int getX() {
        return this.x;
    }
    
    public int getY() {
        return this.y;
    }
    
    public int getZ() {
        return this.z;
    }
    
    @Override
    public void read(final NetInput in) throws IOException {
        this.entityId = in.readVarInt();
        this.type = MagicValues.key(GlobalEntityType.class, in.readByte());
        this.x = in.readInt();
        this.y = in.readInt();
        this.z = in.readInt();
    }
    
    @Override
    public void write(final NetOutput out) throws IOException {
        out.writeVarInt(this.entityId);
        out.writeByte(MagicValues.value(Integer.class, this.type));
        out.writeInt(this.x);
        out.writeInt(this.y);
        out.writeInt(this.z);
    }
    
    @Override
    public boolean isPriority() {
        return false;
    }
}
