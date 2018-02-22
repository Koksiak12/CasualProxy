// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.protocol.packet.ingame.client.player;

import org.spacehq.packetlib.io.NetOutput;
import java.io.IOException;
import org.spacehq.packetlib.io.NetInput;
import org.spacehq.packetlib.packet.Packet;

public class ClientSteerVehiclePacket implements Packet
{
    private float sideways;
    private float forward;
    private boolean jump;
    private boolean dismount;
    
    private ClientSteerVehiclePacket() {
    }
    
    public ClientSteerVehiclePacket(final float sideways, final float forward, final boolean jump, final boolean dismount) {
        this.sideways = sideways;
        this.forward = forward;
        this.jump = jump;
        this.dismount = dismount;
    }
    
    public float getSideways() {
        return this.sideways;
    }
    
    public float getForward() {
        return this.forward;
    }
    
    public boolean getJumping() {
        return this.jump;
    }
    
    public boolean getDismounting() {
        return this.dismount;
    }
    
    @Override
    public void read(final NetInput in) throws IOException {
        this.sideways = in.readFloat();
        this.forward = in.readFloat();
        final int flags = in.readUnsignedByte();
        this.jump = ((flags & 0x1) > 0);
        this.dismount = ((flags & 0x2) > 0);
    }
    
    @Override
    public void write(final NetOutput out) throws IOException {
        out.writeFloat(this.sideways);
        out.writeFloat(this.forward);
        byte flags = 0;
        if (this.jump) {
            flags |= 0x1;
        }
        if (this.dismount) {
            flags |= 0x2;
        }
        out.writeByte(flags);
    }
    
    @Override
    public boolean isPriority() {
        return false;
    }
}
