// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.protocol.packet.ingame.server.window;

import org.spacehq.packetlib.io.NetOutput;
import java.io.IOException;
import org.spacehq.packetlib.io.NetInput;
import org.spacehq.mc.protocol.data.game.values.MagicValues;
import org.spacehq.packetlib.packet.Packet;

public class ServerWindowPropertyPacket implements Packet
{
    private int windowId;
    private int property;
    private int value;
    
    private ServerWindowPropertyPacket() {
    }
    
    public ServerWindowPropertyPacket(final int windowId, final int property, final int value) {
        this.windowId = windowId;
        this.property = property;
        this.value = value;
    }
    
    public int getWindowId() {
        return this.windowId;
    }
    
    public int getRawProperty() {
        return this.property;
    }
    
    public <T extends Enum> T getProperty(final Class<T> type) {
        return MagicValues.key(type, this.value);
    }
    
    public int getValue() {
        return this.value;
    }
    
    @Override
    public void read(final NetInput in) throws IOException {
        this.windowId = in.readUnsignedByte();
        this.property = in.readShort();
        this.value = in.readShort();
    }
    
    @Override
    public void write(final NetOutput out) throws IOException {
        out.writeByte(this.windowId);
        out.writeShort(this.property);
        out.writeShort(this.value);
    }
    
    @Override
    public boolean isPriority() {
        return false;
    }
}
