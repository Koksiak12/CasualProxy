// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.protocol.packet.ingame.server.world;

import org.spacehq.packetlib.io.NetOutput;
import java.io.IOException;
import org.spacehq.mc.protocol.util.NetUtil;
import org.spacehq.packetlib.io.NetInput;
import org.spacehq.mc.protocol.data.message.Message;
import org.spacehq.mc.protocol.data.game.Position;
import org.spacehq.packetlib.packet.Packet;

public class ServerUpdateSignPacket implements Packet
{
    private Position position;
    private Message[] lines;
    
    private ServerUpdateSignPacket() {
    }
    
    public ServerUpdateSignPacket(final Position position, final String[] lines) {
        this(position, toMessages(lines));
    }
    
    public ServerUpdateSignPacket(final Position position, final Message[] lines) {
        if (lines.length != 4) {
            throw new IllegalArgumentException("Lines must contain exactly 4 strings!");
        }
        this.position = position;
        this.lines = lines;
    }
    
    public Position getPosition() {
        return this.position;
    }
    
    public Message[] getLines() {
        return this.lines;
    }
    
    @Override
    public void read(final NetInput in) throws IOException {
        this.position = NetUtil.readPosition(in);
        this.lines = new Message[4];
        for (int count = 0; count < this.lines.length; ++count) {
            this.lines[count] = Message.fromString(in.readString());
        }
    }
    
    @Override
    public void write(final NetOutput out) throws IOException {
        NetUtil.writePosition(out, this.position);
        for (final Message line : this.lines) {
            out.writeString(line.toJsonString());
        }
    }
    
    @Override
    public boolean isPriority() {
        return false;
    }
    
    private static Message[] toMessages(final String[] lines) {
        final Message[] messages = new Message[lines.length];
        for (int index = 0; index < lines.length; ++index) {
            messages[index] = Message.fromString(lines[index]);
        }
        return messages;
    }
}
