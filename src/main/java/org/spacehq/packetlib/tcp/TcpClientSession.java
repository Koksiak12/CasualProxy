// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.packetlib.tcp;

import io.netty.bootstrap.AbstractBootstrap;
import io.netty.util.concurrent.Future;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import javax.naming.directory.InitialDirContext;
import java.util.Hashtable;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelOption;
import org.spacehq.packetlib.Session;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFactory;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.bootstrap.Bootstrap;
import org.spacehq.packetlib.packet.PacketProtocol;
import io.netty.channel.EventLoopGroup;
import java.net.Proxy;
import org.spacehq.packetlib.Client;

public class TcpClientSession extends TcpSession
{
    private Client client;
    private Proxy proxy;
    private EventLoopGroup group;
    
    public TcpClientSession(final String host, final int port, final PacketProtocol protocol, final Client client, final Proxy proxy) {
        super(host, port, protocol);
        this.client = client;
        this.proxy = proxy;
    }
    
    @Override
    public void connect(final boolean wait) {
        if (this.disconnected) {
            throw new IllegalStateException("Session has already been disconnected.");
        }
        if (this.group != null) {
            return;
        }
        try {
            final Bootstrap bootstrap = new Bootstrap();
            if (this.proxy != null) {
                this.group = new OioEventLoopGroup();
                bootstrap.channelFactory(new ProxyOioChannelFactory(this.proxy));
            }
            else {
                this.group = new NioEventLoopGroup();
                bootstrap.channel(NioSocketChannel.class);
            }
            bootstrap.handler(new ChannelInitializer<Channel>() {
                public void initChannel(final Channel channel) throws Exception {
                    TcpClientSession.this.getPacketProtocol().newClientSession(TcpClientSession.this.client, TcpClientSession.this);
                    channel.config().setOption(ChannelOption.IP_TOS, 24);
                    channel.config().setOption(ChannelOption.TCP_NODELAY, false);
                    final ChannelPipeline pipeline = channel.pipeline();
                    TcpClientSession.this.refreshReadTimeoutHandler(channel);
                    TcpClientSession.this.refreshWriteTimeoutHandler(channel);
                    pipeline.addLast("encryption", new TcpPacketEncryptor(TcpClientSession.this));
                    pipeline.addLast("sizer", new TcpPacketSizer(TcpClientSession.this));
                    pipeline.addLast("codec", new TcpPacketCodec(TcpClientSession.this));
                    pipeline.addLast("manager", TcpClientSession.this);
                }
            }).group(this.group).option(ChannelOption.CONNECT_TIMEOUT_MILLIS, this.getConnectTimeout() * 1000);
            final Runnable connectTask = new Runnable() {
                @Override
                public void run() {
                    try {
                        String host = TcpClientSession.this.getHost();
                        int port = TcpClientSession.this.getPort();
                        try {
                            final Hashtable<String, String> environment = new Hashtable<String, String>();
                            environment.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");
                            environment.put("java.naming.provider.url", "dns:");
                            final String[] result = new InitialDirContext(environment).getAttributes(TcpClientSession.this.getPacketProtocol().getSRVRecordPrefix() + "._tcp." + host, new String[] { "SRV" }).get("srv").get().toString().split(" ", 4);
                            host = result[3];
                            port = Integer.parseInt(result[2]);
                        }
                        catch (Throwable t2) {}
                        bootstrap.remoteAddress(host, port);
                        final ChannelFuture future = bootstrap.connect().sync();
                        if (future.isSuccess()) {
                            while (!TcpClientSession.this.isConnected() && !TcpClientSession.this.disconnected) {
                                try {
                                    Thread.sleep(5L);
                                }
                                catch (InterruptedException ex) {}
                            }
                        }
                    }
                    catch (Throwable t) {
                        TcpClientSession.this.exceptionCaught(null, t);
                    }
                }
            };
            if (wait) {
                connectTask.run();
            }
            else {
                new Thread(connectTask).start();
            }
        }
        catch (Throwable t) {
            this.exceptionCaught(null, t);
        }
    }
    
    @Override
    public void disconnect(final String reason, final Throwable cause, final boolean wait) {
        super.disconnect(reason, cause, wait);
        if (this.group != null) {
            final Future<?> future = this.group.shutdownGracefully();
            if (wait) {
                try {
                    future.await();
                }
                catch (InterruptedException ex) {}
            }
            this.group = null;
        }
    }
}
