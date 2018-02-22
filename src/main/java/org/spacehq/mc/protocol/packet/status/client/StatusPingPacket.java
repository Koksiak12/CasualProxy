// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.protocol.packet.status.client;

import org.spacehq.packetlib.io.NetOutput;
import java.io.IOException;
import org.spacehq.packetlib.io.NetInput;
import org.spacehq.packetlib.packet.Packet;

public class StatusPingPacket implements Packet
{
    private long time;
    
    private StatusPingPacket() {
    }
    
    public StatusPingPacket(final long time) {
        this.time = time;
    }
    
    public long getPingTime() {
        return this.time;
    }
    
    @Override
    public void read(final NetInput in) throws IOException {
        this.time = in.readLong();
    }
    
    @Override
    public void write(final NetOutput out) throws IOException {
        out.writeLong(this.time);
    }
    
    @Override
    public boolean isPriority() {
        return false;
    }
}
