// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.protocol.packet.ingame.client.player;

public class ClientPlayerRotationPacket extends ClientPlayerMovementPacket
{
    protected ClientPlayerRotationPacket() {
        this.rot = true;
    }
    
    public ClientPlayerRotationPacket(final boolean onGround, final float yaw, final float pitch) {
        super(onGround);
        this.rot = true;
        this.yaw = yaw;
        this.pitch = pitch;
    }
}
