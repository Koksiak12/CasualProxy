// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.protocol.data.game.values.world.block.value;

public class ChestValue implements BlockValue
{
    private int viewers;
    
    public ChestValue(final int viewers) {
        this.viewers = viewers;
    }
    
    public int getViewers() {
        return this.viewers;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final ChestValue that = (ChestValue)o;
        return this.viewers == that.viewers;
    }
    
    @Override
    public int hashCode() {
        return this.viewers;
    }
}
