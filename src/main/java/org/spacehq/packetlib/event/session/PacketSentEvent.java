// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.packetlib.event.session;

import org.spacehq.packetlib.packet.Packet;
import org.spacehq.packetlib.Session;

public class PacketSentEvent implements SessionEvent
{
    private Session session;
    private Packet packet;
    
    public PacketSentEvent(final Session session, final Packet packet) {
        this.session = session;
        this.packet = packet;
    }
    
    public Session getSession() {
        return this.session;
    }
    
    public <T extends Packet> T getPacket() {
        try {
            return (T)this.packet;
        }
        catch (ClassCastException e) {
            throw new IllegalStateException("Tried to get packet as the wrong type. Actual type: " + this.packet.getClass().getName());
        }
    }
    
    @Override
    public void call(final SessionListener listener) {
        listener.packetSent(this);
    }
}
