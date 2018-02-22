// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.protocol.data.game.values.world.block.value;

public class GenericBlockValue implements BlockValue
{
    private int value;
    
    public GenericBlockValue(final int value) {
        this.value = value;
    }
    
    public int getValue() {
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
        final GenericBlockValue that = (GenericBlockValue)o;
        return this.value == that.value;
    }
    
    @Override
    public int hashCode() {
        return this.value;
    }
}
