// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.protocol.packet.ingame.client.window;

import org.spacehq.packetlib.io.NetOutput;
import java.io.IOException;
import org.spacehq.mc.protocol.util.NetUtil;
import org.spacehq.packetlib.io.NetInput;
import org.spacehq.mc.protocol.data.game.ItemStack;
import org.spacehq.packetlib.packet.Packet;

public class ClientCreativeInventoryActionPacket implements Packet
{
    private int slot;
    private ItemStack clicked;
    
    private ClientCreativeInventoryActionPacket() {
    }
    
    public ClientCreativeInventoryActionPacket(final int slot, final ItemStack clicked) {
        this.slot = slot;
        this.clicked = clicked;
    }
    
    public int getSlot() {
        return this.slot;
    }
    
    public ItemStack getClickedItem() {
        return this.clicked;
    }
    
    @Override
    public void read(final NetInput in) throws IOException {
        this.slot = in.readShort();
        this.clicked = NetUtil.readItem(in);
    }
    
    @Override
    public void write(final NetOutput out) throws IOException {
        out.writeShort(this.slot);
        NetUtil.writeItem(out, this.clicked);
    }
    
    @Override
    public boolean isPriority() {
        return false;
    }
}
