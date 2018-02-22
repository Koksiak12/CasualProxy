// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.packetlib.packet;

import org.spacehq.packetlib.io.NetOutput;
import java.io.IOException;
import org.spacehq.packetlib.io.NetInput;

public class DefaultPacketHeader implements PacketHeader
{
    @Override
    public boolean isLengthVariable() {
        return true;
    }
    
    @Override
    public int getLengthSize() {
        return 5;
    }
    
    @Override
    public int getLengthSize(final int length) {
        if ((length & 0xFFFFFF80) == 0x0) {
            return 1;
        }
        if ((length & 0xFFFFC000) == 0x0) {
            return 2;
        }
        if ((length & 0xFFE00000) == 0x0) {
            return 3;
        }
        if ((length & 0xF0000000) == 0x0) {
            return 4;
        }
        return 5;
    }
    
    @Override
    public int readLength(final NetInput in, final int available) throws IOException {
        return in.readVarInt();
    }
    
    @Override
    public void writeLength(final NetOutput out, final int length) throws IOException {
        out.writeVarInt(length);
    }
    
    @Override
    public int readPacketId(final NetInput in) throws IOException {
        return in.readVarInt();
    }
    
    @Override
    public void writePacketId(final NetOutput out, final int packetId) throws IOException {
        out.writeVarInt(packetId);
    }
}
