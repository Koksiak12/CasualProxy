// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.packetlib.io;

import java.util.UUID;
import java.io.IOException;

public interface NetOutput
{
    void writeBoolean(final boolean p0) throws IOException;
    
    void writeByte(final int p0) throws IOException;
    
    void writeShort(final int p0) throws IOException;
    
    void writeChar(final int p0) throws IOException;
    
    void writeInt(final int p0) throws IOException;
    
    void writeVarInt(final int p0) throws IOException;
    
    void writeLong(final long p0) throws IOException;
    
    void writeVarLong(final long p0) throws IOException;
    
    void writeFloat(final float p0) throws IOException;
    
    void writeDouble(final double p0) throws IOException;
    
    void writeBytes(final byte[] p0) throws IOException;
    
    void writeBytes(final byte[] p0, final int p1) throws IOException;
    
    void writeShorts(final short[] p0) throws IOException;
    
    void writeShorts(final short[] p0, final int p1) throws IOException;
    
    void writeInts(final int[] p0) throws IOException;
    
    void writeInts(final int[] p0, final int p1) throws IOException;
    
    void writeLongs(final long[] p0) throws IOException;
    
    void writeLongs(final long[] p0, final int p1) throws IOException;
    
    void writeString(final String p0) throws IOException;
    
    void writeUUID(final UUID p0) throws IOException;
    
    void flush() throws IOException;
}
