// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.opennbt.conversion.builtin.custom;

import org.spacehq.opennbt.tag.builtin.Tag;
import java.io.Serializable;
import org.spacehq.opennbt.tag.builtin.custom.SerializableArrayTag;
import org.spacehq.opennbt.conversion.TagConverter;

public class SerializableArrayTagConverter implements TagConverter<SerializableArrayTag, Serializable[]>
{
    @Override
    public Serializable[] convert(final SerializableArrayTag tag) {
        return tag.getValue();
    }
    
    @Override
    public SerializableArrayTag convert(final String name, final Serializable[] value) {
        return new SerializableArrayTag(name, value);
    }
}
