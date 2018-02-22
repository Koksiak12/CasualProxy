// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.protocol.data.game.values.world.effect;

public class BreakPotionEffectData implements WorldEffectData
{
    private int potionId;
    
    public BreakPotionEffectData(final int potionId) {
        this.potionId = potionId;
    }
    
    public int getPotionId() {
        return this.potionId;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final BreakPotionEffectData that = (BreakPotionEffectData)o;
        return this.potionId == that.potionId;
    }
    
    @Override
    public int hashCode() {
        return this.potionId;
    }
}
