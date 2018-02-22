// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.protocol.data.game.values.world.map;

public class MapPlayer
{
    private int centerX;
    private int centerZ;
    private int iconSize;
    private int iconRotation;
    
    public MapPlayer(final int centerX, final int centerZ, final int iconSize, final int iconRotation) {
        this.centerX = centerX;
        this.centerZ = centerZ;
        this.iconSize = iconSize;
        this.iconRotation = iconRotation;
    }
    
    public int getCenterX() {
        return this.centerX;
    }
    
    public int getCenterZ() {
        return this.centerZ;
    }
    
    public int getIconSize() {
        return this.iconSize;
    }
    
    public int getIconRotation() {
        return this.iconRotation;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final MapPlayer mapPlayer = (MapPlayer)o;
        return this.centerX == mapPlayer.centerX && this.centerZ == mapPlayer.centerZ && this.iconRotation == mapPlayer.iconRotation && this.iconSize == mapPlayer.iconSize;
    }
    
    @Override
    public int hashCode() {
        int result = this.centerX;
        result = 31 * result + this.centerZ;
        result = 31 * result + this.iconSize;
        result = 31 * result + this.iconRotation;
        return result;
    }
}
