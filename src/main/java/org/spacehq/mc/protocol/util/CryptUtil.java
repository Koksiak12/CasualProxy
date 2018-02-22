// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.protocol.util;

import java.security.MessageDigest;
import java.io.UnsupportedEncodingException;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.PrivateKey;
import java.security.GeneralSecurityException;
import java.io.IOException;
import java.security.spec.KeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.KeyPairGenerator;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class CryptUtil
{
    public static SecretKey generateSharedKey() {
        try {
            final KeyGenerator gen = KeyGenerator.getInstance("AES");
            gen.init(128);
            return gen.generateKey();
        }
        catch (NoSuchAlgorithmException e) {
            throw new Error("Failed to generate shared key.", e);
        }
    }
    
    public static KeyPair generateKeyPair() {
        try {
            final KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA");
            gen.initialize(1024);
            return gen.generateKeyPair();
        }
        catch (NoSuchAlgorithmException e) {
            throw new Error("Failed to generate key pair.", e);
        }
    }
    
    public static PublicKey decodePublicKey(final byte[] bytes) throws IOException {
        try {
            return KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(bytes));
        }
        catch (GeneralSecurityException e) {
            throw new IOException("Could not decrypt public key.", e);
        }
    }
    
    public static SecretKey decryptSharedKey(final PrivateKey privateKey, final byte[] sharedKey) {
        return new SecretKeySpec(decryptData(privateKey, sharedKey), "AES");
    }
    
    public static byte[] encryptData(final Key key, final byte[] data) {
        return runEncryption(1, key, data);
    }
    
    public static byte[] decryptData(final Key key, final byte[] data) {
        return runEncryption(2, key, data);
    }
    
    private static byte[] runEncryption(final int mode, final Key key, final byte[] data) {
        try {
            final Cipher cipher = Cipher.getInstance(key.getAlgorithm());
            cipher.init(mode, key);
            return cipher.doFinal(data);
        }
        catch (GeneralSecurityException e) {
            throw new Error("Failed to run encryption.", e);
        }
    }
    
    public static byte[] getServerIdHash(final String serverId, final PublicKey publicKey, final SecretKey secretKey) {
        try {
            return encrypt("SHA-1", new byte[][] { serverId.getBytes("ISO_8859_1"), secretKey.getEncoded(), publicKey.getEncoded() });
        }
        catch (UnsupportedEncodingException e) {
            throw new Error("Failed to generate server id hash.", e);
        }
    }
    
    private static byte[] encrypt(final String encryption, final byte[]... data) {
        try {
            final MessageDigest digest = MessageDigest.getInstance(encryption);
            for (final byte[] array : data) {
                digest.update(array);
            }
            return digest.digest();
        }
        catch (NoSuchAlgorithmException e) {
            throw new Error("Failed to encrypt data.", e);
        }
    }
}
