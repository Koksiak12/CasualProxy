// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.packetlib.tcp;

import io.netty.bootstrap.AbstractBootstrap;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.Future;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelPipeline;
import org.spacehq.packetlib.packet.PacketProtocol;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelOption;
import org.spacehq.packetlib.Session;
import java.net.InetSocketAddress;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import org.spacehq.packetlib.Server;
import org.spacehq.packetlib.ConnectionListener;

public class TcpConnectionListener implements ConnectionListener
{
    private String host;
    private int port;
    private Server server;
    private EventLoopGroup group;
    private Channel channel;
    
    public TcpConnectionListener(final String host, final int port, final Server server) {
        this.host = host;
        this.port = port;
        this.server = server;
    }
    
    @Override
    public String getHost() {
        return this.host;
    }
    
    @Override
    public int getPort() {
        return this.port;
    }
    
    @Override
    public boolean isListening() {
        return this.channel != null && this.channel.isOpen();
    }
    
    @Override
    public void bind() {
        this.bind(true);
    }
    
    @Override
    public void bind(final boolean wait) {
        this.bind(wait, null);
    }
    
    @Override
    public void bind(final boolean wait, final Runnable callback) {
        if (this.group != null || this.channel != null) {
            return;
        }
        this.group = new NioEventLoopGroup();
        final ChannelFuture future = new ServerBootstrap().channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<Channel>() {
            public void initChannel(final Channel channel) throws Exception {
                final InetSocketAddress address = (InetSocketAddress)channel.remoteAddress();
                final PacketProtocol protocol = TcpConnectionListener.this.server.createPacketProtocol();
                final TcpSession session = new TcpServerSession(address.getHostName(), address.getPort(), protocol, TcpConnectionListener.this.server);
                session.getPacketProtocol().newServerSession(TcpConnectionListener.this.server, session);
                channel.config().setOption(ChannelOption.IP_TOS, 24);
                channel.config().setOption(ChannelOption.TCP_NODELAY, false);
                final ChannelPipeline pipeline = channel.pipeline();
                session.refreshReadTimeoutHandler(channel);
                session.refreshWriteTimeoutHandler(channel);
                pipeline.addLast("encryption", new TcpPacketEncryptor(session));
                pipeline.addLast("sizer", new TcpPacketSizer(session));
                pipeline.addLast("codec", new TcpPacketCodec(session));
                pipeline.addLast("manager", session);
            }
        }).group(this.group).localAddress(this.host, this.port).bind();
        if (wait) {
            try {
                future.sync();
            }
            catch (InterruptedException ex) {}
            this.channel = future.channel();
            if (callback != null) {
                callback.run();
            }
        }
        else {
            future.addListener((GenericFutureListener<? extends Future<? super Void>>)new ChannelFutureListener() {
                @Override
                public void operationComplete(final ChannelFuture future) throws Exception {
                    if (future.isSuccess()) {
                        TcpConnectionListener.this.channel = future.channel();
                        if (callback != null) {
                            callback.run();
                        }
                    }
                    else {
                        System.err.println("[ERROR] Failed to asynchronously bind connection listener.");
                        if (future.cause() != null) {
                            future.cause().printStackTrace();
                        }
                    }
                }
            });
        }
    }
    
    @Override
    public void close() {
        this.close(false);
    }
    
    @Override
    public void close(final boolean wait) {
        this.close(wait, null);
    }
    
    @Override
    public void close(final boolean wait, final Runnable callback) {
        if (this.channel != null) {
            if (this.channel.isOpen()) {
                final ChannelFuture future = this.channel.close();
                if (wait) {
                    try {
                        future.sync();
                    }
                    catch (InterruptedException ex) {}
                    if (callback != null) {
                        callback.run();
                    }
                }
                else {
                    future.addListener((GenericFutureListener<? extends Future<? super Void>>)new ChannelFutureListener() {
                        @Override
                        public void operationComplete(final ChannelFuture future) throws Exception {
                            if (future.isSuccess()) {
                                if (callback != null) {
                                    callback.run();
                                }
                            }
                            else {
                                System.err.println("[ERROR] Failed to asynchronously close connection listener.");
                                if (future.cause() != null) {
                                    future.cause().printStackTrace();
                                }
                            }
                        }
                    });
                }
            }
            this.channel = null;
        }
        if (this.group != null) {
            final Future<?> future2 = this.group.shutdownGracefully();
            if (wait) {
                try {
                    future2.sync();
                }
                catch (InterruptedException ex2) {}
            }
            else {
                future2.addListener(new GenericFutureListener() {
                    @Override
                    public void operationComplete(final Future future) throws Exception {
                        if (!future.isSuccess()) {
                            System.err.println("[ERROR] Failed to asynchronously close connection listener.");
                            if (future.cause() != null) {
                                future.cause().printStackTrace();
                            }
                        }
                    }
                });
            }
            this.group = null;
        }
    }
}
