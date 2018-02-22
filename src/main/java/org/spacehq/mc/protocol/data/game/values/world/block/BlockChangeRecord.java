// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.protocol.data.game.values.world.block;

import org.spacehq.mc.protocol.data.game.Position;

public class BlockChangeRecord
{
    private Position position;
    private int id;
    private int data;
    
    public BlockChangeRecord(final Position position, final int id, final int data) {
        this.position = position;
        this.id = id;
        this.data = data;
    }
    
    public Position getPosition() {
        return this.position;
    }
    
    public int getId() {
        return this.id;
    }
    
    public int getData() {
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
        final BlockChangeRecord record = (BlockChangeRecord)o;
        return this.id == record.id && this.data == record.data && this.position.equals(record.position);
    }
    
    @Override
    public int hashCode() {
        int result = this.position.hashCode();
        result = 31 * result + this.id;
        result = 31 * result + this.data;
        return result;
    }
}
