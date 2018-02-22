// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.protocol.packet.ingame.client;

import org.spacehq.packetlib.io.NetOutput;
import java.io.IOException;
import org.spacehq.packetlib.io.NetInput;
import org.spacehq.packetlib.packet.Packet;

public class ClientChatPacket implements Packet
{
    private String message;
    
    private ClientChatPacket() {
    }
    
    public ClientChatPacket(final String message) {
        this.message = message;
    }
    
    public String getMessage() {
        return this.message;
    }
    
    @Override
    public void read(final NetInput in) throws IOException {
        this.message = in.readString();
    }
    
    @Override
    public void write(final NetOutput out) throws IOException {
        out.writeString(this.message);
    }
    
    @Override
    public boolean isPriority() {
        return false;
    }
}
