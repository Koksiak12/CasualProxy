// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.protocol.packet.ingame.server.window;

import org.spacehq.packetlib.io.NetOutput;
import java.io.IOException;
import org.spacehq.packetlib.io.NetInput;
import org.spacehq.packetlib.packet.Packet;

public class ServerConfirmTransactionPacket implements Packet
{
    private int windowId;
    private int actionId;
    private boolean accepted;
    
    private ServerConfirmTransactionPacket() {
    }
    
    public ServerConfirmTransactionPacket(final int windowId, final int actionId, final boolean accepted) {
        this.windowId = windowId;
        this.actionId = actionId;
        this.accepted = accepted;
    }
    
    public int getWindowId() {
        return this.windowId;
    }
    
    public int getActionId() {
        return this.actionId;
    }
    
    public boolean getAccepted() {
        return this.accepted;
    }
    
    @Override
    public void read(final NetInput in) throws IOException {
        this.windowId = in.readUnsignedByte();
        this.actionId = in.readShort();
        this.accepted = in.readBoolean();
    }
    
    @Override
    public void write(final NetOutput out) throws IOException {
        out.writeByte(this.windowId);
        out.writeShort(this.actionId);
        out.writeBoolean(this.accepted);
    }
    
    @Override
    public boolean isPriority() {
        return false;
    }
}
