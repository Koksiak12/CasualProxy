// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.protocol.packet.ingame.client.player;

import org.spacehq.packetlib.io.NetOutput;
import java.io.IOException;
import org.spacehq.packetlib.io.NetInput;
import org.spacehq.packetlib.packet.Packet;

public class ClientChangeHeldItemPacket implements Packet
{
    private int slot;
    
    private ClientChangeHeldItemPacket() {
    }
    
    public ClientChangeHeldItemPacket(final int slot) {
        this.slot = slot;
    }
    
    public int getSlot() {
        return this.slot;
    }
    
    @Override
    public void read(final NetInput in) throws IOException {
        this.slot = in.readShort();
    }
    
    @Override
    public void write(final NetOutput out) throws IOException {
        out.writeShort(this.slot);
    }
    
    @Override
    public boolean isPriority() {
        return false;
    }
}
