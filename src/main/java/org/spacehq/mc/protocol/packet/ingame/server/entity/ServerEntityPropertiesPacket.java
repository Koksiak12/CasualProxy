// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.protocol.packet.ingame.server.entity;

import java.util.Iterator;
import org.spacehq.packetlib.io.NetOutput;
import java.io.IOException;
import org.spacehq.mc.protocol.data.game.values.entity.AttributeType;
import org.spacehq.mc.protocol.data.game.values.MagicValues;
import org.spacehq.mc.protocol.data.game.values.entity.ModifierOperation;
import org.spacehq.mc.protocol.data.game.attribute.AttributeModifier;
import java.util.ArrayList;
import org.spacehq.packetlib.io.NetInput;
import org.spacehq.mc.protocol.data.game.attribute.Attribute;
import java.util.List;
import org.spacehq.packetlib.packet.Packet;

public class ServerEntityPropertiesPacket implements Packet
{
    private int entityId;
    private List<Attribute> attributes;
    
    private ServerEntityPropertiesPacket() {
    }
    
    public ServerEntityPropertiesPacket(final int entityId, final List<Attribute> attributes) {
        this.entityId = entityId;
        this.attributes = attributes;
    }
    
    public int getEntityId() {
        return this.entityId;
    }
    
    public List<Attribute> getAttributes() {
        return this.attributes;
    }
    
    @Override
    public void read(final NetInput in) throws IOException {
        this.entityId = in.readVarInt();
        this.attributes = new ArrayList<Attribute>();
        for (int length = in.readInt(), index = 0; index < length; ++index) {
            final String key = in.readString();
            final double value = in.readDouble();
            final List<AttributeModifier> modifiers = new ArrayList<AttributeModifier>();
            for (int len = in.readVarInt(), ind = 0; ind < len; ++ind) {
                modifiers.add(new AttributeModifier(in.readUUID(), in.readDouble(), MagicValues.key(ModifierOperation.class, in.readByte())));
            }
            this.attributes.add(new Attribute(MagicValues.key(AttributeType.class, key), value, modifiers));
        }
    }
    
    @Override
    public void write(final NetOutput out) throws IOException {
        out.writeVarInt(this.entityId);
        out.writeInt(this.attributes.size());
        for (final Attribute attribute : this.attributes) {
            out.writeString(MagicValues.value(String.class, attribute.getType()));
            out.writeDouble(attribute.getValue());
            out.writeVarInt(attribute.getModifiers().size());
            for (final AttributeModifier modifier : attribute.getModifiers()) {
                out.writeUUID(modifier.getUUID());
                out.writeDouble(modifier.getAmount());
                out.writeByte(MagicValues.value(Integer.class, modifier.getOperation()));
            }
        }
    }
    
    @Override
    public boolean isPriority() {
        return false;
    }
}
