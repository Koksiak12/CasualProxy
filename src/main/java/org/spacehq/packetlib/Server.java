// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.packetlib;

import org.spacehq.packetlib.event.server.ServerClosedEvent;
import org.spacehq.packetlib.event.server.ServerClosingEvent;
import org.spacehq.packetlib.event.server.SessionRemovedEvent;
import org.spacehq.packetlib.event.server.SessionAddedEvent;
import java.util.Iterator;
import java.util.Collection;
import java.lang.reflect.Constructor;
import org.spacehq.packetlib.event.server.ServerEvent;
import org.spacehq.packetlib.event.server.ServerBoundEvent;
import java.util.HashMap;
import java.util.ArrayList;
import org.spacehq.packetlib.event.server.ServerListener;
import java.util.Map;
import java.util.List;
import org.spacehq.packetlib.packet.PacketProtocol;

public class Server
{
    private String host;
    private int port;
    private Class<? extends PacketProtocol> protocol;
    private SessionFactory factory;
    private ConnectionListener listener;
    private List<Session> sessions;
    private Map<String, Object> flags;
    private List<ServerListener> listeners;
    
    public Server(final String host, final int port, final Class<? extends PacketProtocol> protocol, final SessionFactory factory) {
        this.sessions = new ArrayList<Session>();
        this.flags = new HashMap<String, Object>();
        this.listeners = new ArrayList<ServerListener>();
        this.host = host;
        this.port = port;
        this.protocol = protocol;
        this.factory = factory;
    }
    
    public Server bind() {
        return this.bind(true);
    }
    
    public Server bind(final boolean wait) {
        (this.listener = this.factory.createServerListener(this)).bind(wait, new Runnable() {
            @Override
            public void run() {
                Server.this.callEvent(new ServerBoundEvent(Server.this));
            }
        });
        return this;
    }
    
    public String getHost() {
        return this.host;
    }
    
    public int getPort() {
        return this.port;
    }
    
    public Class<? extends PacketProtocol> getPacketProtocol() {
        return this.protocol;
    }
    
    public PacketProtocol createPacketProtocol() {
        try {
            final Constructor<? extends PacketProtocol> constructor = this.protocol.getDeclaredConstructor((Class<?>[])new Class[0]);
            if (!constructor.isAccessible()) {
                constructor.setAccessible(true);
            }
            return (PacketProtocol)constructor.newInstance(new Object[0]);
        }
        catch (NoSuchMethodError e2) {
            throw new IllegalStateException("PacketProtocol \"" + this.protocol.getName() + "\" does not have a no-params constructor for instantiation.");
        }
        catch (Exception e) {
            throw new IllegalStateException("Failed to instantiate PacketProtocol " + this.protocol.getName() + ".", e);
        }
    }
    
    public Map<String, Object> getGlobalFlags() {
        return new HashMap<String, Object>(this.flags);
    }
    
    public boolean hasGlobalFlag(final String key) {
        return this.flags.containsKey(key);
    }
    
    public <T> T getGlobalFlag(final String key) {
        final Object value = this.flags.get(key);
        if (value == null) {
            return null;
        }
        try {
            return (T)value;
        }
        catch (ClassCastException e) {
            throw new IllegalStateException("Tried to get flag \"" + key + "\" as the wrong type. Actual type: " + value.getClass().getName());
        }
    }
    
    public void setGlobalFlag(final String key, final Object value) {
        this.flags.put(key, value);
    }
    
    public List<ServerListener> getListeners() {
        return new ArrayList<ServerListener>(this.listeners);
    }
    
    public void addListener(final ServerListener listener) {
        this.listeners.add(listener);
    }
    
    public void removeListener(final ServerListener listener) {
        this.listeners.remove(listener);
    }
    
    public void callEvent(final ServerEvent event) {
        for (final ServerListener listener : this.listeners) {
            event.call(listener);
        }
    }
    
    public List<Session> getSessions() {
        return new ArrayList<Session>(this.sessions);
    }
    
    public void addSession(final Session session) {
        this.sessions.add(session);
        this.callEvent(new SessionAddedEvent(this, session));
    }
    
    public void removeSession(final Session session) {
        this.sessions.remove(session);
        if (session.isConnected()) {
            session.disconnect("Connection closed.");
        }
        this.callEvent(new SessionRemovedEvent(this, session));
    }
    
    public boolean isListening() {
        return this.listener.isListening();
    }
    
    public void close() {
        this.close(true);
    }
    
    public void close(final boolean wait) {
        this.callEvent(new ServerClosingEvent(this));
        for (final Session session : this.getSessions()) {
            if (session.isConnected()) {
                session.disconnect("Server closed.");
            }
        }
        this.listener.close(wait, new Runnable() {
            @Override
            public void run() {
                Server.this.callEvent(new ServerClosedEvent(Server.this));
            }
        });
    }
}
