// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.protocol.data.game.values.world.block.value;

public class NoteBlockValue implements BlockValue
{
    private int pitch;
    
    public NoteBlockValue(final int pitch) {
        if (pitch < 0 || pitch > 24) {
            throw new IllegalArgumentException("Pitch must be between 0 and 24.");
        }
        this.pitch = pitch;
    }
    
    public int getPitch() {
        return this.pitch;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final NoteBlockValue that = (NoteBlockValue)o;
        return this.pitch == that.pitch;
    }
    
    @Override
    public int hashCode() {
        return this.pitch;
    }
}
