// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.protocol.packet.ingame.server.world;

import org.spacehq.packetlib.io.NetOutput;
import java.io.IOException;
import org.spacehq.mc.protocol.data.game.values.world.CustomSound;
import org.spacehq.mc.protocol.data.game.values.MagicValues;
import org.spacehq.mc.protocol.data.game.values.world.GenericSound;
import org.spacehq.packetlib.io.NetInput;
import org.spacehq.mc.protocol.data.game.values.world.Sound;
import org.spacehq.packetlib.packet.Packet;

public class ServerPlaySoundPacket implements Packet
{
    private Sound sound;
    private double x;
    private double y;
    private double z;
    private float volume;
    private float pitch;
    
    private ServerPlaySoundPacket() {
    }
    
    public ServerPlaySoundPacket(final Sound sound, final double x, final double y, final double z, final float volume, final float pitch) {
        this.sound = sound;
        this.x = x;
        this.y = y;
        this.z = z;
        this.volume = volume;
        this.pitch = pitch;
    }
    
    public Sound getSound() {
        return this.sound;
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
    
    public float getVolume() {
        return this.volume;
    }
    
    public float getPitch() {
        return this.pitch;
    }
    
    @Override
    public void read(final NetInput in) throws IOException {
        final String value = in.readString();
        this.sound = MagicValues.key(GenericSound.class, value);
        if (this.sound == null) {
            this.sound = new CustomSound(value);
        }
        this.x = in.readInt() / 8.0;
        this.y = in.readInt() / 8.0;
        this.z = in.readInt() / 8.0;
        this.volume = in.readFloat();
        this.pitch = in.readUnsignedByte() / 63.0f;
    }
    
    @Override
    public void write(final NetOutput out) throws IOException {
        String value = "";
        if (this.sound instanceof CustomSound) {
            value = ((CustomSound)this.sound).getName();
        }
        else if (this.sound instanceof GenericSound) {
            value = MagicValues.value(String.class, (Enum<?>)this.sound);
        }
        out.writeString(value);
        out.writeInt((int)(this.x * 8.0));
        out.writeInt((int)(this.y * 8.0));
        out.writeInt((int)(this.z * 8.0));
        out.writeFloat(this.volume);
        int pitch = (int)(this.pitch * 63.0f);
        if (pitch > 255) {
            pitch = 255;
        }
        if (pitch < 0) {
            pitch = 0;
        }
        out.writeByte(pitch);
    }
    
    @Override
    public boolean isPriority() {
        return false;
    }
}
