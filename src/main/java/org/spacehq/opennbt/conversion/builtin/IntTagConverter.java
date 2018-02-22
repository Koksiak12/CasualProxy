// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.opennbt.conversion.builtin;

import org.spacehq.opennbt.tag.builtin.Tag;
import org.spacehq.opennbt.tag.builtin.IntTag;
import org.spacehq.opennbt.conversion.TagConverter;

public class IntTagConverter implements TagConverter<IntTag, Integer>
{
    @Override
    public Integer convert(final IntTag tag) {
        return tag.getValue();
    }
    
    @Override
    public IntTag convert(final String name, final Integer value) {
        return new IntTag(name, value);
    }
}
