// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.opennbt.conversion.builtin;

import org.spacehq.opennbt.tag.builtin.Tag;
import org.spacehq.opennbt.tag.builtin.StringTag;
import org.spacehq.opennbt.conversion.TagConverter;

public class StringTagConverter implements TagConverter<StringTag, String>
{
    @Override
    public String convert(final StringTag tag) {
        return tag.getValue();
    }
    
    @Override
    public StringTag convert(final String name, final String value) {
        return new StringTag(name, value);
    }
}
