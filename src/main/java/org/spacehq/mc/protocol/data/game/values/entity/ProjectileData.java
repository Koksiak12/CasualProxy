// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.protocol.data.game.values.entity;

public class ProjectileData implements ObjectData
{
    private int ownerId;
    
    public ProjectileData(final int ownerId) {
        this.ownerId = ownerId;
    }
    
    public int getOwnerId() {
        return this.ownerId;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final ProjectileData that = (ProjectileData)o;
        return this.ownerId == that.ownerId;
    }
    
    @Override
    public int hashCode() {
        return this.ownerId;
    }
}
