// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.packetlib.event.server;

public interface ServerListener
{
    void serverBound(final ServerBoundEvent p0);
    
    void serverClosing(final ServerClosingEvent p0);
    
    void serverClosed(final ServerClosedEvent p0);
    
    void sessionAdded(final SessionAddedEvent p0);
    
    void sessionRemoved(final SessionRemovedEvent p0);
}
