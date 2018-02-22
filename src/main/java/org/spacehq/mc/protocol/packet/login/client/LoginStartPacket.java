// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.protocol.packet.login.client;

import org.spacehq.packetlib.io.NetOutput;
import java.io.IOException;
import org.spacehq.packetlib.io.NetInput;
import org.spacehq.packetlib.packet.Packet;

public class LoginStartPacket implements Packet
{
    private String username;
    
    private LoginStartPacket() {
    }
    
    public LoginStartPacket(final String username) {
        this.username = username;
    }
    
    public String getUsername() {
        return this.username;
    }
    
    @Override
    public void read(final NetInput in) throws IOException {
        this.username = in.readString();
    }
    
    @Override
    public void write(final NetOutput out) throws IOException {
        out.writeString(this.username);
    }
    
    @Override
    public boolean isPriority() {
        return true;
    }
}
