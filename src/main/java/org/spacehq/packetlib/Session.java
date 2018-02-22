// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.packetlib;

import org.spacehq.packetlib.packet.Packet;
import org.spacehq.packetlib.event.session.SessionEvent;
import org.spacehq.packetlib.event.session.SessionListener;
import java.util.List;
import java.util.Map;
import org.spacehq.packetlib.packet.PacketProtocol;

public interface Session
{
    void connect();
    
    void connect(final boolean p0);
    
    String getHost();
    
    int getPort();
    
    PacketProtocol getPacketProtocol();
    
    Map<String, Object> getFlags();
    
    boolean hasFlag(final String p0);
    
     <T> T getFlag(final String p0);
    
    void setFlag(final String p0, final Object p1);
    
    List<SessionListener> getListeners();
    
    void addListener(final SessionListener p0);
    
    void removeListener(final SessionListener p0);
    
    void callEvent(final SessionEvent p0);
    
    int getCompressionThreshold();
    
    void setCompressionThreshold(final int p0);
    
    int getConnectTimeout();
    
    void setConnectTimeout(final int p0);
    
    int getReadTimeout();
    
    void setReadTimeout(final int p0);
    
    int getWriteTimeout();
    
    void setWriteTimeout(final int p0);
    
    boolean isConnected();
    
    void send(final Packet p0);
    
    void disconnect(final String p0);
    
    void disconnect(final String p0, final boolean p1);
    
    void disconnect(final String p0, final Throwable p1);
    
    void disconnect(final String p0, final Throwable p1, final boolean p2);
}
