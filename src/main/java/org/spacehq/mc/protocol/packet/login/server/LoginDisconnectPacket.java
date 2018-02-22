// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.protocol.packet.login.server;

import org.spacehq.packetlib.io.NetOutput;
import java.io.IOException;
import org.spacehq.packetlib.io.NetInput;
import org.spacehq.mc.protocol.data.message.Message;
import org.spacehq.packetlib.packet.Packet;

public class LoginDisconnectPacket implements Packet
{
    private Message message;
    
    private LoginDisconnectPacket() {
    }
    
    public LoginDisconnectPacket(final String text) {
        this(Message.fromString(text));
    }
    
    public LoginDisconnectPacket(final Message message) {
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
