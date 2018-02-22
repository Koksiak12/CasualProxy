// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.protocol.packet.ingame.client.player;

public class ClientPlayerPositionPacket extends ClientPlayerMovementPacket
{
    protected ClientPlayerPositionPacket() {
        this.pos = true;
    }
    
    public ClientPlayerPositionPacket(final boolean onGround, final double x, final double y, final double z) {
        super(onGround);
        this.pos = true;
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
