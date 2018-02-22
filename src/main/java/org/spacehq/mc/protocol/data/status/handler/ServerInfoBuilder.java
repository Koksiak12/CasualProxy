// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.protocol.data.status.handler;

import org.spacehq.mc.protocol.data.status.ServerStatusInfo;
import org.spacehq.packetlib.Session;

public interface ServerInfoBuilder
{
    ServerStatusInfo buildInfo(final Session p0);
}
