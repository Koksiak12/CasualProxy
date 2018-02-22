// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.packetlib.tcp;

import io.netty.buffer.Unpooled;
import io.netty.handler.codec.DecoderException;
import org.spacehq.packetlib.tcp.io.ByteBufNetInput;
import java.util.List;
import org.spacehq.packetlib.tcp.io.ByteBufNetOutput;
import io.netty.channel.ChannelHandlerContext;
import java.util.zip.Inflater;
import java.util.zip.Deflater;
import org.spacehq.packetlib.Session;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.ByteToMessageCodec;

public class TcpPacketCompression extends ByteToMessageCodec<ByteBuf>
{
    private static final int MAX_COMPRESSED_SIZE = 2097152;
    private Session session;
    private Deflater deflater;
    private Inflater inflater;
    private byte[] buf;
    
    public TcpPacketCompression(final Session session) {
        this.deflater = new Deflater();
        this.inflater = new Inflater();
        this.buf = new byte[8192];
        this.session = session;
    }
    
    public void encode(final ChannelHandlerContext ctx, final ByteBuf in, final ByteBuf out) throws Exception {
        final int readable = in.readableBytes();
        final ByteBufNetOutput output = new ByteBufNetOutput(out);
        if (readable < this.session.getCompressionThreshold()) {
            output.writeVarInt(0);
            out.writeBytes(in);
        }
        else {
            final byte[] bytes = new byte[readable];
            in.readBytes(bytes);
            output.writeVarInt(bytes.length);
            this.deflater.setInput(bytes, 0, readable);
            this.deflater.finish();
            while (!this.deflater.finished()) {
                final int length = this.deflater.deflate(this.buf);
                output.writeBytes(this.buf, length);
            }
            this.deflater.reset();
        }
    }
    
    @Override
    protected void decode(final ChannelHandlerContext ctx, final ByteBuf buf, final List<Object> out) throws Exception {
        if (buf.readableBytes() != 0) {
            final ByteBufNetInput in = new ByteBufNetInput(buf);
            final int size = in.readVarInt();
            if (size == 0) {
                out.add(buf.readBytes(buf.readableBytes()));
            }
            else {
                if (size < this.session.getCompressionThreshold()) {
                    throw new DecoderException("Badly compressed packet: size of " + size + " is below threshold of " + this.session.getCompressionThreshold() + ".");
                }
                if (size > 2097152) {
                    throw new DecoderException("Badly compressed packet: size of " + size + " is larger than protocol maximum of " + 2097152 + ".");
                }
                final byte[] bytes = new byte[buf.readableBytes()];
                in.readBytes(bytes);
                this.inflater.setInput(bytes);
                final byte[] inflated = new byte[size];
                this.inflater.inflate(inflated);
                out.add(Unpooled.wrappedBuffer(inflated));
                this.inflater.reset();
            }
        }
    }
}
