// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.packetlib.tcp;

import io.netty.handler.timeout.WriteTimeoutException;
import io.netty.handler.timeout.ReadTimeoutException;
import java.net.ConnectException;
import io.netty.channel.ConnectTimeoutException;
import org.spacehq.packetlib.event.session.ConnectedEvent;
import org.spacehq.packetlib.event.session.PacketReceivedEvent;
import io.netty.handler.timeout.WriteTimeoutHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.spacehq.packetlib.event.session.DisconnectedEvent;
import org.spacehq.packetlib.event.session.DisconnectingEvent;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.Future;
import org.spacehq.packetlib.event.session.PacketSentEvent;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import java.util.Iterator;
import io.netty.channel.ChannelHandlerContext;
import org.spacehq.packetlib.event.session.SessionEvent;
import java.util.Collection;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import io.netty.channel.Channel;
import org.spacehq.packetlib.event.session.SessionListener;
import java.util.List;
import java.util.Map;
import org.spacehq.packetlib.packet.PacketProtocol;
import org.spacehq.packetlib.Session;
import org.spacehq.packetlib.packet.Packet;
import io.netty.channel.SimpleChannelInboundHandler;

public abstract class TcpSession extends SimpleChannelInboundHandler<Packet> implements Session
{
    private String host;
    private int port;
    private PacketProtocol protocol;
    private int compressionThreshold;
    private int connectTimeout;
    private int readTimeout;
    private int writeTimeout;
    private Map<String, Object> flags;
    private List<SessionListener> listeners;
    private Channel channel;
    protected boolean disconnected;
    private BlockingQueue<Packet> packets;
    private Thread packetHandleThread;
    
    public TcpSession(final String host, final int port, final PacketProtocol protocol) {
        this.compressionThreshold = -1;
        this.connectTimeout = 30;
        this.readTimeout = 30;
        this.writeTimeout = 0;
        this.flags = new HashMap<String, Object>();
        this.listeners = new CopyOnWriteArrayList<SessionListener>();
        this.disconnected = false;
        this.packets = new LinkedBlockingQueue<Packet>();
        this.host = host;
        this.port = port;
        this.protocol = protocol;
    }
    
    @Override
    public void connect() {
        this.connect(true);
    }
    
    @Override
    public void connect(final boolean wait) {
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
    public PacketProtocol getPacketProtocol() {
        return this.protocol;
    }
    
    @Override
    public Map<String, Object> getFlags() {
        return new HashMap<String, Object>(this.flags);
    }
    
    @Override
    public boolean hasFlag(final String key) {
        return this.getFlags().containsKey(key);
    }
    
    @Override
    public <T> T getFlag(final String key) {
        final Object value = this.getFlags().get(key);
        if (value == null) {
            return null;
        }
        try {
            return (T)value;
        }
        catch (ClassCastException e) {
            throw new IllegalStateException("Tried to get flag \"" + key + "\" as the wrong type. Actual type: " + value.getClass().getName());
        }
    }
    
    @Override
    public void setFlag(final String key, final Object value) {
        this.flags.put(key, value);
    }
    
    @Override
    public List<SessionListener> getListeners() {
        return new ArrayList<SessionListener>(this.listeners);
    }
    
    @Override
    public void addListener(final SessionListener listener) {
        this.listeners.add(listener);
    }
    
    @Override
    public void removeListener(final SessionListener listener) {
        this.listeners.remove(listener);
    }
    
    @Override
    public void callEvent(final SessionEvent event) {
        try {
            for (final SessionListener listener : this.listeners) {
                event.call(listener);
            }
        }
        catch (Throwable t) {
            this.exceptionCaught(null, t);
        }
    }
    
    @Override
    public int getCompressionThreshold() {
        return this.compressionThreshold;
    }
    
    @Override
    public void setCompressionThreshold(final int threshold) {
        this.compressionThreshold = threshold;
        if (this.channel != null) {
            if (this.compressionThreshold >= 0) {
                if (this.channel.pipeline().get("compression") == null) {
                    this.channel.pipeline().addBefore("codec", "compression", new TcpPacketCompression(this));
                }
            }
            else if (this.channel.pipeline().get("compression") != null) {
                this.channel.pipeline().remove("compression");
            }
        }
    }
    
    @Override
    public int getConnectTimeout() {
        return this.connectTimeout;
    }
    
    @Override
    public void setConnectTimeout(final int timeout) {
        this.connectTimeout = timeout;
    }
    
    @Override
    public int getReadTimeout() {
        return this.readTimeout;
    }
    
    @Override
    public void setReadTimeout(final int timeout) {
        this.readTimeout = timeout;
        this.refreshReadTimeoutHandler();
    }
    
    @Override
    public int getWriteTimeout() {
        return this.writeTimeout;
    }
    
    @Override
    public void setWriteTimeout(final int timeout) {
        this.writeTimeout = timeout;
        this.refreshWriteTimeoutHandler();
    }
    
    @Override
    public boolean isConnected() {
        return this.channel != null && this.channel.isOpen() && !this.disconnected;
    }
    
    @Override
    public void send(final Packet packet) {
        if (this.channel == null) {
            return;
        }
        final ChannelFuture future = this.channel.writeAndFlush(packet).addListener((GenericFutureListener<? extends Future<? super Void>>)new ChannelFutureListener() {
            @Override
            public void operationComplete(final ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    TcpSession.this.callEvent(new PacketSentEvent(TcpSession.this, packet));
                }
                else {
                    TcpSession.this.exceptionCaught(null, future.cause());
                }
            }
        });
        if (packet.isPriority()) {
            try {
                future.await();
            }
            catch (InterruptedException ex) {}
        }
    }
    
