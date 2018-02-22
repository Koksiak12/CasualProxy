// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.opennbt.tag.builtin;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.DataInputStream;

public class FloatTag extends Tag
{
    private float value;
    
    public FloatTag(final String name) {
        this(name, 0.0f);
    }
    
    public FloatTag(final String name, final float value) {
        super(name);
        this.value = value;
    }
    
    @Override
    public Float getValue() {
        return this.value;
    }
    
    public void setValue(final float value) {
        this.value = value;
    }
    
    @Override
    public void read(final DataInputStream in) throws IOException {
        this.value = in.readFloat();
    }
    
    @Override
    public void write(final DataOutputStream out) throws IOException {
        out.writeFloat(this.value);
    }
    
    @Override
    public FloatTag clone() {
        return new FloatTag(this.getName(), this.getValue());
    }
}
