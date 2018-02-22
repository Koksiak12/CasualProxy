// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.protocol.data.game.values.world.effect;

public class HardLandingEffectData implements WorldEffectData
{
    private int damagingDistance;
    
    public HardLandingEffectData(final int damagingDistance) {
        this.damagingDistance = damagingDistance;
    }
    
    public int getDamagingDistance() {
        return this.damagingDistance;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final HardLandingEffectData that = (HardLandingEffectData)o;
        return this.damagingDistance == that.damagingDistance;
    }
    
    @Override
    public int hashCode() {
        return this.damagingDistance;
    }
}
