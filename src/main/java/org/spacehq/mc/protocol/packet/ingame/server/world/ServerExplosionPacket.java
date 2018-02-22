// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.protocol.packet.ingame.server.world;

import java.util.Iterator;
import org.spacehq.packetlib.io.NetOutput;
import java.io.IOException;
import java.util.ArrayList;
import org.spacehq.packetlib.io.NetInput;
import org.spacehq.mc.protocol.data.game.values.world.block.ExplodedBlockRecord;
import java.util.List;
import org.spacehq.packetlib.packet.Packet;

public class ServerExplosionPacket implements Packet
{
    private float x;
    private float y;
    private float z;
    private float radius;
    private List<ExplodedBlockRecord> exploded;
    private float pushX;
    private float pushY;
    private float pushZ;
    
    private ServerExplosionPacket() {
    }
    
    public ServerExplosionPacket(final float x, final float y, final float z, final float radius, final List<ExplodedBlockRecord> exploded, final float pushX, final float pushY, final float pushZ) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.radius = radius;
        this.exploded = exploded;
        this.pushX = pushX;
        this.pushY = pushY;
        this.pushZ = pushZ;
    }
    
    public float getX() {
        return this.x;
    }
    
    public float getY() {
        return this.y;
    }
    
    public float getZ() {
        return this.z;
    }
    
    public float getRadius() {
        return this.radius;
    }
    
    public List<ExplodedBlockRecord> getExploded() {
        return this.exploded;
    }
    
    public float getPushX() {
        return this.pushX;
    }
    
    public float getPushY() {
        return this.pushY;
    }
    
    public float getPushZ() {
        return this.pushZ;
    }
    
    @Override
    public void read(final NetInput in) throws IOException {
        this.x = in.readFloat();
        this.y = in.readFloat();
        this.z = in.readFloat();
        this.radius = in.readFloat();
        this.exploded = new ArrayList<ExplodedBlockRecord>();
        for (int length = in.readInt(), count = 0; count < length; ++count) {
            this.exploded.add(new ExplodedBlockRecord(in.readByte(), in.readByte(), in.readByte()));
        }
        this.pushX = in.readFloat();
        this.pushY = in.readFloat();
        this.pushZ = in.readFloat();
    }
    
    @Override
    public void write(final NetOutput out) throws IOException {
        out.writeFloat(this.x);
        out.writeFloat(this.y);
        out.writeFloat(this.z);
        out.writeFloat(this.radius);
        out.writeInt(this.exploded.size());
        for (final ExplodedBlockRecord record : this.exploded) {
            out.writeByte(record.getX());
            out.writeByte(record.getY());
            out.writeByte(record.getZ());
        }
        out.writeFloat(this.pushX);
        out.writeFloat(this.pushY);
        out.writeFloat(this.pushZ);
    }
    
    @Override
    public boolean isPriority() {
        return false;
    }
}
