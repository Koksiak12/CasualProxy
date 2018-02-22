// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.protocol.packet.ingame.server;

import org.spacehq.packetlib.io.NetOutput;
import java.io.IOException;
import org.spacehq.packetlib.io.NetInput;
import org.spacehq.packetlib.packet.Packet;

public class ServerSetCompressionPacket implements Packet
{
    private int threshold;
    
    private ServerSetCompressionPacket() {
    }
    
    public ServerSetCompressionPacket(final int threshold) {
        this.threshold = threshold;
    }
    
    public int getThreshold() {
        return this.threshold;
    }
    
    @Override
    public void read(final NetInput in) throws IOException {
        this.threshold = in.readVarInt();
    }
    
    @Override
    public void write(final NetOutput out) throws IOException {
        out.writeVarInt(this.threshold);
    }
    
    @Override
    public boolean isPriority() {
        return true;
    }
}
