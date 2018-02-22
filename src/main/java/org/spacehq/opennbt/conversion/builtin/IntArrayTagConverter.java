// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.opennbt.conversion.builtin;

import org.spacehq.opennbt.tag.builtin.Tag;
import org.spacehq.opennbt.tag.builtin.IntArrayTag;
import org.spacehq.opennbt.conversion.TagConverter;

public class IntArrayTagConverter implements TagConverter<IntArrayTag, int[]>
{
    @Override
    public int[] convert(final IntArrayTag tag) {
        return tag.getValue();
    }
    
    @Override
    public IntArrayTag convert(final String name, final int[] value) {
        return new IntArrayTag(name, value);
    }
}
