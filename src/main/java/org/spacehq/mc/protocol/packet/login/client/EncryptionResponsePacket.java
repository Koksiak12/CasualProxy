// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.protocol.packet.login.client;

import org.spacehq.packetlib.io.NetOutput;
import java.io.IOException;
import org.spacehq.packetlib.io.NetInput;
import java.security.PrivateKey;
import java.security.Key;
import org.spacehq.mc.protocol.util.CryptUtil;
import java.security.PublicKey;
import javax.crypto.SecretKey;
import org.spacehq.packetlib.packet.Packet;

public class EncryptionResponsePacket implements Packet
{
    private byte[] sharedKey;
    private byte[] verifyToken;
    
    private EncryptionResponsePacket() {
    }
    
    public EncryptionResponsePacket(final SecretKey secretKey, final PublicKey publicKey, final byte[] verifyToken) {
        this.sharedKey = CryptUtil.encryptData(publicKey, secretKey.getEncoded());
        this.verifyToken = CryptUtil.encryptData(publicKey, verifyToken);
    }
    
    public SecretKey getSecretKey(final PrivateKey privateKey) {
        return CryptUtil.decryptSharedKey(privateKey, this.sharedKey);
    }
    
    public byte[] getVerifyToken(final PrivateKey privateKey) {
        return CryptUtil.decryptData(privateKey, this.verifyToken);
    }
    
    @Override
    public void read(final NetInput in) throws IOException {
        this.sharedKey = in.readBytes(in.readVarInt());
        this.verifyToken = in.readBytes(in.readVarInt());
    }
    
    @Override
    public void write(final NetOutput out) throws IOException {
        out.writeVarInt(this.sharedKey.length);
        out.writeBytes(this.sharedKey);
        out.writeVarInt(this.verifyToken.length);
        out.writeBytes(this.verifyToken);
    }
    
    @Override
    public boolean isPriority() {
        return true;
    }
}
