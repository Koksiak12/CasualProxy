// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.protocol.packet.ingame.server;

import org.spacehq.packetlib.io.NetOutput;
import java.io.IOException;
import org.spacehq.mc.protocol.data.game.values.MagicValues;
import org.spacehq.packetlib.io.NetInput;
import org.spacehq.mc.protocol.data.game.values.MessageType;
import org.spacehq.mc.protocol.data.message.Message;
import org.spacehq.packetlib.packet.Packet;

public class ServerChatPacket implements Packet
{
    private Message message;
    private MessageType type;
    
    private ServerChatPacket() {
    }
    
    public ServerChatPacket(final String text) {
        this(Message.fromString(text));
    }
    
    public ServerChatPacket(final Message message) {
        this(message, MessageType.SYSTEM);
    }
    
    public ServerChatPacket(final String text, final MessageType type) {
        this(Message.fromString(text), type);
    }
    
    public ServerChatPacket(final Message message, final MessageType type) {
        this.message = message;
        this.type = type;
    }
    
    public Message getMessage() {
        return this.message;
    }
    
    public MessageType getType() {
        return this.type;
    }
    
    @Override
    public void read(final NetInput in) throws IOException {
        this.message = Message.fromString(in.readString());
        this.type = MagicValues.key(MessageType.class, in.readByte());
    }
    
    @Override
    public void write(final NetOutput out) throws IOException {
        out.writeString(this.message.toJsonString());
        out.writeByte(MagicValues.value(Integer.class, this.type));
    }
    
    @Override
    public boolean isPriority() {
        return false;
    }
}
