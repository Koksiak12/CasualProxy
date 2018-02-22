// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.packetlib.tcp;

import org.spacehq.packetlib.ConnectionListener;
import org.spacehq.packetlib.Server;
import org.spacehq.packetlib.Session;
import org.spacehq.packetlib.Client;
import java.net.Proxy;
import org.spacehq.packetlib.SessionFactory;

public class TcpSessionFactory implements SessionFactory
{
    private Proxy clientProxy;
    
    public TcpSessionFactory() {
    }
    
    public TcpSessionFactory(final Proxy clientProxy) {
        this.clientProxy = clientProxy;
    }
    
    @Override
    public Session createClientSession(final Client client) {
        return new TcpClientSession(client.getHost(), client.getPort(), client.getPacketProtocol(), client, this.clientProxy);
    }
    
    @Override
    public ConnectionListener createServerListener(final Server server) {
        return new TcpConnectionListener(server.getHost(), server.getPort(), server);
    }
}
