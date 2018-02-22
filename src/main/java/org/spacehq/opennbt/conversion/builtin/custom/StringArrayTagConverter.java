// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.opennbt.conversion.builtin.custom;

import org.spacehq.opennbt.tag.builtin.Tag;
import org.spacehq.opennbt.tag.builtin.custom.StringArrayTag;
import org.spacehq.opennbt.conversion.TagConverter;

public class StringArrayTagConverter implements TagConverter<StringArrayTag, String[]>
{
    @Override
    public String[] convert(final StringArrayTag tag) {
        return tag.getValue();
    }
    
    @Override
    public StringArrayTag convert(final String name, final String[] value) {
        return new StringArrayTag(name, value);
    }
}
