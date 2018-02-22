// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.protocol.packet.ingame.client;

import java.util.Iterator;
import org.spacehq.packetlib.io.NetOutput;
import java.io.IOException;
import java.util.ArrayList;
import org.spacehq.mc.protocol.data.game.values.MagicValues;
import org.spacehq.packetlib.io.NetInput;
import java.util.Arrays;
import org.spacehq.mc.protocol.data.game.values.setting.SkinPart;
import java.util.List;
import org.spacehq.mc.protocol.data.game.values.setting.ChatVisibility;
import org.spacehq.packetlib.packet.Packet;

public class ClientSettingsPacket implements Packet
{
    private String locale;
    private int renderDistance;
    private ChatVisibility chatVisibility;
    private boolean chatColors;
    private List<SkinPart> visibleParts;
    
    private ClientSettingsPacket() {
    }
    
    public ClientSettingsPacket(final String locale, final int renderDistance, final ChatVisibility chatVisibility, final boolean chatColors, final SkinPart... visibleParts) {
        this.locale = locale;
        this.renderDistance = renderDistance;
        this.chatVisibility = chatVisibility;
        this.chatColors = chatColors;
        this.visibleParts = Arrays.asList(visibleParts);
    }
    
    public String getLocale() {
        return this.locale;
    }
    
    public int getRenderDistance() {
        return this.renderDistance;
    }
    
    public ChatVisibility getChatVisibility() {
        return this.chatVisibility;
    }
    
    public boolean getUseChatColors() {
        return this.chatColors;
    }
    
    public List<SkinPart> getVisibleParts() {
        return this.visibleParts;
    }
    
    @Override
    public void read(final NetInput in) throws IOException {
        this.locale = in.readString();
        this.renderDistance = in.readByte();
        this.chatVisibility = MagicValues.key(ChatVisibility.class, in.readByte());
        this.chatColors = in.readBoolean();
        this.visibleParts = new ArrayList<SkinPart>();
        final int flags = in.readUnsignedByte();
        for (final SkinPart part : SkinPart.values()) {
            final int bit = 1 << part.ordinal();
            if ((flags & bit) == bit) {
                this.visibleParts.add(part);
            }
        }
    }
    
    @Override
    public void write(final NetOutput out) throws IOException {
        out.writeString(this.locale);
        out.writeByte(this.renderDistance);
        out.writeByte(MagicValues.value(Integer.class, this.chatVisibility));
        out.writeBoolean(this.chatColors);
        int flags = 0;
        for (final SkinPart part : this.visibleParts) {
            flags |= 1 << part.ordinal();
        }
        out.writeByte(flags);
    }
    
    @Override
    public boolean isPriority() {
        return false;
    }
}
