// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.protocol.packet.ingame.server.entity.player;

import java.util.Iterator;
import org.spacehq.packetlib.io.NetOutput;
import java.io.IOException;
import org.spacehq.mc.protocol.data.game.values.MagicValues;
import java.util.ArrayList;
import org.spacehq.packetlib.io.NetInput;
import java.util.Arrays;
import org.spacehq.mc.protocol.data.game.values.entity.player.PositionElement;
import java.util.List;
import org.spacehq.packetlib.packet.Packet;

public class ServerPlayerPositionRotationPacket implements Packet
{
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;
    private List<PositionElement> relative;
    
    private ServerPlayerPositionRotationPacket() {
    }
    
    public ServerPlayerPositionRotationPacket(final double x, final double y, final double z, final float yaw, final float pitch) {
        this(x, y, z, yaw, pitch, new PositionElement[0]);
    }
    
    public ServerPlayerPositionRotationPacket(final double x, final double y, final double z, final float yaw, final float pitch, final PositionElement... relative) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.relative = Arrays.asList(relative);
    }
    
    public double getX() {
        return this.x;
    }
    
    public double getY() {
        return this.y;
    }
    
    public double getZ() {
        return this.z;
    }
    
    public float getYaw() {
        return this.yaw;
    }
    
    public float getPitch() {
        return this.pitch;
    }
    
    public List<PositionElement> getRelativeElements() {
        return this.relative;
    }
    
    @Override
    public void read(final NetInput in) throws IOException {
        this.x = in.readDouble();
        this.y = in.readDouble();
        this.z = in.readDouble();
        this.yaw = in.readFloat();
        this.pitch = in.readFloat();
        this.relative = new ArrayList<PositionElement>();
        final int flags = in.readUnsignedByte();
        for (final PositionElement element : PositionElement.values()) {
            final int bit = 1 << MagicValues.value(Integer.class, element);
            if ((flags & bit) == bit) {
                this.relative.add(element);
            }
        }
    }
    
    @Override
    public void write(final NetOutput out) throws IOException {
        out.writeDouble(this.x);
        out.writeDouble(this.y);
        out.writeDouble(this.z);
        out.writeFloat(this.yaw);
        out.writeFloat(this.pitch);
        int flags = 0;
        for (final PositionElement element : this.relative) {
            flags |= 1 << MagicValues.value(Integer.class, element);
        }
        out.writeByte(flags);
    }
    
    @Override
    public boolean isPriority() {
        return false;
    }
}
