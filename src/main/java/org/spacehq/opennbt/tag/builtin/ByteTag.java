// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.opennbt.tag.builtin;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.DataInputStream;

public class ByteTag extends Tag
{
    private byte value;
    
    public ByteTag(final String name) {
        this(name, (byte)0);
    }
    
    public ByteTag(final String name, final byte value) {
        super(name);
        this.value = value;
    }
    
    @Override
    public Byte getValue() {
        return this.value;
    }
    
    public void setValue(final byte value) {
        this.value = value;
    }
    
    @Override
    public void read(final DataInputStream in) throws IOException {
        this.value = in.readByte();
    }
    
    @Override
    public void write(final DataOutputStream out) throws IOException {
        out.writeByte(this.value);
    }
    
    @Override
    public ByteTag clone() {
        return new ByteTag(this.getName(), this.getValue());
    }
}
