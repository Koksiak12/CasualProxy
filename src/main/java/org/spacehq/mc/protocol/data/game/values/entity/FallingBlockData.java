// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.protocol.data.game.values.entity;

public class FallingBlockData implements ObjectData
{
    private int id;
    private int metadata;
    
    public FallingBlockData(final int id, final int metadata) {
        this.id = id;
        this.metadata = metadata;
    }
    
    public int getId() {
        return this.id;
    }
    
    public int getMetadata() {
        return this.metadata;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final FallingBlockData that = (FallingBlockData)o;
        return this.id == that.id && this.metadata == that.metadata;
    }
    
    @Override
    public int hashCode() {
        int result = this.id;
        result = 31 * result + this.metadata;
        return result;
    }
}
