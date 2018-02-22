// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.opennbt.tag.builtin.custom;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.DataInputStream;
import org.spacehq.opennbt.tag.builtin.Tag;

public class DoubleArrayTag extends Tag
{
    private double[] value;
    
    public DoubleArrayTag(final String name) {
        this(name, new double[0]);
    }
    
    public DoubleArrayTag(final String name, final double[] value) {
        super(name);
        this.value = value;
    }
    
    @Override
    public double[] getValue() {
        return this.value.clone();
    }
    
    public void setValue(final double[] value) {
        if (value == null) {
            return;
        }
        this.value = value.clone();
    }
    
    public double getValue(final int index) {
        return this.value[index];
    }
    
    public void setValue(final int index, final double value) {
        this.value[index] = value;
    }
    
    public int length() {
        return this.value.length;
    }
    
    @Override
    public void read(final DataInputStream in) throws IOException {
        this.value = new double[in.readInt()];
        for (int index = 0; index < this.value.length; ++index) {
            this.value[index] = in.readDouble();
        }
    }
    
    @Override
    public void write(final DataOutputStream out) throws IOException {
        out.writeInt(this.value.length);
        for (int index = 0; index < this.value.length; ++index) {
            out.writeDouble(this.value[index]);
        }
    }
    
    @Override
    public DoubleArrayTag clone() {
        return new DoubleArrayTag(this.getName(), this.getValue());
    }
}
