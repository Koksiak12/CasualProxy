// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.packetlib.event.session;

public interface SessionListener
{
    void packetReceived(final PacketReceivedEvent p0);
    
    void packetSent(final PacketSentEvent p0);
    
    void connected(final ConnectedEvent p0);
    
    void disconnecting(final DisconnectingEvent p0);
    
    void disconnected(final DisconnectedEvent p0);
}
