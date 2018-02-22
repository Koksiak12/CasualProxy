// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.protocol;

import org.spacehq.mc.protocol.packet.status.client.StatusQueryPacket;
import org.spacehq.mc.protocol.packet.login.client.LoginStartPacket;
import org.spacehq.mc.protocol.packet.handshake.client.HandshakePacket;
import org.spacehq.mc.protocol.data.game.values.HandshakeIntent;
import org.spacehq.packetlib.event.session.ConnectedEvent;
import org.spacehq.mc.protocol.data.status.ServerStatusInfo;
import javax.crypto.SecretKey;
import org.spacehq.mc.protocol.packet.ingame.server.ServerSetCompressionPacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerDisconnectPacket;
import org.spacehq.mc.protocol.packet.ingame.client.ClientKeepAlivePacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerKeepAlivePacket;
import org.spacehq.mc.protocol.data.status.handler.ServerPingTimeHandler;
import org.spacehq.mc.protocol.packet.status.server.StatusPongPacket;
import org.spacehq.mc.protocol.packet.status.client.StatusPingPacket;
import org.spacehq.mc.protocol.data.status.handler.ServerInfoHandler;
import org.spacehq.mc.protocol.packet.status.server.StatusResponsePacket;
import org.spacehq.mc.protocol.packet.login.server.LoginSetCompressionPacket;
import org.spacehq.mc.protocol.packet.login.server.LoginDisconnectPacket;
import org.spacehq.mc.protocol.packet.login.server.LoginSuccessPacket;
import java.security.Key;
import org.spacehq.packetlib.packet.Packet;
import org.spacehq.mc.protocol.packet.login.client.EncryptionResponsePacket;
import org.spacehq.mc.auth.exception.request.RequestException;
import org.spacehq.mc.auth.exception.request.InvalidCredentialsException;
import org.spacehq.mc.auth.exception.request.ServiceUnavailableException;
import org.spacehq.mc.auth.service.SessionService;
import java.math.BigInteger;
import org.spacehq.mc.auth.data.GameProfile;
import java.net.Proxy;
import org.spacehq.mc.protocol.util.CryptUtil;
import org.spacehq.mc.protocol.packet.login.server.EncryptionRequestPacket;
import org.spacehq.mc.protocol.data.SubProtocol;
import org.spacehq.packetlib.event.session.PacketReceivedEvent;
import org.spacehq.packetlib.event.session.SessionAdapter;

public class ClientListener extends SessionAdapter
{
    @Override
    public void packetReceived(final PacketReceivedEvent event) {
        final MinecraftProtocol protocol = (MinecraftProtocol)event.getSession().getPacketProtocol();
        if (protocol.getSubProtocol() == SubProtocol.LOGIN) {
            if (event.getPacket() instanceof EncryptionRequestPacket) {
                final EncryptionRequestPacket packet = event.getPacket();
                final SecretKey key = CryptUtil.generateSharedKey();
                Proxy proxy = event.getSession().getFlag("auth-proxy");
                if (proxy == null) {
                    proxy = Proxy.NO_PROXY;
                }
                final GameProfile profile = event.getSession().getFlag("profile");
                final String serverHash = new BigInteger(CryptUtil.getServerIdHash(packet.getServerId(), packet.getPublicKey(), key)).toString(16);
                final String accessToken = event.getSession().getFlag("access-token");
                try {
                    new SessionService(proxy).joinServer(profile, accessToken, serverHash);
                }
                catch (ServiceUnavailableException e) {
                    event.getSession().disconnect("Login failed: Authentication service unavailable.", e);
                    return;
                }
                catch (InvalidCredentialsException e2) {
                    event.getSession().disconnect("Login failed: Invalid login session.", e2);
                    return;
                }
                catch (RequestException e3) {
                    event.getSession().disconnect("Login failed: Authentication error: " + e3.getMessage(), e3);
                    return;
                }
                event.getSession().send(new EncryptionResponsePacket(key, packet.getPublicKey(), packet.getVerifyToken()));
                protocol.enableEncryption(key);
            }
            else if (event.getPacket() instanceof LoginSuccessPacket) {
                final LoginSuccessPacket packet2 = event.getPacket();
                event.getSession().setFlag("profile", packet2.getProfile());
                protocol.setSubProtocol(SubProtocol.GAME, true, event.getSession());
            }
            else if (event.getPacket() instanceof LoginDisconnectPacket) {
                final LoginDisconnectPacket packet3 = event.getPacket();
                event.getSession().disconnect(packet3.getReason().getFullText());
            }
            else if (event.getPacket() instanceof LoginSetCompressionPacket) {
                event.getSession().setCompressionThreshold(((LoginSetCompressionPacket) event.getPacket()).getThreshold());
            }
        }
        else if (protocol.getSubProtocol() == SubProtocol.STATUS) {
            if (event.getPacket() instanceof StatusResponsePacket) {
                final ServerStatusInfo info = ((StatusResponsePacket) event.getPacket()).getInfo();
                final ServerInfoHandler handler = event.getSession().getFlag("server-info-handler");
                if (handler != null) {
                    handler.handle(event.getSession(), info);
                }
                event.getSession().send(new StatusPingPacket(System.currentTimeMillis()));
            }
            else if (event.getPacket() instanceof StatusPongPacket) {
                final long time = System.currentTimeMillis() - ((StatusPongPacket) event.getPacket()).getPingTime();
                final ServerPingTimeHandler handler2 = event.getSession().getFlag("server-ping-time-handler");
                if (handler2 != null) {
                    handler2.handle(event.getSession(), time);
                }
                event.getSession().disconnect("Finished");
            }
        }
        else if (protocol.getSubProtocol() == SubProtocol.GAME) {
            if (event.getPacket() instanceof ServerKeepAlivePacket) {
                event.getSession().send(new ClientKeepAlivePacket(((ServerKeepAlivePacket) event.getPacket()).getPingId()));
            }
            else if (event.getPacket() instanceof ServerDisconnectPacket) {
                event.getSession().disconnect(((ServerDisconnectPacket) event.getPacket()).getReason().getFullText());
            }
            else if (event.getPacket() instanceof ServerSetCompressionPacket) {
                event.getSession().setCompressionThreshold(((ServerSetCompressionPacket) event.getPacket()).getThreshold());
            }
        }
    }
    
    @Override
    public void connected(final ConnectedEvent event) {
        final MinecraftProtocol protocol = (MinecraftProtocol)event.getSession().getPacketProtocol();
        if (protocol.getSubProtocol() == SubProtocol.LOGIN) {
            final GameProfile profile = event.getSession().getFlag("profile");
            protocol.setSubProtocol(SubProtocol.HANDSHAKE, true, event.getSession());
            event.getSession().send(new HandshakePacket(47, event.getSession().getHost(), event.getSession().getPort(), HandshakeIntent.LOGIN));
            protocol.setSubProtocol(SubProtocol.LOGIN, true, event.getSession());
            event.getSession().send(new LoginStartPacket((profile != null) ? profile.getName() : ""));
        }
        else if (protocol.getSubProtocol() == SubProtocol.STATUS) {
            protocol.setSubProtocol(SubProtocol.HANDSHAKE, true, event.getSession());
            event.getSession().send(new HandshakePacket(47, event.getSession().getHost(), event.getSession().getPort(), HandshakeIntent.STATUS));
            protocol.setSubProtocol(SubProtocol.STATUS, true, event.getSession());
            event.getSession().send(new StatusQueryPacket());
        }
    }
}
