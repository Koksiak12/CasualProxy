// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.protocol.packet.ingame.server;

import org.spacehq.packetlib.io.NetOutput;
import java.io.IOException;
import org.spacehq.packetlib.io.NetInput;
import org.spacehq.mc.protocol.data.message.Message;
import org.spacehq.packetlib.packet.Packet;

public class ServerDisconnectPacket implements Packet
{
    private Message message;
    
    private ServerDisconnectPacket() {
    }
    
    public ServerDisconnectPacket(final String text) {
        this(Message.fromString(text));
    }
    
    public ServerDisconnectPacket(final Message message) {
        this.message = message;
    }
    
    public Message getReason() {
        return this.message;
    }
    
    @Override
    public void read(final NetInput in) throws IOException {
        this.message = Message.fromString(in.readString());
    }
    
    @Override
    public void write(final NetOutput out) throws IOException {
        out.writeString(this.message.toJsonString());
    }
    
    @Override
    public boolean isPriority() {
        return true;
    }
}
