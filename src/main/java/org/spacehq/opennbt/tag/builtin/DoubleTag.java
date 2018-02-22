// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.opennbt.tag.builtin;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.DataInputStream;

public class DoubleTag extends Tag
{
    private double value;
    
    public DoubleTag(final String name) {
        this(name, 0.0);
    }
    
    public DoubleTag(final String name, final double value) {
        super(name);
        this.value = value;
    }
    
    @Override
    public Double getValue() {
        return this.value;
    }
    
    public void setValue(final double value) {
        this.value = value;
    }
    
    @Override
    public void read(final DataInputStream in) throws IOException {
        this.value = in.readDouble();
    }
    
    @Override
    public void write(final DataOutputStream out) throws IOException {
        out.writeDouble(this.value);
    }
    
    @Override
    public DoubleTag clone() {
        return new DoubleTag(this.getName(), this.getValue());
    }
}
