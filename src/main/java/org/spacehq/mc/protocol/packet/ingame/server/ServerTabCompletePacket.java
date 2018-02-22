// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.protocol.packet.ingame.server;

import org.spacehq.packetlib.io.NetOutput;
import java.io.IOException;
import org.spacehq.packetlib.io.NetInput;
import org.spacehq.packetlib.packet.Packet;

public class ServerTabCompletePacket implements Packet
{
    private String[] matches;
    
    private ServerTabCompletePacket() {
    }
    
    public ServerTabCompletePacket(final String[] matches) {
        this.matches = matches;
    }
    
    public String[] getMatches() {
        return this.matches;
    }
    
    @Override
    public void read(final NetInput in) throws IOException {
        this.matches = new String[in.readVarInt()];
        for (int index = 0; index < this.matches.length; ++index) {
            this.matches[index] = in.readString();
        }
    }
    
    @Override
    public void write(final NetOutput out) throws IOException {
        out.writeVarInt(this.matches.length);
        for (final String match : this.matches) {
            out.writeString(match);
        }
    }
    
    @Override
    public boolean isPriority() {
        return false;
    }
}
