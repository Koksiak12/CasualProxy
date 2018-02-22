// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.protocol.packet.ingame.server.world;

import org.spacehq.packetlib.io.NetOutput;
import java.io.IOException;
import org.spacehq.packetlib.io.NetInput;
import org.spacehq.mc.protocol.data.game.values.world.map.MapData;
import org.spacehq.mc.protocol.data.game.values.world.map.MapPlayer;
import org.spacehq.packetlib.packet.Packet;

public class ServerMapDataPacket implements Packet
{
    private int mapId;
    private byte scale;
    private MapPlayer[] players;
    private MapData data;
    
    private ServerMapDataPacket() {
    }
    
    public ServerMapDataPacket(final int mapId, final byte scale, final MapPlayer[] players) {
        this(mapId, scale, players, null);
    }
    
    public ServerMapDataPacket(final int mapId, final byte scale, final MapPlayer[] players, final MapData data) {
        this.mapId = mapId;
        this.scale = scale;
        this.players = players;
        this.data = data;
    }
    
    public int getMapId() {
        return this.mapId;
    }
    
    public byte getScale() {
        return this.scale;
    }
    
    public MapPlayer[] getPlayers() {
        return this.players;
    }
    
    public MapData getData() {
        return this.data;
    }
    
    @Override
    public void read(final NetInput in) throws IOException {
        this.mapId = in.readVarInt();
        this.scale = in.readByte();
        this.players = new MapPlayer[in.readVarInt()];
        for (int index = 0; index < this.players.length; ++index) {
            final int data = in.readUnsignedByte();
            final int size = data >> 4 & 0xF;
            final int rotation = data & 0xF;
            final int x = in.readUnsignedByte();
            final int z = in.readUnsignedByte();
            this.players[index] = new MapPlayer(x, z, size, rotation);
        }
        final int columns = in.readUnsignedByte();
        if (columns > 0) {
            final int rows = in.readUnsignedByte();
            final int x2 = in.readUnsignedByte();
            final int y = in.readUnsignedByte();
            final byte[] data2 = in.readBytes(in.readVarInt());
            this.data = new MapData(columns, rows, x2, y, data2);
        }
    }
    
    @Override
    public void write(final NetOutput out) throws IOException {
        out.writeVarInt(this.mapId);
        out.writeByte(this.scale);
        out.writeVarInt(this.players.length);
        for (int index = 0; index < this.players.length; ++index) {
            final MapPlayer player = this.players[index];
            out.writeByte((player.getIconSize() & 0xF) << 4 | (player.getIconRotation() & 0xF));
            out.writeByte(player.getCenterX());
            out.writeByte(player.getCenterZ());
        }
        if (this.data != null && this.data.getColumns() != 0) {
            out.writeByte(this.data.getColumns());
            out.writeByte(this.data.getRows());
            out.writeByte(this.data.getX());
            out.writeByte(this.data.getY());
            out.writeVarInt(this.data.getData().length);
            out.writeBytes(this.data.getData());
        }
        else {
            out.writeByte(0);
        }
    }
    
    @Override
    public boolean isPriority() {
        return false;
    }
}
