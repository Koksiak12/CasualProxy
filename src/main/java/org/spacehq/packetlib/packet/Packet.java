// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.packetlib.packet;

import org.spacehq.packetlib.io.NetOutput;
import java.io.IOException;
import org.spacehq.packetlib.io.NetInput;

public interface Packet
{
    void read(final NetInput p0) throws IOException;
    
    void write(final NetOutput p0) throws IOException;
    
    boolean isPriority();
}
