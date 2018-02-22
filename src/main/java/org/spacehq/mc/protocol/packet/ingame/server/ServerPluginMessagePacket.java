// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.protocol.packet.ingame.server;

import org.spacehq.packetlib.io.NetOutput;
import java.io.IOException;
import org.spacehq.packetlib.io.NetInput;
import org.spacehq.packetlib.packet.Packet;

public class ServerPluginMessagePacket implements Packet
{
    private String channel;
    private byte[] data;
    
    private ServerPluginMessagePacket() {
    }
    
    public ServerPluginMessagePacket(final String channel, final byte[] data) {
        this.channel = channel;
        this.data = data;
    }
    
    public String getChannel() {
        return this.channel;
    }
    
    public byte[] getData() {
        return this.data;
    }
    
    @Override
    public void read(final NetInput in) throws IOException {
        this.channel = in.readString();
        this.data = in.readBytes(in.available());
    }
    
    @Override
    public void write(final NetOutput out) throws IOException {
        out.writeString(this.channel);
        out.writeBytes(this.data);
    }
    
    @Override
    public boolean isPriority() {
        return false;
    }
}
