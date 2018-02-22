// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.packetlib.tcp;

import org.spacehq.packetlib.io.NetInput;
import org.spacehq.packetlib.event.session.SessionEvent;
import org.spacehq.packetlib.event.session.PacketReceivedEvent;
import org.spacehq.packetlib.tcp.io.ByteBufNetInput;
import java.util.List;
import org.spacehq.packetlib.io.NetOutput;
import org.spacehq.packetlib.tcp.io.ByteBufNetOutput;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import org.spacehq.packetlib.Session;
import org.spacehq.packetlib.packet.Packet;
import io.netty.handler.codec.ByteToMessageCodec;

public class TcpPacketCodec extends ByteToMessageCodec<Packet>
{
    private Session session;
    
    public TcpPacketCodec(final Session session) {
        this.session = session;
    }
    
    public void encode(final ChannelHandlerContext ctx, final Packet packet, final ByteBuf buf) throws Exception {
        final NetOutput out = new ByteBufNetOutput(buf);
        this.session.getPacketProtocol().getPacketHeader().writePacketId(out, this.session.getPacketProtocol().getOutgoingId(packet.getClass()));
        packet.write(out);
    }
    
    @Override
    protected void decode(final ChannelHandlerContext ctx, final ByteBuf buf, final List<Object> out) throws Exception {
        final int initial = buf.readerIndex();
        final NetInput in = new ByteBufNetInput(buf);
        final int id = this.session.getPacketProtocol().getPacketHeader().readPacketId(in);
        if (id == -1) {
            buf.readerIndex(initial);
            return;
        }
        final Packet packet = this.session.getPacketProtocol().createIncomingPacket(id);
        packet.read(in);
        if (buf.readableBytes() > 0) {
            throw new IllegalStateException("Packet \"" + packet.getClass().getSimpleName() + "\" not fully read.");
        }
        if (packet.isPriority()) {
            this.session.callEvent(new PacketReceivedEvent(this.session, packet));
        }
        out.add(packet);
    }
}
