// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.protocol.data.game;

import org.spacehq.mc.protocol.data.game.values.entity.MetadataType;

public class EntityMetadata
{
    private int id;
    private MetadataType type;
    private Object value;
    
    public EntityMetadata(final int id, final MetadataType type, final Object value) {
        this.id = id;
        this.type = type;
        this.value = value;
    }
    
    public int getId() {
        return this.id;
    }
    
    public MetadataType getType() {
        return this.type;
    }
    
    public Object getValue() {
        return this.value;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final EntityMetadata metadata = (EntityMetadata)o;
        return this.id == metadata.id && this.type == metadata.type && this.value.equals(metadata.value);
    }
    
    @Override
    public int hashCode() {
        int result = this.id;
        result = 31 * result + this.type.hashCode();
        result = 31 * result + this.value.hashCode();
        return result;
    }
}
