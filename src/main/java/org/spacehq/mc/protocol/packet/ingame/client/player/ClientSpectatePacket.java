// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.protocol.packet.ingame.client.player;

import org.spacehq.packetlib.io.NetOutput;
import java.io.IOException;
import org.spacehq.packetlib.io.NetInput;
import java.util.UUID;
import org.spacehq.packetlib.packet.Packet;

public class ClientSpectatePacket implements Packet
{
    private UUID target;
    
    private ClientSpectatePacket() {
    }
    
    public ClientSpectatePacket(final UUID target) {
        this.target = target;
    }
    
    public UUID getTarget() {
        return this.target;
    }
    
    @Override
    public void read(final NetInput in) throws IOException {
        this.target = in.readUUID();
    }
    
    @Override
    public void write(final NetOutput out) throws IOException {
        out.writeUUID(this.target);
    }
    
    @Override
    public boolean isPriority() {
        return false;
    }
}
