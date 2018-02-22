// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.protocol.packet.ingame.server.entity;

import org.spacehq.packetlib.io.NetOutput;
import java.io.IOException;
import org.spacehq.packetlib.io.NetInput;
import org.spacehq.packetlib.packet.Packet;

public class ServerEntityAttachPacket implements Packet
{
    private int entityId;
    private int attachedToId;
    private boolean leash;
    
    private ServerEntityAttachPacket() {
    }
    
    public ServerEntityAttachPacket(final int entityId, final int attachedToId, final boolean leash) {
        this.entityId = entityId;
        this.attachedToId = attachedToId;
        this.leash = leash;
    }
    
    public int getEntityId() {
        return this.entityId;
    }
    
    public int getAttachedToId() {
        return this.attachedToId;
    }
    
    public boolean getLeash() {
        return this.leash;
    }
    
    @Override
    public void read(final NetInput in) throws IOException {
        this.entityId = in.readInt();
        this.attachedToId = in.readInt();
        this.leash = in.readBoolean();
    }
    
    @Override
    public void write(final NetOutput out) throws IOException {
        out.writeInt(this.entityId);
        out.writeInt(this.attachedToId);
        out.writeBoolean(this.leash);
    }
    
    @Override
    public boolean isPriority() {
        return false;
    }
}
