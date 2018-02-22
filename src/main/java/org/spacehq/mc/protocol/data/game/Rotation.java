// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.protocol.data.game;

public class Rotation
{
    private float pitch;
    private float yaw;
    private float roll;
    
    public Rotation() {
        this(0.0f, 0.0f, 0.0f);
    }
    
    public Rotation(final float pitch, final float yaw, final float roll) {
        this.pitch = pitch;
        this.yaw = yaw;
        this.roll = roll;
    }
    
    public float getPitch() {
        return this.pitch;
    }
    
    public float getYaw() {
        return this.yaw;
    }
    
    public float getRoll() {
        return this.roll;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final Rotation rotation = (Rotation)o;
        return Float.compare(rotation.pitch, this.pitch) == 0 && Float.compare(rotation.roll, this.roll) == 0 && Float.compare(rotation.yaw, this.yaw) == 0;
    }
    
    @Override
    public int hashCode() {
        int result = (this.pitch != 0.0f) ? Float.floatToIntBits(this.pitch) : 0;
        result = 31 * result + ((this.yaw != 0.0f) ? Float.floatToIntBits(this.yaw) : 0);
        result = 31 * result + ((this.roll != 0.0f) ? Float.floatToIntBits(this.roll) : 0);
        return result;
    }
}
