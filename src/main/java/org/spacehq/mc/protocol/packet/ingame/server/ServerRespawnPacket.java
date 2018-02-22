// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.protocol.packet.ingame.server;

import org.spacehq.packetlib.io.NetOutput;
import java.io.IOException;
import org.spacehq.mc.protocol.data.game.values.MagicValues;
import org.spacehq.packetlib.io.NetInput;
import org.spacehq.mc.protocol.data.game.values.world.WorldType;
import org.spacehq.mc.protocol.data.game.values.entity.player.GameMode;
import org.spacehq.mc.protocol.data.game.values.setting.Difficulty;
import org.spacehq.packetlib.packet.Packet;

public class ServerRespawnPacket implements Packet
{
    private int dimension;
    private Difficulty difficulty;
    private GameMode gamemode;
    private WorldType worldType;
    
    private ServerRespawnPacket() {
    }
    
    public ServerRespawnPacket(final int dimension, final Difficulty difficulty, final GameMode gamemode, final WorldType worldType) {
        this.dimension = dimension;
        this.difficulty = difficulty;
        this.gamemode = gamemode;
        this.worldType = worldType;
    }
    
    public int getDimension() {
        return this.dimension;
    }
    
    public Difficulty getDifficulty() {
        return this.difficulty;
    }
    
    public GameMode getGameMode() {
        return this.gamemode;
    }
    
    public WorldType getWorldType() {
        return this.worldType;
    }
    
    @Override
    public void read(final NetInput in) throws IOException {
        this.dimension = in.readInt();
        this.difficulty = MagicValues.key(Difficulty.class, in.readUnsignedByte());
        this.gamemode = MagicValues.key(GameMode.class, in.readUnsignedByte());
        this.worldType = MagicValues.key(WorldType.class, in.readString().toLowerCase());
    }
    
    @Override
    public void write(final NetOutput out) throws IOException {
        out.writeInt(this.dimension);
        out.writeByte(MagicValues.value(Integer.class, this.difficulty));
        out.writeByte(MagicValues.value(Integer.class, this.gamemode));
        out.writeString(MagicValues.value(String.class, this.worldType));
    }
    
    @Override
    public boolean isPriority() {
        return false;
    }
}
