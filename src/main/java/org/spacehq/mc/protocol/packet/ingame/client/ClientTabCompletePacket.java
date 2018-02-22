// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.protocol.packet.ingame.client;

import org.spacehq.packetlib.io.NetOutput;
import java.io.IOException;
import org.spacehq.mc.protocol.util.NetUtil;
import org.spacehq.packetlib.io.NetInput;
import org.spacehq.mc.protocol.data.game.Position;
import org.spacehq.packetlib.packet.Packet;

public class ClientTabCompletePacket implements Packet
{
    private String text;
    private Position position;
    
    private ClientTabCompletePacket() {
    }
    
    public ClientTabCompletePacket(final String text) {
        this(text, null);
    }
    
    public ClientTabCompletePacket(final String text, final Position position) {
        this.text = text;
        this.position = position;
    }
    
    public String getText() {
        return this.text;
    }
    
    @Override
    public void read(final NetInput in) throws IOException {
        this.text = in.readString();
        this.position = (in.readBoolean() ? NetUtil.readPosition(in) : null);
    }
    
    @Override
    public void write(final NetOutput out) throws IOException {
        out.writeString(this.text);
        out.writeBoolean(this.position != null);
        if (this.position != null) {
            NetUtil.writePosition(out, this.position);
        }
    }
    
    @Override
    public boolean isPriority() {
        return false;
    }
}
