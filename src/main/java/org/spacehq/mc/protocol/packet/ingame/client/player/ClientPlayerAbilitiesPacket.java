// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.protocol.packet.ingame.client.player;

import org.spacehq.packetlib.io.NetOutput;
import java.io.IOException;
import org.spacehq.packetlib.io.NetInput;
import org.spacehq.packetlib.packet.Packet;

public class ClientPlayerAbilitiesPacket implements Packet
{
    private boolean invincible;
    private boolean canFly;
    private boolean flying;
    private boolean creative;
    private float flySpeed;
    private float walkSpeed;
    
    private ClientPlayerAbilitiesPacket() {
    }
    
    public ClientPlayerAbilitiesPacket(final boolean invincible, final boolean canFly, final boolean flying, final boolean creative, final float flySpeed, final float walkSpeed) {
        this.invincible = invincible;
        this.canFly = canFly;
        this.flying = flying;
        this.creative = creative;
        this.flySpeed = flySpeed;
        this.walkSpeed = walkSpeed;
    }
    
    public boolean getInvincible() {
        return this.invincible;
    }
    
    public boolean getCanFly() {
        return this.canFly;
    }
    
    public boolean getFlying() {
        return this.flying;
    }
    
    public boolean getCreative() {
        return this.creative;
    }
    
    public float getFlySpeed() {
        return this.flySpeed;
    }
    
    public float getWalkSpeed() {
        return this.walkSpeed;
    }
    
    @Override
    public void read(final NetInput in) throws IOException {
        final byte flags = in.readByte();
        this.invincible = ((flags & 0x1) > 0);
        this.canFly = ((flags & 0x2) > 0);
        this.flying = ((flags & 0x4) > 0);
        this.creative = ((flags & 0x8) > 0);
        this.flySpeed = in.readFloat();
        this.walkSpeed = in.readFloat();
    }
    
    @Override
    public void write(final NetOutput out) throws IOException {
        byte flags = 0;
        if (this.invincible) {
            flags |= 0x1;
        }
        if (this.canFly) {
            flags |= 0x2;
        }
        if (this.flying) {
            flags |= 0x4;
        }
        if (this.creative) {
            flags |= 0x8;
        }
        out.writeByte(flags);
        out.writeFloat(this.flySpeed);
        out.writeFloat(this.walkSpeed);
    }
    
    @Override
    public boolean isPriority() {
        return false;
    }
}
