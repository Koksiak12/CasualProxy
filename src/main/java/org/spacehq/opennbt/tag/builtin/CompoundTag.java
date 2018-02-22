// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.opennbt.tag.builtin;

import java.io.DataOutputStream;
import java.util.List;
import java.io.EOFException;
import java.io.IOException;
import org.spacehq.opennbt.NBTIO;
import java.util.ArrayList;
import java.io.DataInputStream;
import java.util.Iterator;
import java.util.Collection;
import java.util.Set;
import java.util.LinkedHashMap;
import java.util.Map;

public class CompoundTag extends Tag implements Iterable<Tag>
{
    private Map<String, Tag> value;
    
    public CompoundTag(final String name) {
        this(name, new LinkedHashMap<String, Tag>());
    }
    
    public CompoundTag(final String name, final Map<String, Tag> value) {
        super(name);
        this.value = new LinkedHashMap<String, Tag>(value);
    }
    
    @Override
    public Map<String, Tag> getValue() {
        return new LinkedHashMap<String, Tag>(this.value);
    }
    
    public void setValue(final Map<String, Tag> value) {
        this.value = new LinkedHashMap<String, Tag>(value);
    }
    
    public boolean isEmpty() {
        return this.value.isEmpty();
    }
    
    public boolean contains(final String tagName) {
        return this.value.containsKey(tagName);
    }
    
    public <T extends Tag> T get(final String tagName) {
        return (T)this.value.get(tagName);
    }
    
    public <T extends Tag> T put(final T tag) {
        return (T)this.value.put(tag.getName(), tag);
    }
    
    public <T extends Tag> T remove(final String tagName) {
        return (T)this.value.remove(tagName);
    }
    
    public Set<String> keySet() {
        return this.value.keySet();
    }
    
    public Collection<Tag> values() {
        return this.value.values();
    }
    
    public int size() {
        return this.value.size();
    }
    
    public void clear() {
        this.value.clear();
    }
    
    @Override
    public Iterator<Tag> iterator() {
        return this.values().iterator();
    }
    
    @Override
    public void read(final DataInputStream in) throws IOException {
        final List<Tag> tags = new ArrayList<Tag>();
        try {
            Tag tag;
            while ((tag = NBTIO.readTag(in)) != null) {
                tags.add(tag);
            }
        }
        catch (EOFException e) {
            throw new IOException("Closing EndTag was not found!");
        }
        for (final Tag tag2 : tags) {
            this.put(tag2);
        }
    }
    
    @Override
    public void write(final DataOutputStream out) throws IOException {
        for (final Tag tag : this.value.values()) {
            NBTIO.writeTag(out, tag);
        }
        out.writeByte(0);
    }
    
    @Override
    public CompoundTag clone() {
        final Map<String, Tag> newMap = new LinkedHashMap<String, Tag>();
        for (final Map.Entry<String, Tag> entry : this.value.entrySet()) {
            newMap.put(entry.getKey(), entry.getValue().clone());
        }
        return new CompoundTag(this.getName(), newMap);
    }
}
