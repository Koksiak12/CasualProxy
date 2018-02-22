// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.opennbt.conversion.builtin.custom;

import org.spacehq.opennbt.tag.builtin.Tag;
import org.spacehq.opennbt.tag.builtin.custom.ShortArrayTag;
import org.spacehq.opennbt.conversion.TagConverter;

public class ShortArrayTagConverter implements TagConverter<ShortArrayTag, short[]>
{
    @Override
    public short[] convert(final ShortArrayTag tag) {
        return tag.getValue();
    }
    
    @Override
    public ShortArrayTag convert(final String name, final short[] value) {
        return new ShortArrayTag(name, value);
    }
}
