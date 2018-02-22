// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.protocol.packet.ingame.server;

import java.util.Iterator;
import org.spacehq.packetlib.io.NetOutput;
import java.io.IOException;
import org.spacehq.mc.protocol.data.game.values.statistic.GenericStatistic;
import org.spacehq.mc.protocol.data.game.values.statistic.BreakItemStatistic;
import org.spacehq.mc.protocol.data.game.values.statistic.UseItemStatistic;
import org.spacehq.mc.protocol.data.game.values.statistic.BreakBlockStatistic;
import org.spacehq.mc.protocol.data.game.values.statistic.CraftItemStatistic;
import org.spacehq.mc.protocol.data.game.values.MagicValues;
import org.spacehq.mc.protocol.data.game.values.statistic.Achievement;
import org.spacehq.packetlib.io.NetInput;
import java.util.HashMap;
import org.spacehq.mc.protocol.data.game.values.statistic.Statistic;
import java.util.Map;
import org.spacehq.packetlib.packet.Packet;

public class ServerStatisticsPacket implements Packet
{
    private static final String CRAFT_ITEM_PREFIX = "stats.craftItem.";
    private static final String BREAK_BLOCK_PREFIX = "stats.mineBlock.";
    private static final String USE_ITEM_PREFIX = "stats.useItem.";
    private static final String BREAK_ITEM_PREFIX = "stats.breakItem.";
    private Map<Statistic, Integer> statistics;
    
    private ServerStatisticsPacket() {
        this.statistics = new HashMap<Statistic, Integer>();
    }
    
    public ServerStatisticsPacket(final Map<Statistic, Integer> statistics) {
        this.statistics = new HashMap<Statistic, Integer>();
        this.statistics = statistics;
    }
    
    public Map<Statistic, Integer> getStatistics() {
        return this.statistics;
    }
    
    @Override
    public void read(final NetInput in) throws IOException {
        for (int length = in.readVarInt(), index = 0; index < length; ++index) {
            final String value = in.readString();
            Statistic statistic = null;
            if (value.startsWith("achievement.")) {
                statistic = MagicValues.key(Achievement.class, value);
            }
            else if (value.startsWith("stats.craftItem.")) {
                statistic = new CraftItemStatistic(Integer.parseInt(value.substring(value.lastIndexOf(".") + 1)));
            }
            else if (value.startsWith("stats.mineBlock.")) {
                statistic = new BreakBlockStatistic(Integer.parseInt(value.substring(value.lastIndexOf(".") + 1)));
            }
            else if (value.startsWith("stats.useItem.")) {
                statistic = new UseItemStatistic(Integer.parseInt(value.substring(value.lastIndexOf(".") + 1)));
            }
            else if (value.startsWith("stats.breakItem.")) {
                statistic = new BreakItemStatistic(Integer.parseInt(value.substring(value.lastIndexOf(".") + 1)));
            }
            else {
                statistic = MagicValues.key(GenericStatistic.class, value);
            }
            this.statistics.put(statistic, in.readVarInt());
        }
    }
    
    @Override
    public void write(final NetOutput out) throws IOException {
        out.writeVarInt(this.statistics.size());
        for (final Statistic statistic : this.statistics.keySet()) {
            String value = "";
            if (statistic instanceof Achievement) {
                value = MagicValues.value(String.class, (Enum<?>)statistic);
            }
            else if (statistic instanceof CraftItemStatistic) {
                value = "stats.craftItem." + ((CraftItemStatistic)statistic).getId();
            }
            else if (statistic instanceof BreakBlockStatistic) {
                value = "stats.mineBlock." + ((CraftItemStatistic)statistic).getId();
            }
            else if (statistic instanceof UseItemStatistic) {
                value = "stats.useItem." + ((CraftItemStatistic)statistic).getId();
            }
            else if (statistic instanceof BreakItemStatistic) {
                value = "stats.breakItem." + ((CraftItemStatistic)statistic).getId();
            }
            else if (statistic instanceof GenericStatistic) {
                value = MagicValues.value(String.class, (Enum<?>)statistic);
            }
            out.writeString(value);
            out.writeVarInt(this.statistics.get(statistic));
        }
    }
    
    @Override
    public boolean isPriority() {
        return false;
    }
}
