// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.opennbt.conversion.builtin;

import java.util.Iterator;
import org.spacehq.opennbt.conversion.ConverterRegistry;
import org.spacehq.opennbt.tag.builtin.Tag;
import java.util.ArrayList;
import java.util.List;
import org.spacehq.opennbt.tag.builtin.ListTag;
import org.spacehq.opennbt.conversion.TagConverter;

public class ListTagConverter implements TagConverter<ListTag, List>
{
    @Override
    public List convert(final ListTag tag) {
        final List<Object> ret = new ArrayList<Object>();
        final List<? extends Tag> tags = tag.getValue();
        for (final Tag t : tags) {
            ret.add(ConverterRegistry.convertToValue(t));
        }
        return ret;
    }
    
    @Override
    public ListTag convert(final String name, final List value) {
        if (value.isEmpty()) {
            throw new IllegalArgumentException("Cannot convert ListTag with size of 0.");
        }
        final List<Tag> tags = new ArrayList<Tag>();
        for (final Object o : value) {
            tags.add(ConverterRegistry.convertToTag("", o));
        }
        return new ListTag(name, tags);
    }
}
