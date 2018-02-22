// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.opennbt.conversion.builtin;

import org.spacehq.opennbt.tag.builtin.Tag;
import org.spacehq.opennbt.tag.builtin.ByteTag;
import org.spacehq.opennbt.conversion.TagConverter;

public class ByteTagConverter implements TagConverter<ByteTag, Byte>
{
    @Override
    public Byte convert(final ByteTag tag) {
        return tag.getValue();
    }
    
    @Override
    public ByteTag convert(final String name, final Byte value) {
        return new ByteTag(name, value);
    }
}
