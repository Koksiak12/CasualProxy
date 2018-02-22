// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.packetlib;

public interface ConnectionListener
{
    String getHost();
    
    int getPort();
    
    boolean isListening();
    
    void bind();
    
    void bind(final boolean p0);
    
    void bind(final boolean p0, final Runnable p1);
    
    void close();
    
    void close(final boolean p0);
    
    void close(final boolean p0, final Runnable p1);
}
