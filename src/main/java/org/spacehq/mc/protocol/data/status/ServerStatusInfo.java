// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.protocol.data.status;

import java.awt.image.BufferedImage;
import org.spacehq.mc.protocol.data.message.Message;

public class ServerStatusInfo
{
    private VersionInfo version;
    private PlayerInfo players;
    private Message description;
    private BufferedImage icon;
    
    public ServerStatusInfo(final VersionInfo version, final PlayerInfo players, final Message description, final BufferedImage icon) {
        this.version = version;
        this.players = players;
        this.description = description;
        this.icon = icon;
    }
    
    public VersionInfo getVersionInfo() {
        return this.version;
    }
    
    public PlayerInfo getPlayerInfo() {
        return this.players;
    }
    
    public Message getDescription() {
        return this.description;
    }
    
    public BufferedImage getIcon() {
        return this.icon;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final ServerStatusInfo that = (ServerStatusInfo)o;
        if (!this.description.equals(that.description)) {
            return false;
        }
        if (this.icon != null) {
            if (this.icon.equals(that.icon)) {
                return this.players.equals(that.players) && this.version.equals(that.version);
            }
        }
        else if (that.icon == null) {
            return this.players.equals(that.players) && this.version.equals(that.version);
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        int result = this.version.hashCode();
        result = 31 * result + this.players.hashCode();
        result = 31 * result + this.description.hashCode();
        result = 31 * result + ((this.icon != null) ? this.icon.hashCode() : 0);
        return result;
    }
}
