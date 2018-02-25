package xyz.yooniks.cproxy;

import org.spacehq.mc.auth.data.GameProfile;
import org.spacehq.mc.protocol.MinecraftProtocol;
import org.spacehq.mc.protocol.ServerLoginHandler;
import org.spacehq.mc.protocol.data.game.Position;
import org.spacehq.mc.protocol.data.game.values.HandshakeIntent;
import org.spacehq.mc.protocol.data.game.values.MessageType;
import org.spacehq.mc.protocol.data.game.values.entity.player.GameMode;
import org.spacehq.mc.protocol.data.game.values.setting.Difficulty;
import org.spacehq.mc.protocol.data.game.values.world.WorldType;
import org.spacehq.mc.protocol.data.message.TextMessage;
import org.spacehq.mc.protocol.data.status.PlayerInfo;
import org.spacehq.mc.protocol.data.status.ServerStatusInfo;
import org.spacehq.mc.protocol.data.status.VersionInfo;
import org.spacehq.mc.protocol.data.status.handler.ServerInfoBuilder;
import org.spacehq.mc.protocol.packet.handshake.client.HandshakePacket;
import org.spacehq.mc.protocol.packet.ingame.client.ClientChatPacket;
import org.spacehq.mc.protocol.packet.ingame.client.ClientRequestPacket;
import org.spacehq.mc.protocol.packet.ingame.client.ClientSettingsPacket;
import org.spacehq.mc.protocol.packet.ingame.client.ClientTabCompletePacket;
import org.spacehq.mc.protocol.packet.ingame.client.player.*;
import org.spacehq.mc.protocol.packet.ingame.client.window.ClientCloseWindowPacket;
import org.spacehq.mc.protocol.packet.ingame.client.window.ClientConfirmTransactionPacket;
import org.spacehq.mc.protocol.packet.ingame.client.window.ClientCreativeInventoryActionPacket;
import org.spacehq.mc.protocol.packet.ingame.client.window.ClientWindowActionPacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerChatPacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerJoinGamePacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerSwitchCameraPacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerTitlePacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.player.ServerChangeHeldItemPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.player.ServerPlayerAbilitiesPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.player.ServerPlayerPositionRotationPacket;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerSpawnPositionPacket;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerUpdateTimePacket;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerWorldBorderPacket;
import org.spacehq.mc.protocol.packet.status.client.StatusPingPacket;
import org.spacehq.packetlib.Server;
import org.spacehq.packetlib.Session;
import org.spacehq.packetlib.event.server.ServerAdapter;
import org.spacehq.packetlib.event.server.SessionAddedEvent;
import org.spacehq.packetlib.event.server.SessionRemovedEvent;
import org.spacehq.packetlib.event.session.*;
import org.spacehq.packetlib.tcp.TcpSessionFactory;
import xyz.yooniks.cproxy.command.Command;
import xyz.yooniks.cproxy.command.CommandManager;
import xyz.yooniks.cproxy.interfaces.Loader;
import xyz.yooniks.cproxy.managers.PlayerManager;
import xyz.yooniks.cproxy.managers.ProxyManager;
import xyz.yooniks.cproxy.objects.Bot;
import xyz.yooniks.cproxy.objects.Player;
import xyz.yooniks.cproxy.threads.LagThread;
import xyz.yooniks.cproxy.threads.TabThread;
import xyz.yooniks.cproxy.utils.ChatUtilities;
import xyz.yooniks.cproxy.utils.DateUtilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public class CasualProxy extends ProxyHelper implements Loader {

    private static List<String> accessPlayers = new ArrayList<>();
    private static List<Session> players = new CopyOnWriteArrayList<>();
    private Server server;

    public static List<Session> getPlayers() {
        return players;
    }

    public static List<String> getAccessPlayers() {
        return accessPlayers;
    }

    public static void addAccess(String player) {
        accessPlayers.add(player);
    }

    @Override
    public void onLoad() {
        getLogger().info("Loading proxy..");
        loadConfig();
        loadCommands();

        this.server = new Server("0.0.0.0", 25565, MinecraftProtocol.class, new TcpSessionFactory());
        this.server.bind();

        this.server.setGlobalFlag("compression-threshold", 100);
        this.server.setGlobalFlag("verify-users", false);
        this.server.setGlobalFlag("login-handler", new ServerLoginHandler() {
            @Override
            public void loggedIn(final Session session) {
                final GameProfile profile = session.getFlag("profile");
                if (!accessPlayers.contains(profile.getName())) {
                    session.disconnect(ChatUtilities
                            .fixColor("$p &7" + profile.getName() + ", " +
                                    "&cnie posiadasz dostepu do tego serwera.\nZakup dostep na skype: &ayooniksyooniks@gmail.com" + "\n\n&7@edit, dodaj siebie w CasualProxy/settings.xml, zmien unsafe_cash na swoj nick, jezeli ten plik nie istnieje to go pobierz i zedytuj: www.github.com/yooniks/CasualProxy/CasualProxy/settings.xml"));
                    return;
                }
                final Player p = PlayerManager.getPlayer(session);
                if (!p.canJoin()) {
                    session.disconnect(ChatUtilities.fixColor(
                            "$p &a" + profile.getName() + "&c, data waznosci twojego konta minela!" +
                                    " (" + DateUtilities.getDate(p.getExpirationDate()) + ")" +
                                    "\n&cZakup dostep na skype: &ayooniksyooniks@gmail.com"));
                    return;
                }
                for (Session sPlayer : players) {
                    final GameProfile profilePlayer = sPlayer.getFlag("profile");
                    final String nameS = profilePlayer.getName();
                    if (nameS.equalsIgnoreCase(p.getNick())) {
                        session.disconnect(ChatUtilities.fixColor(
                                "$p &7Gracz o nicku &a" + nameS + " &7jest juz &2online&7! " +
                                        "Jezeli to blad zglos to na skype: &ayooniksyooniks@gmail.com"));
                    }
                }
                session.send(new ServerJoinGamePacket(1, false,
                        GameMode.SURVIVAL, 0, Difficulty.PEACEFUL, 1000,
                        WorldType.FLAT, false));
                session.send(new ServerSpawnPositionPacket(new Position(
                        0, 1337, 0)));
                session.send(new ServerPlayerAbilitiesPacket(false, false, false, false, 0.1f, 0.1f));
                session.send(new ServerChangeHeldItemPacket(0));
                session.send(new ServerUpdateTimePacket(1, 0L));
                session.send(new ServerPlayerPositionRotationPacket(0, 1337, 0, 0.0F, 0.0F));
                session.send(new ServerWorldBorderPacket(15));
                /*for (final Player p : StaticMC.this.world.getPlayers()) {
                    session.send(new ServerEntityPositionRotationPacket(p.getId(), 0.0, 0.0, 0.0, (float)p.yaw, (float)p.pitch, p.isOnGround));
                    final User uP = UserManager.getUser(p.profile.getName());
                    final ServerPlayerListEntryPacket packetek = new ServerPlayerListEntryPacket(
                            PlayerListEntryAction.ADD_PLAYER, (PlayerListEntry[])Arrays.asList(
                                    new PlayerListEntry(new GameProfile(UUID.randomUUID(),
                                            ChatUtil.fixColor("&9" + p.profile.getName())),
                                            GameMode.SURVIVAL, 0, new TextMessage(ChatUtil.fixColor(uP.getRankType().prefix + uP.getNick() + "  ")))).toArray());
                    session.send(packetek);
                    session.send(p.getSpawnPacket());
                }*/
                p.setSession(session);
                p.setConnected(false);
                p.setLogged(false);
                p.setSessionConnect(null);
                p.setLastPacketMs(0L);
                p.setLastPacket("&cRozlaczono");
                final Thread t = new TabThread();
                t.start();
                final Thread t2 = new LagThread();
                t2.start();
                if (p == null || session == null) return;
                /*session.send(new ServerTeamPacket("yooniks", TeamAction.ADD_PLAYER, new String[] { "yooniks" }));
                session.send(new ServerDisplayScoreboardPacket(ScoreboardPosition.SIDEBAR, "yooniks"));
                session.send(new ServerScoreboardObjectivePacket("yooniks", ObjectiveAction.ADD, "yooniks", ScoreType.INTEGER));
                session.send(new ServerScoreboardObjectivePacket("yooniks", ObjectiveAction.UPDATE, "yooniks", ScoreType.INTEGER));*/
                players.add(session);
                p.sendMessage(String.valueOf(getChars()));
                p.sendMessage("$p &7Zaloguj sie uzywajac: &a,login [haslo]");
                session.send(new ServerTitlePacket(
                        ChatUtilities.fixColor("$p &7Zaloguj sie uzywajac: &a,login [haslo]"), true));
                ChatUtilities.broadcast("$p &a" + profile.getName() + "&7 dolaczyl do proxy!");
                session.send(new ServerTitlePacket(20, 28, 20));
                session.send(new ServerSwitchCameraPacket(2));
                session.addListener(new SessionListener() {
                    @Override
                    public void packetReceived(final PacketReceivedEvent event) {
                    }

                    @Override
                    public void packetSent(final PacketSentEvent packetSentEvent) {
                    }

                    @Override
                    public void connected(final ConnectedEvent connectedEvent) {
                    }

                    @Override
                    public void disconnecting(final DisconnectingEvent disconnectingEvent) {
                    }

                    @Override
                    public void disconnected(final DisconnectedEvent ev) {
                        final GameProfile profile = ev.getSession().getFlag("profile");
                        getLogger().info(profile.getName() + " wyszedl, " +
                                "powod: " + ev.getReason() + " cause: " + ev.getCause());
                        ev.getCause().printStackTrace();
                    }
                });
            }
        });
        this.server.setGlobalFlag("info-builder", new ServerInfoBuilder() {
            @Override
            public ServerStatusInfo buildInfo(final Session session) {
                final GameProfile[] profiles = {new GameProfile(UUID.randomUUID(),
                        ChatUtilities.fixColor("&7Autor proxy: &ayooniks\n\n&8>> &7Lista aktywnych socks do 200ms: " +
                                "&a" + ProxyManager.proxies.size() + "&8/&c" + ProxyManager.allproxies + " \n" +
                                "\n&8>> &7Crasherki NBT i wiecej\n&8>> &7Bardzo szybkie boty, macro, mother, pelno ustawien do botow itd."))};
                return new ServerStatusInfo(new VersionInfo("§2§lCasual§a§lProxy                    §7Online: §a" + players.size() + " ", 48),
                        new PlayerInfo(800, 0, profiles),
                        new TextMessage("§2Casual§aProxy §8| §7Dostep na skype: §ayooniksyooniks@gmail.com\n      §aComing soon.."), null);
            }
        });
        this.server.addListener(new ServerAdapter() {
            @Override
            public void sessionAdded(final SessionAddedEvent event) {
                event.getSession().addListener(new SessionAdapter() {
                    @Override
                    public void packetReceived(final PacketReceivedEvent event) {

                        //System.out.println(event.getPacket().getClass().getSimpleName());
                        //TODO

                        if (event.getPacket() instanceof HandshakePacket) {
                            final HandshakePacket packet = event.getPacket();
                            if (packet.getIntent() == HandshakeIntent.STATUS) {
                                getLogger().info("Uzyskano pakiet pingu od: " + packet.getHostName());
                                ChatUtilities.broadcast("$p &7Uzyskano pakiet pingu od: &a" + packet.getHostName());
                            }
                            return;
                        }


                        if (event.getPacket() instanceof ClientPlayerMovementPacket) {
                            final GameProfile profile = event.getSession().getFlag("profile");
                            final Player p = PlayerManager.getPlayer(profile.getName());
                            final ClientPlayerMovementPacket packet = event.getPacket();
                            p.setPosition(new Position((int) packet.getX(), (int) packet.getY(), (int) packet.getZ()));
                            p.setYaw(packet.getYaw());
                            p.setPitch(packet.getPitch());
                            if (p.isMother() && p.isConnected() && p.getSessionConnect() != null) {
                                if (p.getBots().size() > 0) {
                                    for (Bot bot : p.getBots()) {
                                        bot.getSession().send(event.getPacket());
                                    }
                                }
                            }
                            return;
                        }
                        if (/*event.getPacket() instanceof ClientPlayerPositionPacket ||
                                /*event.getPacket() instanceof ClientPlayerPositionRotationPacket ||
                               /* event.getPacket() instanceof ClientPlayerMovementPacket ||*/
                                event.getPacket() instanceof ClientWindowActionPacket ||
                                        event.getPacket() instanceof ClientSwingArmPacket ||
                                        event.getPacket() instanceof ClientPlayerPlaceBlockPacket ||
                                        event.getPacket() instanceof ClientRequestPacket ||
                                        event.getPacket() instanceof ClientPlayerStatePacket ||
                                        event.getPacket() instanceof ClientChangeHeldItemPacket ||
                                        event.getPacket() instanceof ClientCloseWindowPacket ||
                                        event.getPacket() instanceof ClientPlayerRotationPacket ||
                                        event.getPacket() instanceof ClientConfirmTransactionPacket ||
                                        event.getPacket() instanceof ClientPlayerActionPacket ||
                                        event.getPacket() instanceof ClientCreativeInventoryActionPacket ||
                                        event.getPacket() instanceof ClientSettingsPacket ||
                                        event.getPacket() instanceof ClientSteerVehiclePacket ||
                                        event.getPacket() instanceof ClientTabCompletePacket) {
                            final GameProfile profile = event.getSession().getFlag("profile");
                            final Player p = PlayerManager.getPlayer(profile.getName());
                            if (p.isMother() && p.isConnected() && p.getSessionConnect() != null) {
                                if ((event.getPacket() instanceof ClientChatPacket &&
                                        ((ClientChatPacket) event.getPacket()).getMessage().startsWith(","))) return;
                                if (p.getBots().size() > 0) {
                                    for (Bot bot : p.getBots())
                                        bot.getSession().send(event.getPacket());
                                }
                            }
                        }
                        if (event.getPacket() instanceof ClientChatPacket) {
                            final GameProfile profile = event.getSession().getFlag("profile");
                            final Player p = PlayerManager.getPlayer(profile.getName());
                            final ClientChatPacket packet = event.getPacket();
                            final Session s = event.getSession();
                            final String[] args = packet.getMessage().split(" ");
                            if ((!p.isLogged() && args[0].equalsIgnoreCase(",login")) ||
                                    args[0].equalsIgnoreCase(",l")) {
                                if (args.length < 2) {
                                    p.sendMessage("$p &cPoprawne uzycie: &7!,login [haslo]");
                                    s.send(new ServerChatPacket(
                                            ChatUtilities.fixColor("$p &cPoprawne uzycie: " +
                                                    "&7,login [haslo]"), MessageType.NOTIFICATION));
                                    return;
                                }
                                if (p.isLogged()) {
                                    p.sendMessage("$p &cJestes juz zalogowany!");
                                    return;
                                }
                                final String pass = args[1];
                                if (pass.equalsIgnoreCase(p.getPassword())) {
                                    p.setLogged(true);
                                    p.sendMessage("$p &aZalogowano!");
                                    s.send(new ServerChatPacket(ChatUtilities.fixColor(
                                            "&7Witaj &a" + profile.getName() + "&7, CasualProxy by yooniks"),
                                            MessageType.NOTIFICATION));
                                    p.sendMessage("$p &7Witaj: &a" + profile.getName() + " " +
                                            "&7na: &2Casual&aProxy&7 " +
                                            "pomocne komendy pod: &3,help");
                                    p.sendMessage("$p &aData waznosci twojego konta: &6" +
                                            DateUtilities.getDate(p.getExpirationDate()));
                                    s.send(new ServerTitlePacket(ChatUtilities.fixColor("&aTwoje konto jest wazne do: " +
                                            "&2" + DateUtilities.getDate(p.getExpirationDate())), true));
                                    s.send(new ServerTitlePacket(20, 27, 20));
                                    s.send(new ServerTitlePacket(ChatUtilities.fixColor("&8## &2Casual&aProxy &8##"), false));
                                } else {
                                    s.disconnect(ChatUtilities.fixColor("$p &cNiepoprawne haslo."));
                                }
                            } else {
                                if (!p.isLogged() && !args[0].equalsIgnoreCase(",login")
                                        && !args[0].equalsIgnoreCase(",l")) {
                                    p.sendMessage("$p &cPierw sie zaloguj! (,login [haslo])");
                                    s.send(new ServerChatPacket(ChatUtilities.fixColor(
                                            "$p &cPierw sie zaloguj! (,login [haslo])"), MessageType.NOTIFICATION));
                                    return;
                                }
                                if (packet.getMessage().startsWith(",")) {
                                    final String arg = args[0].replace(",", "");
                                    for (Command cmd : CommandManager.getCommands()) {
                                        if (cmd.getCommand().equalsIgnoreCase(arg) ||
                                                Arrays.asList(cmd.getAliases()).contains(arg)) {
                                            cmd.onCommand(p, cmd, args);
                                        }
                                    }
                                    return;
                                } else if (packet.getMessage().startsWith("@")) {
                                    for (Session sessionAll : server.getSessions()) {
                                        sessionAll.send(new ServerChatPacket(
                                                ChatUtilities.fixColor("&3&l[CHAT] "
                                                        + p.getGroup().getPrefix() + profile.getName() + " §8» " +
                                                        "" + p.getGroup().getSuffix() + packet.getMessage().replace(
                                                        "@", ""))));
                                    }
                                }
                            }
                        }
                    }
                });
            }

            @Override
            public void sessionRemoved(final SessionRemovedEvent event) {
                final GameProfile profile = event.getSession().getFlag("profile");
                final Player p = PlayerManager.getPlayer(profile.getName());
                players.remove(event.getSession());
                ChatUtilities.broadcast(
                        "$p &a" + p.getNick() + "&7 opuscil proxy!");
            }
        });
        getLogger().info("Loading proxy finished!");
    }
}
