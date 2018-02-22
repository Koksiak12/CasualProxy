// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.protocol.packet.ingame.server;

import java.util.Iterator;
import org.spacehq.packetlib.io.NetOutput;
import java.io.IOException;
import java.util.UUID;
import org.spacehq.mc.protocol.data.message.Message;
import org.spacehq.mc.protocol.data.game.values.entity.player.GameMode;
import org.spacehq.mc.auth.data.GameProfile;
import org.spacehq.mc.protocol.data.game.values.MagicValues;
import org.spacehq.packetlib.io.NetInput;
import org.spacehq.mc.protocol.data.game.values.PlayerListEntry;
import org.spacehq.mc.protocol.data.game.values.PlayerListEntryAction;
import org.spacehq.packetlib.packet.Packet;

public class ServerPlayerListEntryPacket implements Packet
{
    private PlayerListEntryAction action;
    private PlayerListEntry[] entries;
    
    private ServerPlayerListEntryPacket() {
    }
    
    public ServerPlayerListEntryPacket(final PlayerListEntryAction action, final PlayerListEntry[] entries) {
        this.action = action;
        this.entries = entries;
    }
    
    public PlayerListEntryAction getAction() {
        return this.action;
    }
    
    public PlayerListEntry[] getEntries() {
        return this.entries;
    }
    
    @Override
    public void read(final NetInput in) throws IOException {
        this.action = MagicValues.key(PlayerListEntryAction.class, in.readVarInt());
        this.entries = new PlayerListEntry[in.readVarInt()];
        for (int count = 0; count < this.entries.length; ++count) {
            final UUID uuid = in.readUUID();
            GameProfile profile;
            if (this.action == PlayerListEntryAction.ADD_PLAYER) {
                profile = new GameProfile(uuid, in.readString());
            }
            else {
                profile = new GameProfile(uuid, null);
            }
            PlayerListEntry entry = null;
            switch (this.action) {
                case ADD_PLAYER: {
                    for (int properties = in.readVarInt(), index = 0; index < properties; ++index) {
                        final String propertyName = in.readString();
                        final String value = in.readString();
                        String signature = null;
                        if (in.readBoolean()) {
                            signature = in.readString();
                        }
                        profile.getProperties().add(new GameProfile.Property(propertyName, value, signature));
                    }
                    final GameMode gameMode = MagicValues.key(GameMode.class, in.readVarInt());
                    final int ping = in.readVarInt();
                    Message displayName = null;
                    if (in.readBoolean()) {
                        displayName = Message.fromString(in.readString());
                    }
                    entry = new PlayerListEntry(profile, gameMode, ping, displayName);
                    break;
                }
                case UPDATE_GAMEMODE: {
                    final GameMode mode = MagicValues.key(GameMode.class, in.readVarInt());
                    entry = new PlayerListEntry(profile, mode);
                    break;
                }
                case UPDATE_LATENCY: {
                    final int png = in.readVarInt();
                    entry = new PlayerListEntry(profile, png);
                    break;
                }
                case UPDATE_DISPLAY_NAME: {
                    Message disp = null;
                    if (in.readBoolean()) {
                        disp = Message.fromString(in.readString());
                    }
                    entry = new PlayerListEntry(profile, disp);
                }
                case REMOVE_PLAYER: {
                    entry = new PlayerListEntry(profile);
                    break;
                }
            }
            this.entries[count] = entry;
        }
    }
    
    @Override
    public void write(final NetOutput out) throws IOException {
        out.writeVarInt(MagicValues.value(Integer.class, this.action));
        out.writeVarInt(this.entries.length);
        for (final PlayerListEntry entry : this.entries) {
            out.writeUUID(entry.getProfile().getId());
            switch (this.action) {
                case ADD_PLAYER: {
                    out.writeString(entry.getProfile().getName());
                    out.writeVarInt(entry.getProfile().getProperties().size());
                    for (final GameProfile.Property property : entry.getProfile().getProperties()) {
                        out.writeString(property.getName());
                        out.writeString(property.getValue());
                        out.writeBoolean(property.hasSignature());
                        if (property.hasSignature()) {
                            out.writeString(property.getSignature());
                        }
                    }
                    out.writeVarInt(MagicValues.value(Integer.class, entry.getGameMode()));
                    out.writeVarInt(entry.getPing());
                    out.writeBoolean(entry.getDisplayName() != null);
                    if (entry.getDisplayName() != null) {
                        out.writeString(entry.getDisplayName().toJsonString());
                        break;
                    }
                    break;
                }
                case UPDATE_GAMEMODE: {
                    out.writeVarInt(MagicValues.value(Integer.class, entry.getGameMode()));
                    break;
                }
                case UPDATE_LATENCY: {
                    out.writeVarInt(entry.getPing());
                    break;
                }
                case UPDATE_DISPLAY_NAME: {
                    out.writeBoolean(entry.getDisplayName() != null);
                    if (entry.getDisplayName() != null) {
                        out.writeString(entry.getDisplayName().toJsonString());
                        break;
                    }
                    break;
                }
            }
        }
    }
    
    @Override
    public boolean isPriority() {
        return false;
    }
}
