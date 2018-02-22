// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.protocol.packet.ingame.client.player;

import org.spacehq.packetlib.io.NetOutput;
import java.io.IOException;
import org.spacehq.mc.protocol.util.NetUtil;
import org.spacehq.mc.protocol.data.game.values.MagicValues;
import org.spacehq.packetlib.io.NetInput;
import org.spacehq.mc.protocol.data.game.values.Face;
import org.spacehq.mc.protocol.data.game.Position;
import org.spacehq.mc.protocol.data.game.values.entity.player.PlayerAction;
import org.spacehq.packetlib.packet.Packet;

public class ClientPlayerActionPacket implements Packet
{
    private PlayerAction action;
    private Position position;
    private Face face;
    
    private ClientPlayerActionPacket() {
    }
    
    public ClientPlayerActionPacket(final PlayerAction action, final Position position, final Face face) {
        this.action = action;
        this.position = position;
        this.face = face;
    }
    
    public PlayerAction getAction() {
        return this.action;
    }
    
    public Position getPosition() {
        return this.position;
    }
    
    public Face getFace() {
        return this.face;
    }
    
    @Override
    public void read(final NetInput in) throws IOException {
        this.action = MagicValues.key(PlayerAction.class, in.readUnsignedByte());
        this.position = NetUtil.readPosition(in);
        this.face = MagicValues.key(Face.class, in.readUnsignedByte());
    }
    
    @Override
    public void write(final NetOutput out) throws IOException {
        out.writeByte(MagicValues.value(Integer.class, this.action));
        NetUtil.writePosition(out, this.position);
        out.writeByte(MagicValues.value(Integer.class, this.face));
    }
    
    @Override
    public boolean isPriority() {
        return false;
    }
}
