// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.opennbt.conversion.builtin;

import org.spacehq.opennbt.tag.builtin.Tag;
import org.spacehq.opennbt.tag.builtin.FloatTag;
import org.spacehq.opennbt.conversion.TagConverter;

public class FloatTagConverter implements TagConverter<FloatTag, Float>
{
    @Override
    public Float convert(final FloatTag tag) {
        return tag.getValue();
    }
    
    @Override
    public FloatTag convert(final String name, final Float value) {
        return new FloatTag(name, value);
    }
}
