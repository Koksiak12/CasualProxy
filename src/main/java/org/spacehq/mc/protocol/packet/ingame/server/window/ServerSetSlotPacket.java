// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.protocol.packet.ingame.server.window;

import org.spacehq.packetlib.io.NetOutput;
import java.io.IOException;
import org.spacehq.mc.protocol.util.NetUtil;
import org.spacehq.packetlib.io.NetInput;
import org.spacehq.mc.protocol.data.game.ItemStack;
import org.spacehq.packetlib.packet.Packet;

public class ServerSetSlotPacket implements Packet
{
    private int windowId;
    private int slot;
    private ItemStack item;
    
    private ServerSetSlotPacket() {
    }
    
    public ServerSetSlotPacket(final int windowId, final int slot, final ItemStack item) {
        this.windowId = windowId;
        this.slot = slot;
        this.item = item;
    }
    
    public int getWindowId() {
        return this.windowId;
    }
    
    public int getSlot() {
        return this.slot;
    }
    
    public ItemStack getItem() {
        return this.item;
    }
    
    @Override
    public void read(final NetInput in) throws IOException {
        this.windowId = in.readUnsignedByte();
        this.slot = in.readShort();
        this.item = NetUtil.readItem(in);
    }
    
    @Override
    public void write(final NetOutput out) throws IOException {
        out.writeByte(this.windowId);
        out.writeShort(this.slot);
        NetUtil.writeItem(out, this.item);
    }
    
    @Override
    public boolean isPriority() {
        return false;
    }
}
