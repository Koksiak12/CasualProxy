// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.protocol.packet.ingame.server;

import org.spacehq.packetlib.io.NetOutput;
import java.io.IOException;
import org.spacehq.packetlib.io.NetInput;
import org.spacehq.packetlib.packet.Packet;

public class ServerSwitchCameraPacket implements Packet
{
    private int cameraEntityId;
    
    private ServerSwitchCameraPacket() {
    }
    
    public ServerSwitchCameraPacket(final int cameraEntityId) {
        this.cameraEntityId = cameraEntityId;
    }
    
    public int getCameraEntityId() {
        return this.cameraEntityId;
    }
    
    @Override
    public void read(final NetInput in) throws IOException {
        this.cameraEntityId = in.readVarInt();
    }
    
    @Override
    public void write(final NetOutput out) throws IOException {
        out.writeVarInt(this.cameraEntityId);
    }
    
    @Override
    public boolean isPriority() {
        return false;
    }
}
