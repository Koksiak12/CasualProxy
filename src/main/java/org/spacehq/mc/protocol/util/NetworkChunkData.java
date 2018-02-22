// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.protocol.util;

public class NetworkChunkData
{
    private int mask;
    private boolean fullChunk;
    private boolean sky;
    private byte[] data;
    
    public NetworkChunkData(final int mask, final boolean fullChunk, final boolean sky, final byte[] data) {
        this.mask = mask;
        this.fullChunk = fullChunk;
        this.sky = sky;
        this.data = data;
    }
    
    public int getMask() {
        return this.mask;
    }
    
    public boolean isFullChunk() {
        return this.fullChunk;
    }
    
    public boolean hasSkyLight() {
        return this.sky;
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
        final NetworkChunkData that = (NetworkChunkData)o;
        return this.fullChunk == that.fullChunk && this.mask == that.mask && this.sky == that.sky;
    }
    
    @Override
    public int hashCode() {
        int result = this.mask;
        result = 31 * result + (this.fullChunk ? 1 : 0);
        result = 31 * result + (this.sky ? 1 : 0);
        return result;
    }
}
