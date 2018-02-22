// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.protocol.packet.ingame.server.entity;

import org.spacehq.packetlib.io.NetOutput;
import java.io.IOException;
import org.spacehq.mc.protocol.data.game.values.MagicValues;
import org.spacehq.packetlib.io.NetInput;
import org.spacehq.mc.protocol.data.game.values.entity.Effect;
import org.spacehq.packetlib.packet.Packet;

public class ServerEntityRemoveEffectPacket implements Packet
{
    private int entityId;
    private Effect effect;
    
    private ServerEntityRemoveEffectPacket() {
    }
    
    public ServerEntityRemoveEffectPacket(final int entityId, final Effect effect) {
        this.entityId = entityId;
        this.effect = effect;
    }
    
    public int getEntityId() {
        return this.entityId;
    }
    
    public Effect getEffect() {
        return this.effect;
    }
    
    @Override
    public void read(final NetInput in) throws IOException {
        this.entityId = in.readVarInt();
        this.effect = MagicValues.key(Effect.class, in.readByte());
    }
    
    @Override
    public void write(final NetOutput out) throws IOException {
        out.writeVarInt(this.entityId);
        out.writeByte(MagicValues.value(Integer.class, this.effect));
    }
    
    @Override
    public boolean isPriority() {
        return false;
    }
}
