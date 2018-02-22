// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.protocol.packet.ingame.server.scoreboard;

import org.spacehq.packetlib.io.NetOutput;
import java.io.IOException;
import org.spacehq.mc.protocol.data.game.values.MagicValues;
import org.spacehq.packetlib.io.NetInput;
import org.spacehq.mc.protocol.data.game.values.scoreboard.ScoreType;
import org.spacehq.mc.protocol.data.game.values.scoreboard.ObjectiveAction;
import org.spacehq.packetlib.packet.Packet;

public class ServerScoreboardObjectivePacket implements Packet
{
    private String name;
    private ObjectiveAction action;
    private String displayName;
    private ScoreType type;
    
    private ServerScoreboardObjectivePacket() {
    }
    
    public ServerScoreboardObjectivePacket(final String name) {
        this.name = name;
        this.action = ObjectiveAction.REMOVE;
    }
    
    public ServerScoreboardObjectivePacket(final String name, final ObjectiveAction action, final String displayName, final ScoreType type) {
        if (action != ObjectiveAction.ADD && action != ObjectiveAction.UPDATE) {
            throw new IllegalArgumentException("(name, action, displayName) constructor only valid for adding and updating objectives.");
        }
        this.name = name;
        this.action = action;
        this.displayName = displayName;
        this.type = type;
    }
    
    public String getName() {
        return this.name;
    }
    
    public ObjectiveAction getAction() {
        return this.action;
    }
    
    public String getDisplayName() {
        return this.displayName;
    }
    
    public ScoreType getType() {
        return this.type;
    }
    
    @Override
    public void read(final NetInput in) throws IOException {
        this.name = in.readString();
        this.action = MagicValues.key(ObjectiveAction.class, in.readByte());
        if (this.action == ObjectiveAction.ADD || this.action == ObjectiveAction.UPDATE) {
            this.displayName = in.readString();
            this.type = MagicValues.key(ScoreType.class, in.readString());
        }
    }
    
    @Override
    public void write(final NetOutput out) throws IOException {
        out.writeString(this.name);
        out.writeByte(MagicValues.value(Integer.class, this.action));
        if (this.action == ObjectiveAction.ADD || this.action == ObjectiveAction.UPDATE) {
            out.writeString(this.displayName);
            out.writeString(MagicValues.value(String.class, this.type));
        }
    }
    
    @Override
    public boolean isPriority() {
        return false;
    }
}
