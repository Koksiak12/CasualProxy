// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.protocol.packet.ingame.client.player;

public class ClientPlayerPositionRotationPacket extends ClientPlayerMovementPacket
{
    protected ClientPlayerPositionRotationPacket() {
        this.pos = true;
        this.rot = true;
    }
    
    public ClientPlayerPositionRotationPacket(final boolean onGround, final double x, final double y, final double z, final float yaw, final float pitch) {
        super(onGround);
        this.pos = true;
        this.rot = true;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }
}
