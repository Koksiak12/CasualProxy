// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.protocol.packet.ingame.server.entity;

public class ServerEntityPositionRotationPacket extends ServerEntityMovementPacket
{
    protected ServerEntityPositionRotationPacket() {
        this.pos = true;
        this.rot = true;
    }
    
    public ServerEntityPositionRotationPacket(final int entityId, final double moveX, final double moveY, final double moveZ, final float yaw, final float pitch, final boolean onGround) {
        super(entityId, onGround);
        this.pos = true;
        this.rot = true;
        this.moveX = moveX;
        this.moveY = moveY;
        this.moveZ = moveZ;
        this.yaw = yaw;
        this.pitch = pitch;
    }
}
