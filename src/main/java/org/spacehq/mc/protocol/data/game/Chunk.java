// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.protocol.data.game;

public class Chunk
{
    private ShortArray3d blocks;
    private NibbleArray3d blocklight;
    private NibbleArray3d skylight;
    
    public Chunk(final boolean skylight) {
        this(new ShortArray3d(4096), new NibbleArray3d(4096), skylight ? new NibbleArray3d(4096) : null);
    }
    
    public Chunk(final ShortArray3d blocks, final NibbleArray3d blocklight, final NibbleArray3d skylight) {
        this.blocks = blocks;
        this.blocklight = blocklight;
        this.skylight = skylight;
    }
    
    public ShortArray3d getBlocks() {
        return this.blocks;
    }
    
    public NibbleArray3d getBlockLight() {
        return this.blocklight;
    }
    
    public NibbleArray3d getSkyLight() {
        return this.skylight;
    }
    
    public boolean isEmpty() {
        for (final short block : this.blocks.getData()) {
            if (block != 0) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final Chunk chunk = (Chunk)o;
        if (!this.blocklight.equals(chunk.blocklight)) {
            return false;
        }
        if (!this.blocks.equals(chunk.blocks)) {
            return false;
        }
        if (this.skylight != null) {
            if (this.skylight.equals(chunk.skylight)) {
                return true;
            }
        }
        else if (chunk.skylight == null) {
            return true;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        int result = this.blocks.hashCode();
        result = 31 * result + this.blocklight.hashCode();
        result = 31 * result + ((this.skylight != null) ? this.skylight.hashCode() : 0);
        return result;
    }
}
