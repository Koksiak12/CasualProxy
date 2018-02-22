// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.packetlib.tcp;

import io.netty.channel.Channel;
import java.net.Socket;
import java.net.Proxy;
import io.netty.channel.socket.oio.OioSocketChannel;
import io.netty.channel.ChannelFactory;

public class ProxyOioChannelFactory implements ChannelFactory<OioSocketChannel>
{
    private Proxy proxy;
    
    public ProxyOioChannelFactory(final Proxy proxy) {
        this.proxy = proxy;
    }
    
    @Override
    public OioSocketChannel newChannel() {
        return new OioSocketChannel(new Socket(this.proxy));
    }
}
