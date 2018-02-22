// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.protocol.data.game.values.world.map;

import java.util.Arrays;

public class MapData
{
    private int columns;
    private int rows;
    private int x;
    private int y;
    private byte[] data;
    
    public MapData(final int columns, final int rows, final int x, final int y, final byte[] data) {
        this.columns = columns;
        this.rows = rows;
        this.x = x;
        this.y = y;
        this.data = data;
    }
    
    public int getColumns() {
        return this.columns;
    }
    
    public int getRows() {
        return this.rows;
    }
    
    public int getX() {
        return this.x;
    }
    
    public int getY() {
        return this.y;
    }
    
    public byte[] getData() {
        return this.data;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final MapData mapData = (MapData)o;
        return this.columns == mapData.columns && this.rows == mapData.rows && this.x == mapData.x && this.y == mapData.y && Arrays.equals(this.data, mapData.data);
    }
    
    @Override
    public int hashCode() {
        int result = this.columns;
        result = 31 * result + this.rows;
        result = 31 * result + this.x;
        result = 31 * result + this.y;
        result = 31 * result + Arrays.hashCode(this.data);
        return result;
    }
}
