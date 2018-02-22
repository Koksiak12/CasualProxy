// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.packetlib;

public interface SessionFactory
{
    Session createClientSession(final Client p0);
    
    ConnectionListener createServerListener(final Server p0);
}
