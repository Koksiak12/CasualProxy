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

public class SerializableArrayTag extends Tag
{
    private Serializable[] value;
    
    public SerializableArrayTag(final String name) {
        this(name, new Serializable[0]);
    }
    
    public SerializableArrayTag(final String name, final Serializable[] value) {
        super(name);
        this.value = value;
    }
    
    @Override
    public Serializable[] getValue() {
        return this.value.clone();
    }
    
    public void setValue(final Serializable[] value) {
        if (value == null) {
            return;
        }
        this.value = value.clone();
    }
    
    public Serializable getValue(final int index) {
        return this.value[index];
    }
    
    public void setValue(final int index, final Serializable value) {
        this.value[index] = value;
    }
    
    public int length() {
        return this.value.length;
    }
    
    @Override
    public void read(final DataInputStream in) throws IOException {
        this.value = new Serializable[in.readInt()];
        final ObjectInputStream str = new ObjectInputStream(in);
        for (int index = 0; index < this.value.length; ++index) {
            try {
                this.value[index] = (Serializable)str.readObject();
            }
            catch (ClassNotFoundException e) {
                throw new IOException("Class not found while reading SerializableArrayTag!", e);
            }
        }
    }
    
    @Override
    public void write(final DataOutputStream out) throws IOException {
        out.writeInt(this.value.length);
        final ObjectOutputStream str = new ObjectOutputStream(out);
        for (int index = 0; index < this.value.length; ++index) {
            str.writeObject(this.value[index]);
        }
    }
    
    @Override
    public SerializableArrayTag clone() {
        return new SerializableArrayTag(this.getName(), this.getValue());
    }
}
