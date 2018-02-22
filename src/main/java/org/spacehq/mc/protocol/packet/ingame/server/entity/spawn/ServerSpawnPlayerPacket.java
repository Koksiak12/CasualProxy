// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.protocol.packet.ingame.server.entity.spawn;

import org.spacehq.packetlib.io.NetOutput;
import java.io.IOException;
import org.spacehq.mc.protocol.util.NetUtil;
import org.spacehq.packetlib.io.NetInput;
import org.spacehq.mc.protocol.data.game.EntityMetadata;
import java.util.UUID;
import org.spacehq.packetlib.packet.Packet;

public class ServerSpawnPlayerPacket implements Packet
{
    private int entityId;
    private UUID uuid;
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;
    private int currentItem;
    private EntityMetadata[] metadata;
    
    private ServerSpawnPlayerPacket() {
    }
    
    public ServerSpawnPlayerPacket(final int entityId, final UUID uuid, final double x, final double y, final double z, final float yaw, final float pitch, final int currentItem, final EntityMetadata[] metadata) {
        this.entityId = entityId;
        this.uuid = uuid;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.currentItem = currentItem;
        this.metadata = metadata;
    }
    
    public int getEntityId() {
        return this.entityId;
    }
    
    public UUID getUUID() {
        return this.uuid;
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
    
    public int getCurrentItem() {
        return this.currentItem;
    }
    
    public EntityMetadata[] getMetadata() {
        return this.metadata;
    }
    
    @Override
    public void read(final NetInput in) throws IOException {
        this.entityId = in.readVarInt();
        this.uuid = in.readUUID();
        this.x = in.readInt() / 32.0;
        this.y = in.readInt() / 32.0;
        this.z = in.readInt() / 32.0;
        this.yaw = in.readByte() * 360 / 256.0f;
        this.pitch = in.readByte() * 360 / 256.0f;
        this.currentItem = in.readShort();
        this.metadata = NetUtil.readEntityMetadata(in);
    }
    
    @Override
    public void write(final NetOutput out) throws IOException {
        out.writeVarInt(this.entityId);
        out.writeUUID(this.uuid);
        out.writeInt((int)(this.x * 32.0));
        out.writeInt((int)(this.y * 32.0));
        out.writeInt((int)(this.z * 32.0));
        out.writeByte((byte)(this.yaw * 256.0f / 360.0f));
        out.writeByte((byte)(this.pitch * 256.0f / 360.0f));
        out.writeShort(this.currentItem);
        NetUtil.writeEntityMetadata(out, this.metadata);
    }
    
    @Override
    public boolean isPriority() {
        return false;
    }
}
