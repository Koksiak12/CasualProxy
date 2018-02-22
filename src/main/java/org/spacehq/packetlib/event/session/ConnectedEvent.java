// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.packetlib.event.session;

import org.spacehq.packetlib.Session;

public class ConnectedEvent implements SessionEvent
{
    private Session session;
    
    public ConnectedEvent(final Session session) {
        this.session = session;
    }
    
    public Session getSession() {
        return this.session;
    }
    
    @Override
    public void call(final SessionListener listener) {
        listener.connected(this);
    }
}
