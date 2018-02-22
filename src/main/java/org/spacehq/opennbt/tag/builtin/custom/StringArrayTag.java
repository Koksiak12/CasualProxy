// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.opennbt.tag.builtin.custom;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.DataInputStream;
import org.spacehq.opennbt.tag.builtin.Tag;

public class StringArrayTag extends Tag
{
    private String[] value;
    
    public StringArrayTag(final String name) {
        this(name, new String[0]);
    }
    
    public StringArrayTag(final String name, final String[] value) {
        super(name);
        this.value = value;
    }
    
    @Override
    public String[] getValue() {
        return this.value.clone();
    }
    
    public void setValue(final String[] value) {
        if (value == null) {
            return;
        }
        this.value = value.clone();
    }
    
    public String getValue(final int index) {
        return this.value[index];
    }
    
    public void setValue(final int index, final String value) {
        this.value[index] = value;
    }
    
    public int length() {
        return this.value.length;
    }
    
    @Override
    public void read(final DataInputStream in) throws IOException {
        this.value = new String[in.readInt()];
        for (int index = 0; index < this.value.length; ++index) {
            this.value[index] = in.readUTF();
        }
    }
    
    @Override
    public void write(final DataOutputStream out) throws IOException {
        out.writeInt(this.value.length);
        for (int index = 0; index < this.value.length; ++index) {
            out.writeUTF(this.value[index]);
        }
    }
    
    @Override
    public StringArrayTag clone() {
        return new StringArrayTag(this.getName(), this.getValue());
    }
}
