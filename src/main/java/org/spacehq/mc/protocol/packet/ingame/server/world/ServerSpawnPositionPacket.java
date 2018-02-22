// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.protocol.packet.ingame.server.world;

import org.spacehq.packetlib.io.NetOutput;
import java.io.IOException;
import org.spacehq.mc.protocol.util.NetUtil;
import org.spacehq.packetlib.io.NetInput;
import org.spacehq.mc.protocol.data.game.Position;
import org.spacehq.packetlib.packet.Packet;

public class ServerSpawnPositionPacket implements Packet
{
    private Position position;
    
    private ServerSpawnPositionPacket() {
    }
    
    public ServerSpawnPositionPacket(final Position position) {
        this.position = position;
    }
    
    public Position getPosition() {
        return this.position;
    }
    
    @Override
    public void read(final NetInput in) throws IOException {
        this.position = NetUtil.readPosition(in);
    }
    
    @Override
    public void write(final NetOutput out) throws IOException {
        NetUtil.writePosition(out, this.position);
    }
    
    @Override
    public boolean isPriority() {
        return false;
    }
}
