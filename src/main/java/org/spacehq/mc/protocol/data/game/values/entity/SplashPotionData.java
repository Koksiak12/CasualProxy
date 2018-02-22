// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.protocol.data.game.values.entity;

public class SplashPotionData implements ObjectData
{
    private int potionData;
    
    public SplashPotionData(final int potionData) {
        this.potionData = potionData;
    }
    
    public int getPotionData() {
        return this.potionData;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final SplashPotionData that = (SplashPotionData)o;
        return this.potionData == that.potionData;
    }
    
    @Override
    public int hashCode() {
        return this.potionData;
    }
}
