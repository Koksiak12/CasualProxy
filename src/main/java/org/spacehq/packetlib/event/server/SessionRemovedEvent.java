// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.packetlib.event.server;

import org.spacehq.packetlib.Session;
import org.spacehq.packetlib.Server;

public class SessionRemovedEvent implements ServerEvent
{
    private Server server;
    private Session session;
    
    public SessionRemovedEvent(final Server server, final Session session) {
        this.server = server;
        this.session = session;
    }
    
    public Server getServer() {
        return this.server;
    }
    
    public Session getSession() {
        return this.session;
    }
    
    @Override
    public void call(final ServerListener listener) {
        listener.sessionRemoved(this);
    }
}
