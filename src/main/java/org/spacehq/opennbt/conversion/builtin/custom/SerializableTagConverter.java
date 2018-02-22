// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.opennbt.conversion.builtin.custom;

import org.spacehq.opennbt.tag.builtin.Tag;
import java.io.Serializable;
import org.spacehq.opennbt.tag.builtin.custom.SerializableTag;
import org.spacehq.opennbt.conversion.TagConverter;

public class SerializableTagConverter implements TagConverter<SerializableTag, Serializable>
{
    @Override
    public Serializable convert(final SerializableTag tag) {
        return tag.getValue();
    }
    
    @Override
    public SerializableTag convert(final String name, final Serializable value) {
        return new SerializableTag(name, value);
    }
}
