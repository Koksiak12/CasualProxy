// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.protocol.packet.ingame.server.world;

import org.spacehq.packetlib.io.NetOutput;
import java.io.IOException;
import org.spacehq.mc.protocol.data.game.Position;
import org.spacehq.packetlib.io.NetInput;
import org.spacehq.mc.protocol.data.game.values.world.block.BlockChangeRecord;
import org.spacehq.packetlib.packet.Packet;

public class ServerMultiBlockChangePacket implements Packet
{
    private BlockChangeRecord[] records;
    
    private ServerMultiBlockChangePacket() {
    }
    
    public ServerMultiBlockChangePacket(final BlockChangeRecord... records) {
        if (records == null || records.length == 0) {
            throw new IllegalArgumentException("Records must contain at least 1 value.");
        }
        this.records = records;
    }
    
    public BlockChangeRecord[] getRecords() {
        return this.records;
    }
    
    @Override
    public void read(final NetInput in) throws IOException {
        final int chunkX = in.readInt();
        final int chunkZ = in.readInt();
        this.records = new BlockChangeRecord[in.readVarInt()];
        for (int index = 0; index < this.records.length; ++index) {
            final short pos = in.readShort();
            final int block = in.readVarInt();
            final int x = (chunkX << 4) + (pos >> 12 & 0xF);
            final int y = pos & 0xFF;
            final int z = (chunkZ << 4) + (pos >> 8 & 0xF);
            this.records[index] = new BlockChangeRecord(new Position(x, y, z), block >> 4, block & 0xF);
        }
    }
    
    @Override
    public void write(final NetOutput out) throws IOException {
        final int chunkX = this.records[0].getPosition().getX() >> 4;
        final int chunkZ = this.records[0].getPosition().getZ() >> 4;
        out.writeInt(chunkX);
        out.writeInt(chunkZ);
        out.writeVarInt(this.records.length);
        for (final BlockChangeRecord record : this.records) {
            out.writeShort(record.getPosition().getX() - (chunkX << 4) << 12 | record.getPosition().getZ() - (chunkZ << 4) << 8 | record.getPosition().getY());
            out.writeVarInt(record.getId() << 4 | (record.getData() & 0xF));
        }
    }
    
    @Override
    public boolean isPriority() {
        return false;
    }
}
