// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.protocol.util;

import java.util.Arrays;
import org.spacehq.mc.protocol.data.game.Chunk;

public class ParsedChunkData
{
    private Chunk[] chunks;
    private byte[] biomes;
    
    public ParsedChunkData(final Chunk[] chunks, final byte[] biomes) {
        this.chunks = chunks;
        this.biomes = biomes;
    }
    
    public Chunk[] getChunks() {
        return this.chunks;
    }
    
    public byte[] getBiomes() {
        return this.biomes;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final ParsedChunkData that = (ParsedChunkData)o;
        return Arrays.equals(this.biomes, that.biomes) && Arrays.equals(this.chunks, that.chunks);
    }
    
    @Override
    public int hashCode() {
        int result = Arrays.hashCode(this.chunks);
        result = 31 * result + ((this.biomes != null) ? Arrays.hashCode(this.biomes) : 0);
        return result;
    }
}
