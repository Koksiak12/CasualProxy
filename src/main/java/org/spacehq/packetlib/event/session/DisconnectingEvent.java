// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.packetlib.event.session;

import org.spacehq.packetlib.Session;

public class DisconnectingEvent implements SessionEvent
{
    private Session session;
    private String reason;
    private Throwable cause;
    
    public DisconnectingEvent(final Session session, final String reason) {
        this(session, reason, null);
    }
    
    public DisconnectingEvent(final Session session, final String reason, final Throwable cause) {
        this.session = session;
        this.reason = reason;
        this.cause = cause;
    }
    
    public Session getSession() {
        return this.session;
    }
    
    public String getReason() {
        return this.reason;
    }
    
    public Throwable getCause() {
        return this.cause;
    }
    
    @Override
    public void call(final SessionListener listener) {
        listener.disconnecting(this);
    }
}
