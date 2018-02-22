// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.opennbt.tag.builtin;

import java.io.DataOutputStream;
import org.spacehq.opennbt.tag.TagCreateException;
import java.io.IOException;
import org.spacehq.opennbt.tag.TagRegistry;
import java.io.DataInputStream;
import java.util.Iterator;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;

public class ListTag extends Tag implements Iterable<Tag>
{
    private Class<? extends Tag> type;
    private List<Tag> value;
    
    private ListTag(final String name) {
        super(name);
    }
    
    public ListTag(final String name, final Class<? extends Tag> type) {
        super(name);
        this.type = type;
        this.value = new ArrayList<Tag>();
    }
    
    public ListTag(final String name, final List<Tag> value) throws IllegalArgumentException {
        super(name);
        Class<? extends Tag> type = null;
        for (final Tag tag : value) {
            if (tag == null) {
                throw new IllegalArgumentException("List cannot contain null tags.");
            }
            if (type == null) {
                type = tag.getClass();
            }
            else {
                if (tag.getClass() != type) {
                    throw new IllegalArgumentException("All tags must be of the same type.");
                }
                continue;
            }
        }
        this.type = type;
        this.value = new ArrayList<Tag>(value);
    }
    
    @Override
    public List<Tag> getValue() {
        return new ArrayList<Tag>(this.value);
    }
    
    public void setValue(final List<Tag> value) {
        for (final Tag tag : value) {
            if (tag.getClass() != this.type) {
                throw new IllegalArgumentException("Tag type cannot differ from ListTag type.");
            }
        }
        this.value = new ArrayList<Tag>(value);
    }
    
    public Class<? extends Tag> getElementType() {
        return this.type;
    }
    
    public boolean add(final Tag tag) {
        if (tag.getClass() != this.type) {
            throw new IllegalArgumentException("Tag type cannot differ from ListTag type.");
        }
        return this.value.add(tag);
    }
    
    public boolean remove(final Tag tag) {
        return this.value.remove(tag);
    }
    
    public <T extends Tag> T get(final int index) {
        return (T)this.value.get(index);
    }
    
    public int size() {
        return this.value.size();
    }
    
    @Override
    public Iterator<Tag> iterator() {
        return this.value.iterator();
    }
    
    @Override
    public void read(final DataInputStream in) throws IOException {
        final int id = in.readUnsignedByte();
        this.type = TagRegistry.getClassFor(id);
        this.value = new ArrayList<Tag>();
        if (id != 0 && this.type == null) {
            throw new IOException("Unknown tag ID in ListTag: " + id);
        }
        for (int count = in.readInt(), index = 0; index < count; ++index) {
            Tag tag = null;
            try {
                tag = TagRegistry.createInstance(id, "");
            }
            catch (TagCreateException e) {
                throw new IOException("Failed to create tag.", e);
            }
            tag.read(in);
            this.add(tag);
        }
    }
    
    @Override
    public void write(final DataOutputStream out) throws IOException {
        if (this.value.isEmpty()) {
            out.writeByte(0);
        }
        else {
            final int id = TagRegistry.getIdFor(this.type);
            if (id == -1) {
                throw new IOException("ListTag contains unregistered tag class.");
            }
            out.writeByte(id);
        }
        out.writeInt(this.value.size());
        for (final Tag tag : this.value) {
            tag.write(out);
        }
    }
    
    @Override
    public ListTag clone() {
        final List<Tag> newList = new ArrayList<Tag>();
        for (final Tag value : this.value) {
            newList.add(value.clone());
        }
        return new ListTag(this.getName(), newList);
    }
}
