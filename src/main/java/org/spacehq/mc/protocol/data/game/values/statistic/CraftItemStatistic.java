// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.protocol.data.game.values.statistic;

public class CraftItemStatistic implements Statistic
{
    private int id;
    
    public CraftItemStatistic(final int id) {
        this.id = id;
    }
    
    public int getId() {
        return this.id;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final CraftItemStatistic that = (CraftItemStatistic)o;
        return this.id == that.id;
    }
    
    @Override
    public int hashCode() {
        return this.id;
    }
}
