// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.auth.util;

public class Base64
{
    private static final byte EQUALS_SIGN = 61;
    private static final byte WHITE_SPACE_ENC = -5;
    private static final byte EQUALS_SIGN_ENC = -1;
    private static final byte[] ALPHABET;
    private static final byte[] DECODABET;
    
    public static byte[] encode(final byte[] source) {
        if (source == null) {
            throw new NullPointerException("Cannot serialize a null array.");
        }
        final byte[] outBuff = new byte[source.length / 3 * 4 + ((source.length % 3 > 0) ? 4 : 0)];
        int d;
        int e;
        for (d = 0, e = 0; d < source.length - 2; d += 3, e += 4) {
            encode3to4(source, d, 3, outBuff, e);
        }
        if (d < source.length) {
            encode3to4(source, d, source.length - d, outBuff, e);
            e += 4;
        }
        if (e <= outBuff.length - 1) {
            final byte[] finalOut = new byte[e];
            System.arraycopy(outBuff, 0, finalOut, 0, e);
            return finalOut;
        }
        return outBuff;
    }
    
    public static byte[] decode(final byte[] source) {
        if (source == null) {
            throw new NullPointerException("Cannot decode null source array.");
        }
        final byte[] outBuff = new byte[source.length * 3 / 4];
        final byte[] b4 = new byte[4];
        int outBuffPosn = 0;
        int b4Posn = 0;
        for (int i = 0; i < source.length; ++i) {
            final byte sbiDecode = Base64.DECODABET[source[i] & 0xFF];
            if (sbiDecode < -5) {
                throw new IllegalArgumentException(String.format("Bad Base64 input character decimal %d in array position %d", source[i] & 0xFF, i));
            }
            if (sbiDecode >= -1) {
                b4[b4Posn++] = source[i];
                if (b4Posn > 3) {
                    outBuffPosn += decode4to3(b4, 0, outBuff, outBuffPosn);
                    b4Posn = 0;
                    if (source[i] == 61) {
                        break;
                    }
                }
            }
        }
        final byte[] out = new byte[outBuffPosn];
        System.arraycopy(outBuff, 0, out, 0, outBuffPosn);
        return out;
    }
    
    private static byte[] encode3to4(final byte[] source, final int srcOffset, final int numSigBytes, final byte[] destination, final int destOffset) {
        final int inBuff = ((numSigBytes > 0) ? (source[srcOffset] << 24 >>> 8) : 0) | ((numSigBytes > 1) ? (source[srcOffset + 1] << 24 >>> 16) : 0) | ((numSigBytes > 2) ? (source[srcOffset + 2] << 24 >>> 24) : 0);
        switch (numSigBytes) {
            case 3: {
                destination[destOffset] = Base64.ALPHABET[inBuff >>> 18];
                destination[destOffset + 1] = Base64.ALPHABET[inBuff >>> 12 & 0x3F];
                destination[destOffset + 2] = Base64.ALPHABET[inBuff >>> 6 & 0x3F];
                destination[destOffset + 3] = Base64.ALPHABET[inBuff & 0x3F];
                return destination;
            }
            case 2: {
                destination[destOffset] = Base64.ALPHABET[inBuff >>> 18];
                destination[destOffset + 1] = Base64.ALPHABET[inBuff >>> 12 & 0x3F];
                destination[destOffset + 2] = Base64.ALPHABET[inBuff >>> 6 & 0x3F];
                destination[destOffset + 3] = 61;
                return destination;
            }
            case 1: {
                destination[destOffset] = Base64.ALPHABET[inBuff >>> 18];
                destination[destOffset + 1] = Base64.ALPHABET[inBuff >>> 12 & 0x3F];
                destination[destOffset + 3] = (destination[destOffset + 2] = 61);
                return destination;
            }
            default: {
                return destination;
            }
        }
    }
    
    private static int decode4to3(final byte[] source, final int srcOffset, final byte[] destination, final int destOffset) {
        if (source == null) {
            throw new NullPointerException("Source array was null.");
        }
        if (destination == null) {
            throw new NullPointerException("Destination array was null.");
        }
        if (srcOffset < 0 || srcOffset + 3 >= source.length) {
            throw new IllegalArgumentException(String.format("Source array with length %d cannot have offset of %d and still process four bytes.", source.length, srcOffset));
        }
        if (destOffset < 0 || destOffset + 2 >= destination.length) {
            throw new IllegalArgumentException(String.format("Destination array with length %d cannot have offset of %d and still store three bytes.", destination.length, destOffset));
        }
        if (source[srcOffset + 2] == 61) {
            final int outBuff = (Base64.DECODABET[source[srcOffset]] & 0xFF) << 18 | (Base64.DECODABET[source[srcOffset + 1]] & 0xFF) << 12;
            destination[destOffset] = (byte)(outBuff >>> 16);
            return 1;
        }
        if (source[srcOffset + 3] == 61) {
            final int outBuff = (Base64.DECODABET[source[srcOffset]] & 0xFF) << 18 | (Base64.DECODABET[source[srcOffset + 1]] & 0xFF) << 12 | (Base64.DECODABET[source[srcOffset + 2]] & 0xFF) << 6;
            destination[destOffset] = (byte)(outBuff >>> 16);
            destination[destOffset + 1] = (byte)(outBuff >>> 8);
            return 2;
        }
        final int outBuff = (Base64.DECODABET[source[srcOffset]] & 0xFF) << 18 | (Base64.DECODABET[source[srcOffset + 1]] & 0xFF) << 12 | (Base64.DECODABET[source[srcOffset + 2]] & 0xFF) << 6 | (Base64.DECODABET[source[srcOffset + 3]] & 0xFF);
        destination[destOffset] = (byte)(outBuff >> 16);
        destination[destOffset + 1] = (byte)(outBuff >> 8);
        destination[destOffset + 2] = (byte)outBuff;
        return 3;
    }
    
    static {
        ALPHABET = new byte[] { 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 43, 47 };
        DECODABET = new byte[] { -9, -9, -9, -9, -9, -9, -9, -9, -9, -5, -5, -9, -9, -5, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -5, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 62, -9, -9, -9, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -9, -9, -9, -1, -9, -9, -9, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -9, -9, -9, -9, -9, -9, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9 };
    }
}
