// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.packetlib.tcp;

import io.netty.handler.codec.CorruptedFrameException;
import org.spacehq.packetlib.io.NetInput;
import org.spacehq.packetlib.tcp.io.ByteBufNetInput;
import io.netty.buffer.Unpooled;
import java.util.List;
import org.spacehq.packetlib.io.NetOutput;
import org.spacehq.packetlib.tcp.io.ByteBufNetOutput;
import io.netty.channel.ChannelHandlerContext;
import org.spacehq.packetlib.Session;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.ByteToMessageCodec;

public class TcpPacketSizer extends ByteToMessageCodec<ByteBuf>
{
    private Session session;
    
    public TcpPacketSizer(final Session session) {
        this.session = session;
    }
    
    public void encode(final ChannelHandlerContext ctx, final ByteBuf in, final ByteBuf out) throws Exception {
        final int length = in.readableBytes();
        out.ensureWritable(this.session.getPacketProtocol().getPacketHeader().getLengthSize(length) + length);
        this.session.getPacketProtocol().getPacketHeader().writeLength(new ByteBufNetOutput(out), length);
        out.writeBytes(in);
    }
    
    @Override
    protected void decode(final ChannelHandlerContext ctx, final ByteBuf buf, final List<Object> out) throws Exception {
        final int size = this.session.getPacketProtocol().getPacketHeader().getLengthSize();
        if (size > 0) {
            buf.markReaderIndex();
            final byte[] lengthBytes = new byte[size];
            int index = 0;
            while (index < lengthBytes.length) {
                if (!buf.isReadable()) {
                    buf.resetReaderIndex();
                    return;
                }
                lengthBytes[index] = buf.readByte();
                if ((this.session.getPacketProtocol().getPacketHeader().isLengthVariable() && lengthBytes[index] >= 0) || index == size - 1) {
                    final int length = this.session.getPacketProtocol().getPacketHeader().readLength(new ByteBufNetInput(Unpooled.wrappedBuffer(lengthBytes)), buf.readableBytes());
                    if (buf.readableBytes() < length) {
                        buf.resetReaderIndex();
                        return;
                    }
                    out.add(buf.readBytes(length));
                    return;
                }
                else {
                    ++index;
                }
            }
            throw new CorruptedFrameException("Length is too long.");
        }
        out.add(buf.readBytes(buf.readableBytes()));
    }
}
