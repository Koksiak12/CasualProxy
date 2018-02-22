// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.protocol.packet.ingame.server;

import org.spacehq.packetlib.io.NetOutput;
import java.io.IOException;
import org.spacehq.packetlib.io.NetInput;
import org.spacehq.packetlib.packet.Packet;

public class ServerResourcePackSendPacket implements Packet
{
    private String url;
    private String hash;
    
    private ServerResourcePackSendPacket() {
    }
    
    public ServerResourcePackSendPacket(final String url, final String hash) {
        this.url = url;
        this.hash = hash;
    }
    
    public String getUrl() {
        return this.url;
    }
    
    public String getHash() {
        return this.hash;
    }
    
    @Override
    public void read(final NetInput in) throws IOException {
        this.url = in.readString();
        this.hash = in.readString();
    }
    
    @Override
    public void write(final NetOutput out) throws IOException {
        out.writeString(this.url);
        out.writeString(this.hash);
    }
    
    @Override
    public boolean isPriority() {
        return false;
    }
}
