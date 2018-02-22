// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.protocol.packet.ingame.server.entity;

public class ServerEntityPositionPacket extends ServerEntityMovementPacket
{
    protected ServerEntityPositionPacket() {
        this.pos = true;
    }
    
    public ServerEntityPositionPacket(final int entityId, final double moveX, final double moveY, final double moveZ, final boolean onGround) {
        super(entityId, onGround);
        this.pos = true;
        this.moveX = moveX;
        this.moveY = moveY;
        this.moveZ = moveZ;
    }
}
