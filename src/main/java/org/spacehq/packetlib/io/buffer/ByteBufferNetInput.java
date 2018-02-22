// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.packetlib.io.buffer;

import java.util.UUID;
import java.io.IOException;
import java.nio.ByteBuffer;
import org.spacehq.packetlib.io.NetInput;

public class ByteBufferNetInput implements NetInput
{
    private ByteBuffer buffer;
    
    public ByteBufferNetInput(final ByteBuffer buffer) {
        this.buffer = buffer;
    }
    
    public ByteBuffer getByteBuffer() {
        return this.buffer;
    }
    
    @Override
    public boolean readBoolean() throws IOException {
        return this.buffer.get() == 1;
    }
    
    @Override
    public byte readByte() throws IOException {
        return this.buffer.get();
    }
    
    @Override
    public int readUnsignedByte() throws IOException {
        return this.buffer.get() & 0xFF;
    }
    
    @Override
    public short readShort() throws IOException {
        return this.buffer.getShort();
    }
    
    @Override
    public int readUnsignedShort() throws IOException {
        return this.buffer.getShort() & 0xFFFF;
    }
    
    @Override
    public char readChar() throws IOException {
        return this.buffer.getChar();
    }
    
    @Override
    public int readInt() throws IOException {
        return this.buffer.getInt();
    }
    
    @Override
    public int readVarInt() throws IOException {
        int value = 0;
        int size = 0;
        int b;
        while (((b = this.readByte()) & 0x80) == 0x80) {
            value |= (b & 0x7F) << size++ * 7;
            if (size > 5) {
                throw new IOException("VarInt too long (length must be <= 5)");
            }
        }
        return value | (b & 0x7F) << size * 7;
    }
    
    @Override
    public long readLong() throws IOException {
        return this.buffer.getLong();
    }
    
    @Override
    public long readVarLong() throws IOException {
        int value = 0;
        int size = 0;
        int b;
        while (((b = this.readByte()) & 0x80) == 0x80) {
            value |= (b & 0x7F) << size++ * 7;
            if (size > 10) {
                throw new IOException("VarLong too long (length must be <= 10)");
            }
        }
        return value | (b & 0x7F) << size * 7;
    }
    
    @Override
    public float readFloat() throws IOException {
        return this.buffer.getFloat();
    }
    
    @Override
    public double readDouble() throws IOException {
        return this.buffer.getDouble();
    }
    
    @Override
    public byte[] readBytes(final int length) throws IOException {
        if (length < 0) {
            throw new IllegalArgumentException("Array cannot have length less than 0.");
        }
        final byte[] b = new byte[length];
        this.buffer.get(b);
        return b;
    }
    
    @Override
    public int readBytes(final byte[] b) throws IOException {
        return this.readBytes(b, 0, b.length);
    }
    
    @Override
    public int readBytes(final byte[] b, final int offset, int length) throws IOException {
        final int readable = this.buffer.remaining();
        if (readable <= 0) {
            return -1;
        }
        if (readable < length) {
            length = readable;
        }
        this.buffer.get(b, offset, length);
        return length;
    }
    
    @Override
    public short[] readShorts(final int length) throws IOException {
        if (length < 0) {
            throw new IllegalArgumentException("Array cannot have length less than 0.");
        }
        final short[] s = new short[length];
        for (int index = 0; index < length; ++index) {
            s[index] = this.readShort();
        }
        return s;
    }
    
    @Override
    public int readShorts(final short[] s) throws IOException {
        return this.readShorts(s, 0, s.length);
    }
    
    @Override
    public int readShorts(final short[] s, final int offset, int length) throws IOException {
        final int readable = this.buffer.remaining();
        if (readable <= 0) {
            return -1;
        }
        if (readable < length * 2) {
            length = readable / 2;
        }
        for (int index = offset; index < offset + length; ++index) {
            s[index] = this.readShort();
        }
        return length;
    }
    
    @Override
    public int[] readInts(final int length) throws IOException {
        if (length < 0) {
            throw new IllegalArgumentException("Array cannot have length less than 0.");
        }
        final int[] i = new int[length];
        for (int index = 0; index < length; ++index) {
            i[index] = this.readInt();
        }
        return i;
    }
    
    @Override
    public int readInts(final int[] i) throws IOException {
        return this.readInts(i, 0, i.length);
    }
    
    @Override
    public int readInts(final int[] i, final int offset, int length) throws IOException {
        final int readable = this.buffer.remaining();
        if (readable <= 0) {
            return -1;
        }
        if (readable < length * 4) {
            length = readable / 4;
        }
        for (int index = offset; index < offset + length; ++index) {
            i[index] = this.readInt();
        }
        return length;
    }
    
    @Override
    public long[] readLongs(final int length) throws IOException {
        if (length < 0) {
            throw new IllegalArgumentException("Array cannot have length less than 0.");
        }
        final long[] l = new long[length];
        for (int index = 0; index < length; ++index) {
            l[index] = this.readLong();
        }
        return l;
    }
    
    @Override
    public int readLongs(final long[] l) throws IOException {
        return this.readLongs(l, 0, l.length);
    }
    
    @Override
    public int readLongs(final long[] l, final int offset, int length) throws IOException {
        final int readable = this.buffer.remaining();
        if (readable <= 0) {
            return -1;
        }
        if (readable < length * 2) {
            length = readable / 2;
        }
        for (int index = offset; index < offset + length; ++index) {
            l[index] = this.readLong();
        }
        return length;
    }
    
    @Override
    public String readString() throws IOException {
        final int length = this.readVarInt();
        final byte[] bytes = this.readBytes(length);
        return new String(bytes, "UTF-8");
    }
    
    @Override
    public UUID readUUID() throws IOException {
        return new UUID(this.readLong(), this.readLong());
    }
    
    @Override
    public int available() throws IOException {
        return this.buffer.remaining();
    }
}
