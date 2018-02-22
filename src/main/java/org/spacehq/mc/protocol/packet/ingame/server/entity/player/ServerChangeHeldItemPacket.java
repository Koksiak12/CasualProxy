// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.protocol.packet.ingame.server.entity.player;

import org.spacehq.packetlib.io.NetOutput;
import java.io.IOException;
import org.spacehq.packetlib.io.NetInput;
import org.spacehq.packetlib.packet.Packet;

public class ServerChangeHeldItemPacket implements Packet
{
    private int slot;
    
    private ServerChangeHeldItemPacket() {
    }
    
    public ServerChangeHeldItemPacket(final int slot) {
        this.slot = slot;
    }
    
    public int getSlot() {
        return this.slot;
    }
    
    @Override
    public void read(final NetInput in) throws IOException {
        this.slot = in.readByte();
    }
    
    @Override
    public void write(final NetOutput out) throws IOException {
        out.writeByte(this.slot);
    }
    
    @Override
    public boolean isPriority() {
        return false;
    }
}
