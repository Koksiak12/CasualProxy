// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.packetlib.io.stream;

import java.util.UUID;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import org.spacehq.packetlib.io.NetInput;

public class StreamNetInput implements NetInput
{
    private InputStream in;
    
    public StreamNetInput(final InputStream in) {
        this.in = in;
    }
    
    @Override
    public boolean readBoolean() throws IOException {
        return this.readByte() == 1;
    }
    
    @Override
    public byte readByte() throws IOException {
        return (byte)this.readUnsignedByte();
    }
    
    @Override
    public int readUnsignedByte() throws IOException {
        final int b = this.in.read();
        if (b < 0) {
            throw new EOFException();
        }
        return b;
    }
    
    @Override
    public short readShort() throws IOException {
        return (short)this.readUnsignedShort();
    }
    
    @Override
    public int readUnsignedShort() throws IOException {
        final int ch1 = this.readUnsignedByte();
        final int ch2 = this.readUnsignedByte();
        return (ch1 << 8) + (ch2 << 0);
    }
    
    @Override
    public char readChar() throws IOException {
        final int ch1 = this.readUnsignedByte();
        final int ch2 = this.readUnsignedByte();
        return (char)((ch1 << 8) + (ch2 << 0));
    }
    
    @Override
    public int readInt() throws IOException {
        final int ch1 = this.readUnsignedByte();
        final int ch2 = this.readUnsignedByte();
        final int ch3 = this.readUnsignedByte();
        final int ch4 = this.readUnsignedByte();
        return (ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0);
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
        final byte[] read = this.readBytes(8);
        return (read[0] << 56) + ((read[1] & 0xFF) << 48) + ((read[2] & 0xFF) << 40) + ((read[3] & 0xFF) << 32) + ((read[4] & 0xFF) << 24) + ((read[5] & 0xFF) << 16) + ((read[6] & 0xFF) << 8) + ((read[7] & 0xFF) << 0);
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
        return Float.intBitsToFloat(this.readInt());
    }
    
    @Override
    public double readDouble() throws IOException {
        return Double.longBitsToDouble(this.readLong());
    }
    
    @Override
    public byte[] readBytes(final int length) throws IOException {
        if (length < 0) {
            throw new IllegalArgumentException("Array cannot have length less than 0.");
        }
        final byte[] b = new byte[length];
        int count;
        for (int n = 0; n < length; n += count) {
            count = this.in.read(b, n, length - n);
            if (count < 0) {
                throw new EOFException();
            }
        }
        return b;
    }
    
    @Override
    public int readBytes(final byte[] b) throws IOException {
        return this.in.read(b);
    }
    
    @Override
    public int readBytes(final byte[] b, final int offset, final int length) throws IOException {
        return this.in.read(b, offset, length);
    }
    
    @Override
    public short[] readShorts(final int length) throws IOException {
        if (length < 0) {
            throw new IllegalArgumentException("Array cannot have length less than 0.");
        }
        final short[] s = new short[length];
        final int read = this.readShorts(s);
        if (read < length) {
            throw new EOFException();
        }
        return s;
    }
    
    @Override
    public int readShorts(final short[] s) throws IOException {
        return this.readShorts(s, 0, s.length);
    }
    
    @Override
    public int readShorts(final short[] s, final int offset, final int length) throws IOException {
        for (int index = offset; index < offset + length; ++index) {
            try {
                s[index] = this.readShort();
            }
            catch (EOFException e) {
                return index - offset;
            }
        }
        return length;
    }
    
    @Override
    public int[] readInts(final int length) throws IOException {
        if (length < 0) {
            throw new IllegalArgumentException("Array cannot have length less than 0.");
        }
        final int[] i = new int[length];
        final int read = this.readInts(i);
        if (read < length) {
            throw new EOFException();
        }
        return i;
    }
    
    @Override
    public int readInts(final int[] i) throws IOException {
        return this.readInts(i, 0, i.length);
    }
    
    @Override
    public int readInts(final int[] i, final int offset, final int length) throws IOException {
        for (int index = offset; index < offset + length; ++index) {
            try {
                i[index] = this.readInt();
            }
            catch (EOFException e) {
                return index - offset;
            }
        }
        return length;
    }
    
    @Override
    public long[] readLongs(final int length) throws IOException {
        if (length < 0) {
            throw new IllegalArgumentException("Array cannot have length less than 0.");
        }
        final long[] l = new long[length];
        final int read = this.readLongs(l);
        if (read < length) {
            throw new EOFException();
        }
        return l;
    }
    
    @Override
    public int readLongs(final long[] l) throws IOException {
        return this.readLongs(l, 0, l.length);
    }
    
    @Override
    public int readLongs(final long[] l, final int offset, final int length) throws IOException {
        for (int index = offset; index < offset + length; ++index) {
            try {
                l[index] = this.readLong();
            }
            catch (EOFException e) {
                return index - offset;
            }
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
        return this.in.available();
    }
}
