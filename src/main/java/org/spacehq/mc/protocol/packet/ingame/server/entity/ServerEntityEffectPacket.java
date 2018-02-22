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

public class ServerEntityEffectPacket implements Packet
{
    private int entityId;
    private Effect effect;
    private int amplifier;
    private int duration;
    private boolean hideParticles;
    
    private ServerEntityEffectPacket() {
    }
    
    public ServerEntityEffectPacket(final int entityId, final Effect effect, final int amplifier, final int duration, final boolean hideParticles) {
        this.entityId = entityId;
        this.effect = effect;
        this.amplifier = amplifier;
        this.duration = duration;
        this.hideParticles = hideParticles;
    }
    
    public int getEntityId() {
        return this.entityId;
    }
    
    public Effect getEffect() {
        return this.effect;
    }
    
    public int getAmplifier() {
        return this.amplifier;
    }
    
    public int getDuration() {
        return this.duration;
    }
    
    public boolean getHideParticles() {
        return this.hideParticles;
    }
    
    @Override
    public void read(final NetInput in) throws IOException {
        this.entityId = in.readVarInt();
        this.effect = MagicValues.key(Effect.class, in.readByte());
        this.amplifier = in.readByte();
        this.duration = in.readVarInt();
        this.hideParticles = in.readBoolean();
    }
    
    @Override
    public void write(final NetOutput out) throws IOException {
        out.writeVarInt(this.entityId);
        out.writeByte(MagicValues.value(Integer.class, this.effect));
        out.writeByte(this.amplifier);
        out.writeVarInt(this.duration);
        out.writeBoolean(this.hideParticles);
    }
    
    @Override
    public boolean isPriority() {
        return false;
    }
}
