// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.protocol.packet.ingame.client.player;

import org.spacehq.packetlib.io.NetOutput;
import java.io.IOException;
import org.spacehq.packetlib.io.NetInput;
import org.spacehq.packetlib.packet.Packet;

public class ClientSwingArmPacket implements Packet
{
    @Override
    public void read(final NetInput in) throws IOException {
    }
    
    @Override
    public void write(final NetOutput out) throws IOException {
    }
    
    @Override
    public boolean isPriority() {
        return false;
    }
}
