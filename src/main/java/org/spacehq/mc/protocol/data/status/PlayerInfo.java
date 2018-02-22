// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.protocol.data.status;

import java.util.Arrays;
import org.spacehq.mc.auth.data.GameProfile;

public class PlayerInfo
{
    private int max;
    private int online;
    private GameProfile[] players;
    
    public PlayerInfo(final int max, final int online, final GameProfile[] players) {
        this.max = max;
        this.online = online;
        this.players = players;
    }
    
    public int getMaxPlayers() {
        return this.max;
    }
    
    public int getOnlinePlayers() {
        return this.online;
    }
    
    public GameProfile[] getPlayers() {
        return this.players;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final PlayerInfo that = (PlayerInfo)o;
        return this.max == that.max && this.online == that.online && Arrays.equals(this.players, that.players);
    }
    
    @Override
    public int hashCode() {
        int result = this.max;
        result = 31 * result + this.online;
        result = 31 * result + Arrays.hashCode(this.players);
        return result;
    }
}
