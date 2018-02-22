// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.opennbt.conversion.builtin;

import org.spacehq.opennbt.tag.builtin.Tag;
import org.spacehq.opennbt.tag.builtin.LongTag;
import org.spacehq.opennbt.conversion.TagConverter;

public class LongTagConverter implements TagConverter<LongTag, Long>
{
    @Override
    public Long convert(final LongTag tag) {
        return tag.getValue();
    }
    
    @Override
    public LongTag convert(final String name, final Long value) {
        return new LongTag(name, value);
    }
}
