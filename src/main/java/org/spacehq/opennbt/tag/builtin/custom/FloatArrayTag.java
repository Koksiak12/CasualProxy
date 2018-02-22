// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.opennbt.tag.builtin.custom;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.DataInputStream;
import org.spacehq.opennbt.tag.builtin.Tag;

public class FloatArrayTag extends Tag
{
    private float[] value;
    
    public FloatArrayTag(final String name) {
        this(name, new float[0]);
    }
    
    public FloatArrayTag(final String name, final float[] value) {
        super(name);
        this.value = value;
    }
    
    @Override
    public float[] getValue() {
        return this.value.clone();
    }
    
    public void setValue(final float[] value) {
        if (value == null) {
            return;
        }
        this.value = value.clone();
    }
    
    public float getValue(final int index) {
        return this.value[index];
    }
    
    public void setValue(final int index, final float value) {
        this.value[index] = value;
    }
    
    public int length() {
        return this.value.length;
    }
    
    @Override
    public void read(final DataInputStream in) throws IOException {
        this.value = new float[in.readInt()];
        for (int index = 0; index < this.value.length; ++index) {
            this.value[index] = in.readFloat();
        }
    }
    
    @Override
    public void write(final DataOutputStream out) throws IOException {
        out.writeInt(this.value.length);
        for (int index = 0; index < this.value.length; ++index) {
            out.writeFloat(this.value[index]);
        }
    }
    
    @Override
    public FloatArrayTag clone() {
        return new FloatArrayTag(this.getName(), this.getValue());
    }
}
