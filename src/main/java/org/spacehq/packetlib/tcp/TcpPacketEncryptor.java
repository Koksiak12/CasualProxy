// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.packetlib.tcp;

import java.util.List;
import io.netty.channel.ChannelHandlerContext;
import org.spacehq.packetlib.Session;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.ByteToMessageCodec;

public class TcpPacketEncryptor extends ByteToMessageCodec<ByteBuf>
{
    private Session session;
    private byte[] decryptedArray;
    private byte[] encryptedArray;
    
    public TcpPacketEncryptor(final Session session) {
        this.decryptedArray = new byte[0];
        this.encryptedArray = new byte[0];
        this.session = session;
    }
    
    public void encode(final ChannelHandlerContext ctx, final ByteBuf in, final ByteBuf out) throws Exception {
        if (this.session.getPacketProtocol().getEncryption() != null) {
            final int length = in.readableBytes();
            final byte[] bytes = this.getBytes(in);
            final int outLength = this.session.getPacketProtocol().getEncryption().getEncryptOutputSize(length);
            if (this.encryptedArray.length < outLength) {
                this.encryptedArray = new byte[outLength];
            }
            out.writeBytes(this.encryptedArray, 0, this.session.getPacketProtocol().getEncryption().encrypt(bytes, 0, length, this.encryptedArray, 0));
        }
        else {
            out.writeBytes(in);
        }
    }
    
    @Override
    protected void decode(final ChannelHandlerContext ctx, final ByteBuf buf, final List<Object> out) throws Exception {
        if (this.session.getPacketProtocol().getEncryption() != null) {
            final int length = buf.readableBytes();
            final byte[] bytes = this.getBytes(buf);
            final ByteBuf result = ctx.alloc().heapBuffer(this.session.getPacketProtocol().getEncryption().getDecryptOutputSize(length));
            result.writerIndex(this.session.getPacketProtocol().getEncryption().decrypt(bytes, 0, length, result.array(), result.arrayOffset()));
            out.add(result);
        }
        else {
            out.add(buf.readBytes(buf.readableBytes()));
        }
    }
    
    private byte[] getBytes(final ByteBuf buf) {
        final int length = buf.readableBytes();
        if (this.decryptedArray.length < length) {
            this.decryptedArray = new byte[length];
        }
        buf.readBytes(this.decryptedArray, 0, length);
        return this.decryptedArray;
    }
}
