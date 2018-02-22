// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.protocol.packet.ingame.server.world;

import org.spacehq.packetlib.io.NetOutput;
import java.io.IOException;
import org.spacehq.packetlib.io.NetInput;
import org.spacehq.packetlib.packet.Packet;

public class ServerUpdateTimePacket implements Packet
{
    private long age;
    private long time;
    
    private ServerUpdateTimePacket() {
    }
    
    public ServerUpdateTimePacket(final long age, final long time) {
        this.age = age;
        this.time = time;
    }
    
    public long getWorldAge() {
        return this.age;
    }
    
    public long getTime() {
        return this.time;
    }
    
    @Override
    public void read(final NetInput in) throws IOException {
        this.age = in.readLong();
        this.time = in.readLong();
    }
    
    @Override
    public void write(final NetOutput out) throws IOException {
        out.writeLong(this.age);
        out.writeLong(this.time);
    }
    
    @Override
    public boolean isPriority() {
        return false;
    }
}
