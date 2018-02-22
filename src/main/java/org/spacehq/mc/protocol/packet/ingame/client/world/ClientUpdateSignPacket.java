// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.protocol.packet.ingame.client.world;

import org.spacehq.packetlib.io.NetOutput;
import java.io.IOException;
import org.spacehq.mc.protocol.util.NetUtil;
import org.spacehq.packetlib.io.NetInput;
import org.spacehq.mc.protocol.data.game.Position;
import org.spacehq.packetlib.packet.Packet;

public class ClientUpdateSignPacket implements Packet
{
    private Position position;
    private String[] lines;
    
    private ClientUpdateSignPacket() {
    }
    
    public ClientUpdateSignPacket(final Position position, final String[] lines) {
        if (lines.length != 4) {
            throw new IllegalArgumentException("Lines must contain exactly 4 strings!");
        }
        this.position = position;
        this.lines = lines;
    }
    
    public Position getPosition() {
        return this.position;
    }
    
    public String[] getLines() {
        return this.lines;
    }
    
    @Override
    public void read(final NetInput in) throws IOException {
        this.position = NetUtil.readPosition(in);
        this.lines = new String[4];
        for (int count = 0; count < this.lines.length; ++count) {
            this.lines[count] = in.readString();
        }
    }
    
    @Override
    public void write(final NetOutput out) throws IOException {
        NetUtil.writePosition(out, this.position);
        for (final String line : this.lines) {
            out.writeString(line);
        }
    }
    
    @Override
    public boolean isPriority() {
        return false;
    }
}
