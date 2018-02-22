// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.protocol.packet.ingame.server;

import org.spacehq.packetlib.io.NetOutput;
import java.io.IOException;
import org.spacehq.mc.protocol.data.game.values.MagicValues;
import org.spacehq.packetlib.io.NetInput;
import org.spacehq.mc.protocol.data.game.values.setting.Difficulty;
import org.spacehq.packetlib.packet.Packet;

public class ServerDifficultyPacket implements Packet
{
    private Difficulty difficulty;
    
    private ServerDifficultyPacket() {
    }
    
    public ServerDifficultyPacket(final Difficulty difficulty) {
        this.difficulty = difficulty;
    }
    
    public Difficulty getDifficulty() {
        return this.difficulty;
    }
    
    @Override
    public void read(final NetInput in) throws IOException {
        this.difficulty = MagicValues.key(Difficulty.class, in.readUnsignedByte());
    }
    
    @Override
    public void write(final NetOutput out) throws IOException {
        out.writeByte(MagicValues.value(Integer.class, this.difficulty));
    }
    
    @Override
    public boolean isPriority() {
        return false;
    }
}
