// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.opennbt.tag.builtin.custom;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.DataInputStream;
import org.spacehq.opennbt.tag.builtin.Tag;

public class LongArrayTag extends Tag
{
    private long[] value;
    
    public LongArrayTag(final String name) {
        this(name, new long[0]);
    }
    
    public LongArrayTag(final String name, final long[] value) {
        super(name);
        this.value = value;
    }
    
    @Override
    public long[] getValue() {
        return this.value.clone();
    }
    
    public void setValue(final long[] value) {
        if (value == null) {
            return;
        }
        this.value = value.clone();
    }
    
    public long getValue(final int index) {
        return this.value[index];
    }
    
    public void setValue(final int index, final long value) {
        this.value[index] = value;
    }
    
    public int length() {
        return this.value.length;
    }
    
    @Override
    public void read(final DataInputStream in) throws IOException {
        this.value = new long[in.readInt()];
        for (int index = 0; index < this.value.length; ++index) {
            this.value[index] = in.readLong();
        }
    }
    
    @Override
    public void write(final DataOutputStream out) throws IOException {
        out.writeInt(this.value.length);
        for (int index = 0; index < this.value.length; ++index) {
            out.writeLong(this.value[index]);
        }
    }
    
    @Override
    public LongArrayTag clone() {
        return new LongArrayTag(this.getName(), this.getValue());
    }
}
