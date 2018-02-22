// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.packetlib.event.server;

import org.spacehq.packetlib.Server;

public class ServerClosedEvent implements ServerEvent
{
    private Server server;
    
    public ServerClosedEvent(final Server server) {
        this.server = server;
    }
    
    public Server getServer() {
        return this.server;
    }
    
    @Override
    public void call(final ServerListener listener) {
        listener.serverClosed(this);
    }
}
