// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.opennbt.conversion.builtin;

import org.spacehq.opennbt.tag.builtin.Tag;
import org.spacehq.opennbt.tag.builtin.DoubleTag;
import org.spacehq.opennbt.conversion.TagConverter;

public class DoubleTagConverter implements TagConverter<DoubleTag, Double>
{
    @Override
    public Double convert(final DoubleTag tag) {
        return tag.getValue();
    }
    
    @Override
    public DoubleTag convert(final String name, final Double value) {
        return new DoubleTag(name, value);
    }
}
