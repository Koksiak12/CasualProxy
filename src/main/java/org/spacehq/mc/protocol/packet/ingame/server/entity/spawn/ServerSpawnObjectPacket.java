// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.protocol.packet.ingame.server.entity.spawn;

import org.spacehq.packetlib.io.NetOutput;
import java.io.IOException;
import org.spacehq.mc.protocol.data.game.values.entity.ProjectileData;
import org.spacehq.mc.protocol.data.game.values.entity.SplashPotionData;
import org.spacehq.mc.protocol.data.game.values.entity.FallingBlockData;
import org.spacehq.mc.protocol.data.game.values.entity.HangingDirection;
import org.spacehq.mc.protocol.data.game.values.entity.MinecartType;
import org.spacehq.mc.protocol.data.game.values.MagicValues;
import org.spacehq.packetlib.io.NetInput;
import org.spacehq.mc.protocol.data.game.values.entity.ObjectData;
import org.spacehq.mc.protocol.data.game.values.entity.ObjectType;
import org.spacehq.packetlib.packet.Packet;

public class ServerSpawnObjectPacket implements Packet
{
    private int entityId;
    private ObjectType type;
    private double x;
    private double y;
    private double z;
    private float pitch;
    private float yaw;
    private ObjectData data;
    private double motX;
    private double motY;
    private double motZ;
    
    private ServerSpawnObjectPacket() {
    }
    
    public ServerSpawnObjectPacket(final int entityId, final ObjectType type, final double x, final double y, final double z, final float yaw, final float pitch) {
        this(entityId, type, null, x, y, z, yaw, pitch, 0.0, 0.0, 0.0);
    }
    
    public ServerSpawnObjectPacket(final int entityId, final ObjectType type, final ObjectData data, final double x, final double y, final double z, final float yaw, final float pitch) {
        this(entityId, type, data, x, y, z, yaw, pitch, 0.0, 0.0, 0.0);
    }
    
    public ServerSpawnObjectPacket(final int entityId, final ObjectType type, final double x, final double y, final double z, final float yaw, final float pitch, final double motX, final double motY, final double motZ) {
        this(entityId, type, new ObjectData() {}, x, y, z, yaw, pitch, motX, motY, motZ);
    }
    
    public ServerSpawnObjectPacket(final int entityId, final ObjectType type, final ObjectData data, final double x, final double y, final double z, final float yaw, final float pitch, final double motX, final double motY, final double motZ) {
        this.entityId = entityId;
        this.type = type;
        this.data = data;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.motX = motX;
        this.motY = motY;
        this.motZ = motZ;
    }
    
    public int getEntityId() {
        return this.entityId;
    }
    
    public ObjectType getType() {
        return this.type;
    }
    
    public ObjectData getData() {
        return this.data;
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
    
    public double getMotionX() {
        return this.motX;
    }
    
    public double getMotionY() {
        return this.motY;
    }
    
    public double getMotionZ() {
        return this.motZ;
    }
    
    @Override
    public void read(final NetInput in) throws IOException {
        this.entityId = in.readVarInt();
        this.type = MagicValues.key(ObjectType.class, in.readByte());
        this.x = in.readInt() / 32.0;
        this.y = in.readInt() / 32.0;
        this.z = in.readInt() / 32.0;
        this.pitch = in.readByte() * 360 / 256.0f;
        this.yaw = in.readByte() * 360 / 256.0f;
        final int data = in.readInt();
        if (data > 0) {
            if (this.type == ObjectType.MINECART) {
                this.data = MagicValues.key(MinecartType.class, data);
            }
            else if (this.type == ObjectType.ITEM_FRAME) {
                this.data = MagicValues.key(HangingDirection.class, data);
            }
            else if (this.type == ObjectType.FALLING_BLOCK) {
                this.data = new FallingBlockData(data & 0xFFFF, data >> 16);
            }
            else if (this.type == ObjectType.POTION) {
                this.data = new SplashPotionData(data);
            }
            else if (this.type == ObjectType.ARROW || this.type == ObjectType.BLAZE_FIREBALL || this.type == ObjectType.FISH_HOOK || this.type == ObjectType.GHAST_FIREBALL || this.type == ObjectType.WITHER_HEAD_PROJECTILE) {
                this.data = new ProjectileData(data);
            }
            else {
                this.data = new ObjectData() {};
            }
            this.motX = in.readShort() / 8000.0;
            this.motY = in.readShort() / 8000.0;
            this.motZ = in.readShort() / 8000.0;
        }
    }
    
    @Override
    public void write(final NetOutput out) throws IOException {
        out.writeVarInt(this.entityId);
        out.writeByte(MagicValues.value(Integer.class, this.type));
        out.writeInt((int)(this.x * 32.0));
        out.writeInt((int)(this.y * 32.0));
        out.writeInt((int)(this.z * 32.0));
        out.writeByte((byte)(this.pitch * 256.0f / 360.0f));
        out.writeByte((byte)(this.yaw * 256.0f / 360.0f));
        int data = 0;
        if (this.data != null) {
            if (this.data instanceof MinecartType) {
                data = MagicValues.value(Integer.class, (Enum<?>)this.data);
            }
            else if (this.data instanceof HangingDirection) {
                data = MagicValues.value(Integer.class, (Enum<?>)this.data);
            }
            else if (this.data instanceof FallingBlockData) {
                data = (((FallingBlockData)this.data).getId() | ((FallingBlockData)this.data).getMetadata() << 16);
            }
            else if (this.data instanceof SplashPotionData) {
                data = ((SplashPotionData)this.data).getPotionData();
            }
            else if (this.data instanceof ProjectileData) {
                data = ((ProjectileData)this.data).getOwnerId();
            }
            else {
                data = 1;
            }
        }
        out.writeInt(data);
        if (data > 0) {
            out.writeShort((int)(this.motX * 8000.0));
            out.writeShort((int)(this.motY * 8000.0));
            out.writeShort((int)(this.motZ * 8000.0));
        }
    }
    
    @Override
    public boolean isPriority() {
        return false;
    }
}
