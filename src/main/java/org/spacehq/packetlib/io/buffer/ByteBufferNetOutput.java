// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.packetlib.io.buffer;

import java.util.UUID;
import java.io.IOException;
import java.nio.ByteBuffer;
import org.spacehq.packetlib.io.NetOutput;

public class ByteBufferNetOutput implements NetOutput
{
    private ByteBuffer buffer;
    
    public ByteBufferNetOutput(final ByteBuffer buffer) {
        this.buffer = buffer;
    }
    
    public ByteBuffer getByteBuffer() {
        return this.buffer;
    }
    
    @Override
    public void writeBoolean(final boolean b) throws IOException {
        this.buffer.put((byte)(b ? 1 : 0));
    }
    
    @Override
    public void writeByte(final int b) throws IOException {
        this.buffer.put((byte)b);
    }
    
    @Override
    public void writeShort(final int s) throws IOException {
        this.buffer.putShort((short)s);
    }
    
    @Override
    public void writeChar(final int c) throws IOException {
        this.buffer.putChar((char)c);
    }
    
    @Override
    public void writeInt(final int i) throws IOException {
        this.buffer.putInt(i);
    }
    
    @Override
    public void writeVarInt(int i) throws IOException {
        while ((i & 0xFFFFFF80) != 0x0) {
            this.writeByte((i & 0x7F) | 0x80);
            i >>>= 7;
        }
        this.writeByte(i);
    }
    
    @Override
    public void writeLong(final long l) throws IOException {
        this.buffer.putLong(l);
    }
    
    @Override
    public void writeVarLong(long l) throws IOException {
        while ((l & 0xFFFFFFFFFFFFFF80L) != 0x0L) {
            this.writeByte((int)(l & 0x7FL) | 0x80);
            l >>>= 7;
        }
        this.writeByte((int)l);
    }
    
    @Override
    public void writeFloat(final float f) throws IOException {
        this.buffer.putFloat(f);
    }
    
    @Override
    public void writeDouble(final double d) throws IOException {
        this.buffer.putDouble(d);
    }
    
    @Override
    public void writeBytes(final byte[] b) throws IOException {
        this.buffer.put(b);
    }
    
    @Override
    public void writeBytes(final byte[] b, final int length) throws IOException {
        this.buffer.put(b, 0, length);
    }
    
    @Override
    public void writeShorts(final short[] s) throws IOException {
        this.writeShorts(s, s.length);
    }
    
    @Override
    public void writeShorts(final short[] s, final int length) throws IOException {
        for (int index = 0; index < length; ++index) {
            this.writeShort(s[index]);
        }
    }
    
    @Override
    public void writeInts(final int[] i) throws IOException {
        this.writeInts(i, i.length);
    }
    
    @Override
    public void writeInts(final int[] i, final int length) throws IOException {
        for (int index = 0; index < length; ++index) {
            this.writeInt(i[index]);
        }
    }
    
    @Override
    public void writeLongs(final long[] l) throws IOException {
        this.writeLongs(l, l.length);
    }
    
    @Override
    public void writeLongs(final long[] l, final int length) throws IOException {
        for (int index = 0; index < length; ++index) {
            this.writeLong(l[index]);
        }
    }
    
    @Override
    public void writeString(final String s) throws IOException {
        if (s == null) {
            throw new IllegalArgumentException("String cannot be null!");
        }
        final byte[] bytes = s.getBytes("UTF-8");
        if (bytes.length > 32767) {
            throw new IOException("String too big (was " + s.length() + " bytes encoded, max " + 32767 + ")");
        }
        this.writeVarInt(bytes.length);
        this.writeBytes(bytes);
    }
    
    @Override
    public void writeUUID(final UUID uuid) throws IOException {
        this.writeLong(uuid.getMostSignificantBits());
        this.writeLong(uuid.getLeastSignificantBits());
    }
    
    @Override
    public void flush() throws IOException {
    }
}
