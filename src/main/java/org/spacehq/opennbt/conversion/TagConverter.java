// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.opennbt.conversion;

import org.spacehq.opennbt.tag.builtin.Tag;

public interface TagConverter<T extends Tag, V>
{
    V convert(final T p0);
    
    T convert(final String p0, final V p1);
}
