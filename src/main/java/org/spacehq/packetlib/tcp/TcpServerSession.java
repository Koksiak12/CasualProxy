// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.packetlib.tcp;

import org.spacehq.packetlib.Session;
import io.netty.channel.ChannelHandlerContext;
import java.util.Map;
import org.spacehq.packetlib.packet.PacketProtocol;
import org.spacehq.packetlib.Server;

public class TcpServerSession extends TcpSession
{
    private Server server;
    
    public TcpServerSession(final String host, final int port, final PacketProtocol protocol, final Server server) {
        super(host, port, protocol);
        this.server = server;
    }
    
    @Override
    public Map<String, Object> getFlags() {
        final Map<String, Object> ret = super.getFlags();
        ret.putAll(this.server.getGlobalFlags());
        return ret;
    }
    
    @Override
    public void channelActive(final ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        this.server.addSession(this);
    }
    
    @Override
    public void channelInactive(final ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        this.server.removeSession(this);
    }
}
