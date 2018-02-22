// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.protocol.packet.ingame.server;

import org.spacehq.packetlib.io.NetOutput;
import java.io.IOException;
import org.spacehq.mc.protocol.data.game.values.MagicValues;
import org.spacehq.packetlib.io.NetInput;
import org.spacehq.mc.protocol.data.game.values.entity.player.CombatState;
import org.spacehq.packetlib.packet.Packet;

public class ServerCombatPacket implements Packet
{
    private CombatState state;
    private int entityId;
    private int duration;
    private int playerId;
    private String message;
    
    public ServerCombatPacket() {
        this.state = CombatState.ENTER_COMBAT;
    }
    
    public ServerCombatPacket(final int entityId, final int duration) {
        this.state = CombatState.END_COMBAT;
        this.entityId = entityId;
        this.duration = duration;
    }
    
    public ServerCombatPacket(final int entityId, final int playerId, final String message) {
        this.state = CombatState.ENTITY_DEAD;
        this.entityId = entityId;
        this.playerId = playerId;
        this.message = message;
    }
    
    public CombatState getCombatState() {
        return this.state;
    }
    
    public int getEntityId() {
        return this.entityId;
    }
    
    public int getDuration() {
        return this.duration;
    }
    
    public int getPlayerId() {
        return this.playerId;
    }
    
    public String getMessage() {
        return this.message;
    }
    
    @Override
    public void read(final NetInput in) throws IOException {
        this.state = MagicValues.key(CombatState.class, in.readVarInt());
        if (this.state == CombatState.END_COMBAT) {
            this.duration = in.readVarInt();
            this.entityId = in.readInt();
        }
        else if (this.state == CombatState.ENTITY_DEAD) {
            this.playerId = in.readVarInt();
            this.entityId = in.readInt();
            this.message = in.readString();
        }
    }
    
    @Override
    public void write(final NetOutput out) throws IOException {
        out.writeVarInt(MagicValues.value(Integer.class, this.state));
        if (this.state == CombatState.END_COMBAT) {
            out.writeVarInt(this.duration);
            out.writeInt(this.entityId);
        }
        else if (this.state == CombatState.ENTITY_DEAD) {
            out.writeVarInt(this.playerId);
            out.writeInt(this.entityId);
            out.writeString(this.message);
        }
    }
    
    @Override
    public boolean isPriority() {
        return false;
    }
}
