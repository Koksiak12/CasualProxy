// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.protocol.packet.ingame.client;

import org.spacehq.packetlib.io.NetOutput;
import java.io.IOException;
import org.spacehq.mc.protocol.data.game.values.MagicValues;
import org.spacehq.packetlib.io.NetInput;
import org.spacehq.mc.protocol.data.game.values.ResourcePackStatus;
import org.spacehq.packetlib.packet.Packet;

public class ClientResourcePackStatusPacket implements Packet
{
    private String hash;
    private ResourcePackStatus status;
    
    private ClientResourcePackStatusPacket() {
    }
    
    public ClientResourcePackStatusPacket(final String hash, final ResourcePackStatus status) {
        this.hash = hash;
        this.status = status;
    }
    
    public String getHash() {
        return this.hash;
    }
    
    public ResourcePackStatus getStatus() {
        return this.status;
    }
    
    @Override
    public void read(final NetInput in) throws IOException {
        this.hash = in.readString();
        this.status = MagicValues.key(ResourcePackStatus.class, in.readVarInt());
    }
    
    @Override
    public void write(final NetOutput out) throws IOException {
        out.writeString(this.hash);
        out.writeVarInt(MagicValues.value(Integer.class, this.status));
    }
    
    @Override
    public boolean isPriority() {
        return false;
    }
}
