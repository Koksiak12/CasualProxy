// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.protocol.packet.login.server;

import org.spacehq.packetlib.io.NetOutput;
import java.io.IOException;
import org.spacehq.packetlib.io.NetInput;
import org.spacehq.mc.auth.data.GameProfile;
import org.spacehq.packetlib.packet.Packet;

public class LoginSuccessPacket implements Packet
{
    private GameProfile profile;
    
    private LoginSuccessPacket() {
    }
    
    public LoginSuccessPacket(final GameProfile profile) {
        this.profile = profile;
    }
    
    public GameProfile getProfile() {
        return this.profile;
    }
    
    @Override
    public void read(final NetInput in) throws IOException {
        this.profile = new GameProfile(in.readString(), in.readString());
    }
    
    @Override
    public void write(final NetOutput out) throws IOException {
        out.writeString(this.profile.getIdAsString());
        out.writeString(this.profile.getName());
    }
    
    @Override
    public boolean isPriority() {
        return true;
    }
}
