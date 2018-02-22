// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.packetlib.crypt;

public interface PacketEncryption
{
    int getDecryptOutputSize(final int p0);
    
    int getEncryptOutputSize(final int p0);
    
    int decrypt(final byte[] p0, final int p1, final int p2, final byte[] p3, final int p4) throws Exception;
    
    int encrypt(final byte[] p0, final int p1, final int p2, final byte[] p3, final int p4) throws Exception;
}
