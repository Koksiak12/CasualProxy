// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.opennbt.tag.builtin;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.DataInputStream;

public class StringTag extends Tag
{
    private String value;
    
    public StringTag(final String name) {
        this(name, "");
    }
    
    public StringTag(final String name, final String value) {
        super(name);
        this.value = value;
    }
    
    @Override
    public String getValue() {
        return this.value;
    }
    
    public void setValue(final String value) {
        this.value = value;
    }
    
    @Override
    public void read(final DataInputStream in) throws IOException {
        this.value = in.readUTF();
    }
    
    @Override
    public void write(final DataOutputStream out) throws IOException {
        out.writeUTF(this.value);
    }
    
    @Override
    public StringTag clone() {
        return new StringTag(this.getName(), this.getValue());
    }
}
