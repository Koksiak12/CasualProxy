// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.protocol.data.game.values.world.effect;

public class BreakBlockEffectData implements WorldEffectData
{
    private int blockId;
    
    public BreakBlockEffectData(final int blockId) {
        this.blockId = blockId;
    }
    
    public int getBlockId() {
        return this.blockId;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final BreakBlockEffectData that = (BreakBlockEffectData)o;
        return this.blockId == that.blockId;
    }
    
    @Override
    public int hashCode() {
        return this.blockId;
    }
}