    @Override
    public void disconnect(final String reason) {
        this.disconnect(reason, false);
    }
    
    @Override
    public void disconnect(final String reason, final boolean wait) {
        this.disconnect(reason, null, wait);
    }
    
    @Override
    public void disconnect(final String reason, final Throwable cause) {
        this.disconnect(reason, cause, false);
    }
    
    @Override
    public void disconnect(final String reason, final Throwable cause, final boolean wait) {
        if (this.disconnected) {
            return;
        }
        this.disconnected = true;
        if (this.packetHandleThread != null) {
            this.packetHandleThread.interrupt();
            this.packetHandleThread = null;
        }
        if (this.channel != null && this.channel.isOpen()) {
            this.callEvent(new DisconnectingEvent(this, reason, cause));
            final ChannelFuture future = this.channel.flush().close().addListener((GenericFutureListener<? extends Future<? super Void>>)new ChannelFutureListener() {
                @Override
                public void operationComplete(final ChannelFuture future) throws Exception {
                    TcpSession.this.callEvent(new DisconnectedEvent(TcpSession.this, (reason != null) ? reason : "Connection closed.", cause));
                }
            });
            if (wait) {
                try {
                    future.await();
                }
                catch (InterruptedException ex) {}
            }
        }
        else {
            this.callEvent(new DisconnectedEvent(this, (reason != null) ? reason : "Connection closed.", cause));
        }
        this.channel = null;
    }
    
    protected void refreshReadTimeoutHandler() {
        this.refreshReadTimeoutHandler(this.channel);
    }
    
    protected void refreshReadTimeoutHandler(final Channel channel) {
        if (channel != null) {
            if (this.readTimeout <= 0) {
                if (channel.pipeline().get("readTimeout") != null) {
                    channel.pipeline().remove("readTimeout");
                }
            }
            else if (channel.pipeline().get("readTimeout") == null) {
                channel.pipeline().addFirst("readTimeout", new ReadTimeoutHandler(this.readTimeout));
            }
            else {
                channel.pipeline().replace("readTimeout", "readTimeout", new ReadTimeoutHandler(this.readTimeout));
            }
        }
    }
    
    protected void refreshWriteTimeoutHandler() {
        this.refreshWriteTimeoutHandler(this.channel);
    }
    
    protected void refreshWriteTimeoutHandler(final Channel channel) {
        if (channel != null) {
            if (this.writeTimeout <= 0) {
                if (channel.pipeline().get("writeTimeout") != null) {
                    channel.pipeline().remove("writeTimeout");
                }
            }
            else if (channel.pipeline().get("writeTimeout") == null) {
                channel.pipeline().addFirst("writeTimeout", new WriteTimeoutHandler(this.writeTimeout));
            }
            else {
                channel.pipeline().replace("writeTimeout", "writeTimeout", new WriteTimeoutHandler(this.writeTimeout));
            }
        }
    }
    
    @Override
    public void channelActive(final ChannelHandlerContext ctx) throws Exception {
        if (this.disconnected || this.channel != null) {
            ctx.channel().close();
            return;
        }
        this.channel = ctx.channel();
        (this.packetHandleThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Packet packet;
                    while ((packet = TcpSession.this.packets.take()) != null) {
                        TcpSession.this.callEvent(new PacketReceivedEvent(TcpSession.this, packet));
                    }
                }
                catch (InterruptedException ex) {}
                catch (Throwable t) {
                    TcpSession.this.exceptionCaught(null, t);
                }
            }
        })).start();
        this.callEvent(new ConnectedEvent(this));
    }
    
    @Override
    public void channelInactive(final ChannelHandlerContext ctx) throws Exception {
        if (ctx.channel() == this.channel) {
            this.disconnect("Connection closed.");
        }
    }
    
    @Override
    public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) {
        String message = null;
        if (cause instanceof ConnectTimeoutException || (cause instanceof ConnectException && cause.getMessage().contains("connection timed out"))) {
            message = "Connection timed out.";
        }
        else if (cause instanceof ReadTimeoutException) {
            message = "Read timed out.";
        }
        else if (cause instanceof WriteTimeoutException) {
            message = "Write timed out.";
        }
        else {
            message = "Internal network exception.";
        }
        this.disconnect(message, cause);
    }
    
    @Override
    protected void messageReceived(final ChannelHandlerContext ctx, final Packet packet) throws Exception {
        if (!packet.isPriority()) {
            this.packets.add(packet);
        }
    }
}
