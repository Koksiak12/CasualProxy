// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.opennbt.tag.builtin.custom;

import java.io.OutputStream;
import java.io.ObjectOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.DataInputStream;
import java.io.Serializable;
import org.spacehq.opennbt.tag.builtin.Tag;

public class SerializableTag extends Tag
{
    private Serializable value;
    
    public SerializableTag(final String name) {
        this(name, 0);
    }
    
    public SerializableTag(final String name, final Serializable value) {
        super(name);
        this.value = value;
    }
    
    @Override
    public Serializable getValue() {
        return this.value;
    }
    
    public void setValue(final Serializable value) {
        this.value = value;
    }
    
    @Override
    public void read(final DataInputStream in) throws IOException {
        final ObjectInputStream str = new ObjectInputStream(in);
        try {
            this.value = (Serializable)str.readObject();
        }
        catch (ClassNotFoundException e) {
            throw new IOException("Class not found while reading SerializableTag!", e);
        }
    }
    
    @Override
    public void write(final DataOutputStream out) throws IOException {
        final ObjectOutputStream str = new ObjectOutputStream(out);
        str.writeObject(this.value);
    }
    
    @Override
    public SerializableTag clone() {
        return new SerializableTag(this.getName(), this.getValue());
    }
}
