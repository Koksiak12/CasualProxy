// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.protocol.packet.ingame.server.world;

import org.spacehq.packetlib.io.NetOutput;
import java.io.IOException;
import org.spacehq.mc.protocol.data.game.Position;
import org.spacehq.mc.protocol.util.NetUtil;
import org.spacehq.packetlib.io.NetInput;
import org.spacehq.mc.protocol.data.game.values.world.block.BlockChangeRecord;
import org.spacehq.packetlib.packet.Packet;

public class ServerBlockChangePacket implements Packet
{
    private BlockChangeRecord record;
    
    private ServerBlockChangePacket() {
    }
    
    public ServerBlockChangePacket(final BlockChangeRecord record) {
        this.record = record;
    }
    
    public BlockChangeRecord getRecord() {
        return this.record;
    }
    
    @Override
    public void read(final NetInput in) throws IOException {
        final Position position = NetUtil.readPosition(in);
        final int block = in.readVarInt();
        this.record = new BlockChangeRecord(position, block >> 4, block & 0xF);
    }
    
    @Override
    public void write(final NetOutput out) throws IOException {
        NetUtil.writePosition(out, this.record.getPosition());
        out.writeVarInt(this.record.getId() << 4 | (this.record.getData() & 0xF));
    }
    
    @Override
    public boolean isPriority() {
        return false;
    }
}
