// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.packetlib.crypt;

import java.security.GeneralSecurityException;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import java.security.Key;
import javax.crypto.Cipher;

public class AESEncryption implements PacketEncryption
{
    private Cipher inCipher;
    private Cipher outCipher;
    
    public AESEncryption(final Key key) throws GeneralSecurityException {
        (this.inCipher = Cipher.getInstance("AES/CFB8/NoPadding")).init(2, key, new IvParameterSpec(key.getEncoded()));
        (this.outCipher = Cipher.getInstance("AES/CFB8/NoPadding")).init(1, key, new IvParameterSpec(key.getEncoded()));
    }
    
    @Override
    public int getDecryptOutputSize(final int length) {
        return this.inCipher.getOutputSize(length);
    }
    
    @Override
    public int getEncryptOutputSize(final int length) {
        return this.outCipher.getOutputSize(length);
    }
    
    @Override
    public int decrypt(final byte[] input, final int inputOffset, final int inputLength, final byte[] output, final int outputOffset) throws Exception {
        return this.inCipher.update(input, inputOffset, inputLength, output, outputOffset);
    }
    
    @Override
    public int encrypt(final byte[] input, final int inputOffset, final int inputLength, final byte[] output, final int outputOffset) throws Exception {
        return this.outCipher.update(input, inputOffset, inputLength, output, outputOffset);
    }
}
