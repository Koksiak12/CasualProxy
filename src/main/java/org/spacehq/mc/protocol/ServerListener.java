// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.protocol;

import org.spacehq.mc.protocol.data.game.values.HandshakeIntent;
import org.spacehq.mc.protocol.packet.ingame.server.ServerKeepAlivePacket;
import org.spacehq.mc.protocol.packet.login.server.LoginSuccessPacket;
import org.spacehq.mc.protocol.packet.login.server.LoginSetCompressionPacket;
import org.spacehq.mc.auth.data.GameProfile;
import java.util.UUID;
import org.spacehq.mc.auth.exception.request.RequestException;
import java.math.BigInteger;
import org.spacehq.mc.auth.service.SessionService;
import java.net.Proxy;
import org.spacehq.packetlib.Session;
import org.spacehq.mc.protocol.util.CryptUtil;
import org.spacehq.mc.protocol.packet.ingame.server.ServerDisconnectPacket;
import org.spacehq.mc.protocol.packet.login.server.LoginDisconnectPacket;
import org.spacehq.packetlib.event.session.DisconnectingEvent;
import org.spacehq.mc.protocol.data.status.ServerStatusInfo;
import java.security.PrivateKey;
import org.spacehq.mc.protocol.packet.ingame.client.ClientKeepAlivePacket;
import org.spacehq.mc.protocol.packet.status.server.StatusPongPacket;
import org.spacehq.mc.protocol.packet.status.client.StatusPingPacket;
import org.spacehq.mc.protocol.packet.status.server.StatusResponsePacket;
import org.spacehq.mc.protocol.data.status.handler.ServerInfoBuilder;
import org.spacehq.mc.protocol.packet.status.client.StatusQueryPacket;
import java.security.Key;
import java.util.Arrays;
import org.spacehq.mc.protocol.packet.login.client.EncryptionResponsePacket;
import javax.crypto.SecretKey;
import org.spacehq.packetlib.packet.Packet;
import org.spacehq.mc.protocol.packet.login.server.EncryptionRequestPacket;
import org.spacehq.mc.protocol.packet.login.client.LoginStartPacket;
import org.spacehq.mc.protocol.packet.handshake.client.HandshakePacket;
import org.spacehq.mc.protocol.data.SubProtocol;
import org.spacehq.packetlib.event.session.PacketReceivedEvent;
import org.spacehq.packetlib.event.session.ConnectedEvent;
import java.util.Random;
import java.security.KeyPair;
import org.spacehq.packetlib.event.session.SessionAdapter;

public class ServerListener extends SessionAdapter
{
    private static final KeyPair KEY_PAIR;
    private byte[] verifyToken;
    private String serverId;
    private String username;
    private long lastPingTime;
    private int lastPingId;
    
    public ServerListener() {
        this.verifyToken = new byte[4];
        this.serverId = "";
        this.username = "";
        this.lastPingTime = 0L;
        this.lastPingId = 0;
        new Random().nextBytes(this.verifyToken);
    }
    
    @Override
    public void connected(final ConnectedEvent event) {
        event.getSession().setFlag("ping", 0);
    }
    
