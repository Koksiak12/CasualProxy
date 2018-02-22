// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.opennbt.tag.builtin;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.DataInputStream;

public class IntTag extends Tag
{
    private int value;
    
    public IntTag(final String name) {
        this(name, 0);
    }
    
    public IntTag(final String name, final int value) {
        super(name);
        this.value = value;
    }
    
    @Override
    public Integer getValue() {
        return this.value;
    }
    
    public void setValue(final int value) {
        this.value = value;
    }
    
    @Override
    public void read(final DataInputStream in) throws IOException {
        this.value = in.readInt();
    }
    
    @Override
    public void write(final DataOutputStream out) throws IOException {
        out.writeInt(this.value);
    }
    
    @Override
    public IntTag clone() {
        return new IntTag(this.getName(), this.getValue());
    }
}
