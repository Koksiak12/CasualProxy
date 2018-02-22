// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.protocol.data.game.values.world.effect;

public class RecordEffectData implements WorldEffectData
{
    private int recordId;
    
    public RecordEffectData(final int recordId) {
        this.recordId = recordId;
    }
    
    public int getRecordId() {
        return this.recordId;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final RecordEffectData that = (RecordEffectData)o;
        return this.recordId == that.recordId;
    }
    
    @Override
    public int hashCode() {
        return this.recordId;
    }
}
