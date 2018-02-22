// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.packetlib.packet;

import java.lang.reflect.Constructor;
import org.spacehq.packetlib.Server;
import org.spacehq.packetlib.Session;
import org.spacehq.packetlib.Client;
import org.spacehq.packetlib.crypt.PacketEncryption;
import java.util.HashMap;
import java.util.Map;

public abstract class PacketProtocol
{
    private final Map<Integer, Class<? extends Packet>> incoming;
    private final Map<Class<? extends Packet>, Integer> outgoing;
    
    public PacketProtocol() {
        this.incoming = new HashMap<Integer, Class<? extends Packet>>();
        this.outgoing = new HashMap<Class<? extends Packet>, Integer>();
    }
    
    public abstract String getSRVRecordPrefix();
    
    public abstract PacketHeader getPacketHeader();
    
    public abstract PacketEncryption getEncryption();
    
    public abstract void newClientSession(final Client p0, final Session p1);
    
    public abstract void newServerSession(final Server p0, final Session p1);
    
    public final void clearPackets() {
        this.incoming.clear();
        this.outgoing.clear();
    }
    
    public final void register(final int id, final Class<? extends Packet> packet) {
        this.registerIncoming(id, packet);
        this.registerOutgoing(id, packet);
    }
    
    public final void registerIncoming(final int id, final Class<? extends Packet> packet) {
        this.incoming.put(id, packet);
        try {
            this.createIncomingPacket(id);
        }
        catch (IllegalStateException e) {
            this.incoming.remove(id);
            throw new IllegalArgumentException(e.getMessage(), e.getCause());
        }
    }
    
    public final void registerOutgoing(final int id, final Class<? extends Packet> packet) {
        this.outgoing.put(packet, id);
    }
    
    public final Packet createIncomingPacket(final int id) {
        if (id < 0 || !this.incoming.containsKey(id) || this.incoming.get(id) == null) {
            throw new IllegalArgumentException("Invalid packet id: " + id);
        }
        final Class<? extends Packet> packet = this.incoming.get(id);
        try {
            final Constructor<? extends Packet> constructor = packet.getDeclaredConstructor((Class<?>[])new Class[0]);
            if (!constructor.isAccessible()) {
                constructor.setAccessible(true);
            }
            return (Packet)constructor.newInstance(new Object[0]);
        }
        catch (NoSuchMethodError e2) {
            throw new IllegalStateException("Packet \"" + id + ", " + packet.getName() + "\" does not have a no-params constructor for instantiation.");
        }
        catch (Exception e) {
            throw new IllegalStateException("Failed to instantiate packet \"" + id + ", " + packet.getName() + "\".", e);
        }
    }
    
    public final int getOutgoingId(final Class<? extends Packet> packet) {
        if (!this.outgoing.containsKey(packet) || this.outgoing.get(packet) == null) {
            throw new IllegalArgumentException("Unregistered outgoing packet class: " + packet.getName());
        }
        return this.outgoing.get(packet);
    }
}
