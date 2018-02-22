// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.opennbt.conversion.builtin.custom;

import org.spacehq.opennbt.tag.builtin.Tag;
import org.spacehq.opennbt.tag.builtin.custom.FloatArrayTag;
import org.spacehq.opennbt.conversion.TagConverter;

public class FloatArrayTagConverter implements TagConverter<FloatArrayTag, float[]>
{
    @Override
    public float[] convert(final FloatArrayTag tag) {
        return tag.getValue();
    }
    
    @Override
    public FloatArrayTag convert(final String name, final float[] value) {
        return new FloatArrayTag(name, value);
    }
}
