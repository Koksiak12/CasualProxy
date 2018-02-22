// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.protocol.packet.ingame.client.window;

import org.spacehq.packetlib.io.NetOutput;
import java.io.IOException;
import org.spacehq.packetlib.io.NetInput;
import org.spacehq.packetlib.packet.Packet;

public class ClientEnchantItemPacket implements Packet
{
    private int windowId;
    private int enchantment;
    
    private ClientEnchantItemPacket() {
    }
    
    public ClientEnchantItemPacket(final int windowId, final int enchantment) {
        this.windowId = windowId;
        this.enchantment = enchantment;
    }
    
    public int getWindowId() {
        return this.windowId;
    }
    
    public int getEnchantment() {
        return this.enchantment;
    }
    
    @Override
    public void read(final NetInput in) throws IOException {
        this.windowId = in.readByte();
        this.enchantment = in.readByte();
    }
    
    @Override
    public void write(final NetOutput out) throws IOException {
        out.writeByte(this.windowId);
        out.writeByte(this.enchantment);
    }
    
    @Override
    public boolean isPriority() {
        return false;
    }
}
