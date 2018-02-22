// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.protocol.packet.ingame.client;

import org.spacehq.packetlib.io.NetOutput;
import java.io.IOException;
import org.spacehq.packetlib.io.NetInput;
import org.spacehq.packetlib.packet.Packet;

public class ClientKeepAlivePacket implements Packet
{
    private int id;
    
    private ClientKeepAlivePacket() {
    }
    
    public ClientKeepAlivePacket(final int id) {
        this.id = id;
    }
    
    public int getPingId() {
        return this.id;
    }
    
    @Override
    public void read(final NetInput in) throws IOException {
        this.id = in.readVarInt();
    }
    
    @Override
    public void write(final NetOutput out) throws IOException {
        out.writeVarInt(this.id);
    }
    
    @Override
    public boolean isPriority() {
        return false;
    }
}
