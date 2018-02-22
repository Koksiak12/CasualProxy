// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.packetlib.io;

import java.util.UUID;
import java.io.IOException;

public interface NetInput
{
    boolean readBoolean() throws IOException;
    
    byte readByte() throws IOException;
    
    int readUnsignedByte() throws IOException;
    
    short readShort() throws IOException;
    
    int readUnsignedShort() throws IOException;
    
    char readChar() throws IOException;
    
    int readInt() throws IOException;
    
    int readVarInt() throws IOException;
    
    long readLong() throws IOException;
    
    long readVarLong() throws IOException;
    
    float readFloat() throws IOException;
    
    double readDouble() throws IOException;
    
    byte[] readBytes(final int p0) throws IOException;
    
    int readBytes(final byte[] p0) throws IOException;
    
    int readBytes(final byte[] p0, final int p1, final int p2) throws IOException;
    
    short[] readShorts(final int p0) throws IOException;
    
    int readShorts(final short[] p0) throws IOException;
    
    int readShorts(final short[] p0, final int p1, final int p2) throws IOException;
    
    int[] readInts(final int p0) throws IOException;
    
    int readInts(final int[] p0) throws IOException;
    
    int readInts(final int[] p0, final int p1, final int p2) throws IOException;
    
    long[] readLongs(final int p0) throws IOException;
    
    int readLongs(final long[] p0) throws IOException;
    
    int readLongs(final long[] p0, final int p1, final int p2) throws IOException;
    
    String readString() throws IOException;
    
    UUID readUUID() throws IOException;
    
    int available() throws IOException;
}
