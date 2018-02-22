// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.protocol.packet.ingame.server.entity;

public class ServerEntityRotationPacket extends ServerEntityMovementPacket
{
    protected ServerEntityRotationPacket() {
        this.rot = true;
    }
    
    public ServerEntityRotationPacket(final int entityId, final float yaw, final float pitch, final boolean onGround) {
        super(entityId, onGround);
        this.rot = true;
        this.yaw = yaw;
        this.pitch = pitch;
    }
}
