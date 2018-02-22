// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.protocol.data.game.values.world.block;

public class ExplodedBlockRecord
{
    private int x;
    private int y;
    private int z;
    
    public ExplodedBlockRecord(final int x, final int y, final int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public int getX() {
        return this.x;
    }
    
    public int getY() {
        return this.y;
    }
    
    public int getZ() {
        return this.z;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final ExplodedBlockRecord that = (ExplodedBlockRecord)o;
        return this.x == that.x && this.y == that.y && this.z == that.z;
    }
    
    @Override
    public int hashCode() {
        int result = this.x;
        result = 31 * result + this.y;
        result = 31 * result + this.z;
        return result;
    }
}
