// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.opennbt.conversion.builtin;

import org.spacehq.opennbt.tag.builtin.Tag;
import org.spacehq.opennbt.tag.builtin.ShortTag;
import org.spacehq.opennbt.conversion.TagConverter;

public class ShortTagConverter implements TagConverter<ShortTag, Short>
{
    @Override
    public Short convert(final ShortTag tag) {
        return tag.getValue();
    }
    
    @Override
    public ShortTag convert(final String name, final Short value) {
        return new ShortTag(name, value);
    }
}
