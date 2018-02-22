// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.opennbt.conversion.builtin.custom;

import org.spacehq.opennbt.tag.builtin.Tag;
import org.spacehq.opennbt.tag.builtin.custom.LongArrayTag;
import org.spacehq.opennbt.conversion.TagConverter;

public class LongArrayTagConverter implements TagConverter<LongArrayTag, long[]>
{
    @Override
    public long[] convert(final LongArrayTag tag) {
        return tag.getValue();
    }
    
    @Override
    public LongArrayTag convert(final String name, final long[] value) {
        return new LongArrayTag(name, value);
    }
}