    @Override
    public void packetReceived(final PacketReceivedEvent event) {
        final MinecraftProtocol protocol = (MinecraftProtocol)event.getSession().getPacketProtocol();
        if (protocol.getSubProtocol() == SubProtocol.HANDSHAKE && event.getPacket() instanceof HandshakePacket) {
            final HandshakePacket packet = event.getPacket();
            switch (packet.getIntent()) {
                case STATUS: {
                    protocol.setSubProtocol(SubProtocol.STATUS, false, event.getSession());
                    break;
                }
                case LOGIN: {
                    protocol.setSubProtocol(SubProtocol.LOGIN, false, event.getSession());
                    if (packet.getProtocolVersion() > 47) {
                        event.getSession().disconnect("Outdated server! I'm still on 1.8.8.");
                        break;
                    }
                    if (packet.getProtocolVersion() < 47) {
                        event.getSession().disconnect("Outdated client! Please use 1.8.8.");
                        break;
                    }
                    break;
                }
                default: {
                    throw new UnsupportedOperationException("Invalid client intent: " + packet.getIntent());
                }
            }
        }
        if (protocol.getSubProtocol() == SubProtocol.LOGIN) {
            if (event.getPacket() instanceof LoginStartPacket) {
                this.username = ((LoginStartPacket) event.getPacket()).getUsername();
                final boolean verify = !event.getSession().hasFlag("verify-users") || (boolean)event.getSession().getFlag("verify-users");
                if (verify) {
                    event.getSession().send(new EncryptionRequestPacket(this.serverId, ServerListener.KEY_PAIR.getPublic(), this.verifyToken));
                }
                else {
                    new Thread(new UserAuthTask(event.getSession(), null)).start();
                }
            }
            else if (event.getPacket() instanceof EncryptionResponsePacket) {
                final EncryptionResponsePacket packet2 = event.getPacket();
                final PrivateKey privateKey = ServerListener.KEY_PAIR.getPrivate();
                if (!Arrays.equals(this.verifyToken, packet2.getVerifyToken(privateKey))) {
                    event.getSession().disconnect("Invalid nonce!");
                    return;
                }
                final SecretKey key = packet2.getSecretKey(privateKey);
                protocol.enableEncryption(key);
                new Thread(new UserAuthTask(event.getSession(), key)).start();
            }
        }
        if (protocol.getSubProtocol() == SubProtocol.STATUS) {
            if (event.getPacket() instanceof StatusQueryPacket) {
                final ServerInfoBuilder builder = event.getSession().getFlag("info-builder");
                if (builder == null) {
                    event.getSession().disconnect("No server info builder set.");
                    return;
                }
                final ServerStatusInfo info = builder.buildInfo(event.getSession());
                event.getSession().send(new StatusResponsePacket(info));
            }
            else if (event.getPacket() instanceof StatusPingPacket) {
                event.getSession().send(new StatusPongPacket(((StatusPingPacket) event.getPacket()).getPingTime()));
            }
        }
        if (protocol.getSubProtocol() == SubProtocol.GAME && event.getPacket() instanceof ClientKeepAlivePacket) {
            final ClientKeepAlivePacket packet3 = event.getPacket();
            if (packet3.getPingId() == this.lastPingId) {
                final long time = System.currentTimeMillis() - this.lastPingTime;
                event.getSession().setFlag("ping", time);
            }
        }
    }
    
    @Override
    public void disconnecting(final DisconnectingEvent event) {
        final MinecraftProtocol protocol = (MinecraftProtocol)event.getSession().getPacketProtocol();
        if (protocol.getSubProtocol() == SubProtocol.LOGIN) {
            event.getSession().send(new LoginDisconnectPacket(event.getReason()));
        }
        else if (protocol.getSubProtocol() == SubProtocol.GAME) {
            event.getSession().send(new ServerDisconnectPacket(event.getReason()));
        }
    }
    
    static {
        KEY_PAIR = CryptUtil.generateKeyPair();
    }
    
    private class UserAuthTask implements Runnable
    {
        private Session session;
        private SecretKey key;
        
        public UserAuthTask(final Session session, final SecretKey key) {
            this.key = key;
            this.session = session;
        }
        
        @Override
        public void run() {
            final boolean verify = !this.session.hasFlag("verify-users") || (boolean)this.session.getFlag("verify-users");
            GameProfile profile = null;
            if (verify && this.key != null) {
                Proxy proxy = this.session.getFlag("auth-proxy");
                if (proxy == null) {
                    proxy = Proxy.NO_PROXY;
                }
                try {
                    profile = new SessionService(proxy).getProfileByServer(ServerListener.this.username, new BigInteger(CryptUtil.getServerIdHash(ServerListener.this.serverId, ServerListener.KEY_PAIR.getPublic(), this.key)).toString(16));
                }
                catch (RequestException e) {
                    this.session.disconnect("Failed to make session service request.", e);
                    return;
                }
                if (profile == null) {
                    this.session.disconnect("Failed to verify username.");
                }
            }
            else {
                profile = new GameProfile(UUID.nameUUIDFromBytes(("OfflinePlayer:" + ServerListener.this.username).getBytes()), ServerListener.this.username);
            }
            final int threshold = this.session.getFlag("compression-threshold");
            this.session.send(new LoginSetCompressionPacket(threshold));
            this.session.setCompressionThreshold(threshold);
            this.session.send(new LoginSuccessPacket(profile));
            this.session.setFlag("profile", profile);
            ((MinecraftProtocol)this.session.getPacketProtocol()).setSubProtocol(SubProtocol.GAME, false, this.session);
            final ServerLoginHandler handler = this.session.getFlag("login-handler");
            if (handler != null) {
                handler.loggedIn(this.session);
            }
            new Thread(new KeepAliveTask(this.session)).start();
        }
    }
    
    private class KeepAliveTask implements Runnable
    {
        private Session session;
        
        public KeepAliveTask(final Session session) {
            this.session = session;
        }
        
        @Override
        public void run() {
            while (this.session.isConnected()) {
                ServerListener.this.lastPingTime = System.currentTimeMillis();
                ServerListener.this.lastPingId = (int)ServerListener.this.lastPingTime;
                this.session.send(new ServerKeepAlivePacket(ServerListener.this.lastPingId));
                try {
                    Thread.sleep(2000L);
                    continue;
                }
                catch (InterruptedException e) {}
                break;
            }
        }
    }
}
