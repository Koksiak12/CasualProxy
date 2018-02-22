// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.packetlib.packet;

import org.spacehq.packetlib.io.NetOutput;
import java.io.IOException;
import org.spacehq.packetlib.io.NetInput;

public interface PacketHeader
{
    boolean isLengthVariable();
    
    int getLengthSize();
    
    int getLengthSize(final int p0);
    
    int readLength(final NetInput p0, final int p1) throws IOException;
    
    void writeLength(final NetOutput p0, final int p1) throws IOException;
    
    int readPacketId(final NetInput p0) throws IOException;
    
    void writePacketId(final NetOutput p0, final int p1) throws IOException;
}
