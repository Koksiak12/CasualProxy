// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.protocol.packet.ingame.server.entity;

import org.spacehq.packetlib.io.NetOutput;
import java.io.IOException;
import org.spacehq.mc.protocol.data.game.values.MagicValues;
import org.spacehq.packetlib.io.NetInput;
import org.spacehq.mc.protocol.data.game.values.entity.player.Animation;
import org.spacehq.packetlib.packet.Packet;

public class ServerAnimationPacket implements Packet
{
    private int entityId;
    private Animation animation;
    
    private ServerAnimationPacket() {
    }
    
    public ServerAnimationPacket(final int entityId, final Animation animation) {
        this.entityId = entityId;
        this.animation = animation;
    }
    
    public int getEntityId() {
        return this.entityId;
    }
    
    public Animation getAnimation() {
        return this.animation;
    }
    
    @Override
    public void read(final NetInput in) throws IOException {
        this.entityId = in.readVarInt();
        this.animation = MagicValues.key(Animation.class, in.readByte());
    }
    
    @Override
    public void write(final NetOutput out) throws IOException {
        out.writeVarInt(this.entityId);
        out.writeByte(MagicValues.value(Integer.class, this.animation));
    }
    
    @Override
    public boolean isPriority() {
        return false;
    }
}
