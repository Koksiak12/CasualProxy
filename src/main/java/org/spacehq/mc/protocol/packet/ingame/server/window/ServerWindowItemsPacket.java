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

public class ServerWindowItemsPacket implements Packet
{
    private int windowId;
    private ItemStack[] items;
    
    private ServerWindowItemsPacket() {
    }
    
    public ServerWindowItemsPacket(final int windowId, final ItemStack[] items) {
        this.windowId = windowId;
        this.items = items;
    }
    
    public int getWindowId() {
        return this.windowId;
    }
    
    public ItemStack[] getItems() {
        return this.items;
    }
    
    @Override
    public void read(final NetInput in) throws IOException {
        this.windowId = in.readUnsignedByte();
        this.items = new ItemStack[in.readShort()];
        for (int index = 0; index < this.items.length; ++index) {
            this.items[index] = NetUtil.readItem(in);
        }
    }
    
    @Override
    public void write(final NetOutput out) throws IOException {
        out.writeByte(this.windowId);
        out.writeShort(this.items.length);
        for (final ItemStack item : this.items) {
            NetUtil.writeItem(out, item);
        }
    }
    
    @Override
    public boolean isPriority() {
        return false;
    }
}
