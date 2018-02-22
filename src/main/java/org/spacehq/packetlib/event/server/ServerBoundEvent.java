// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.packetlib.event.server;

import org.spacehq.packetlib.Server;

public class ServerBoundEvent implements ServerEvent
{
    private Server server;
    
    public ServerBoundEvent(final Server server) {
        this.server = server;
    }
    
    public Server getServer() {
        return this.server;
    }
    
    @Override
    public void call(final ServerListener listener) {
        listener.serverBound(this);
    }
}
