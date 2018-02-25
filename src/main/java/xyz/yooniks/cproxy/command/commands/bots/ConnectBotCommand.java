package xyz.yooniks.cproxy.command.commands.bots;

import org.spacehq.mc.protocol.MinecraftProtocol;
import org.spacehq.mc.protocol.data.SubProtocol;
import org.spacehq.mc.protocol.data.game.values.MessageType;
import org.spacehq.mc.protocol.data.game.values.entity.MetadataType;
import org.spacehq.mc.protocol.data.status.ServerStatusInfo;
import org.spacehq.mc.protocol.data.status.handler.ServerInfoHandler;
import org.spacehq.mc.protocol.packet.ingame.client.ClientChatPacket;
import org.spacehq.mc.protocol.packet.ingame.client.ClientKeepAlivePacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerChatPacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerDisconnectPacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerJoinGamePacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnMobPacket;
import org.spacehq.mc.protocol.packet.login.server.LoginDisconnectPacket;
import org.spacehq.packetlib.Client;
import org.spacehq.packetlib.Session;
import org.spacehq.packetlib.event.session.*;
import org.spacehq.packetlib.tcp.TcpSessionFactory;
import xyz.yooniks.cproxy.command.Command;
import xyz.yooniks.cproxy.enums.Group;
import xyz.yooniks.cproxy.managers.MacroManager;
import xyz.yooniks.cproxy.managers.ProxyManager;
import xyz.yooniks.cproxy.objects.Bot;
import xyz.yooniks.cproxy.objects.Macro;
import xyz.yooniks.cproxy.objects.Player;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Random;

public class ConnectBotCommand extends Command {

    public ConnectBotCommand() {
        super("connectbot", "Zaatakuj serwer botami!", ",connectbot [serwer:port] [ilosc] [ping] [delay w ms] [proxy/none/random/] [macro id/none]",
                Group.GRACZ, "joinbot", "connectbots", "joinbots");
    }

    @Override
    public void onCommand(Player p, Command command, String[] args) {
        if (args.length == 0 || args.length < 7) {
            p.sendMessage("$p &7Poprawne uzycie: &a" + getUsage());
            return;
        } else {
            final String host = args[1].split(":")[0];
            final Integer port = Integer.parseInt(args[1].split(":")[1]);
            final Integer amount = Integer.parseInt(args[2]);
            final Boolean ping = Boolean.parseBoolean(args[3]);
            final Long delay = Long.parseLong(args[4]);
            final java.net.Proxy proxy;
            final Macro macro;
            if (args[6].contains("none") || args[6].contains("null")) {
                macro = null;
            } else {
                macro = MacroManager.getMacroById(Integer.parseInt(args[6]));
            }
            if (args[5].contains(":")) {
                proxy = new java.net.Proxy(java.net.Proxy.Type.SOCKS,
                        new InetSocketAddress(args[5].split(":")[0],
                                Integer.valueOf(args[5].split(":")[1])));
                p.sendMessage("$p &7Ip proxy: &aspecified&7 &8(&7" + proxy.address().toString().split(":")[0] + ":"
                        + proxy.address().toString().split(":")[1] + "&8)");
            } else if (args[5].contains("random") || args[5].contains("top")) {
                proxy = null;
                p.sendMessage("$p &7Ip proxy: &arandom from list socks.txt");
            } else {
                p.sendMessage("$p &7Ip proxy: &anull &8(&70:0&8)");
                proxy = java.net.Proxy.NO_PROXY;
            }
            connectBots(p, amount, host, port, delay, ping, proxy, macro);
        }
    }

    private void connectBots(Player owner, int amount, String host, Integer port, long msDelay, boolean ping, Proxy proxy, Macro macro) {
        owner.sendMessage("$p &a" + amount + " &7botow laczy do: &a"
                + host + " &8(" + port + ")&7, wykonuja macro ID: &a" + ((macro == null) ? "none" :
                Integer.toString(macro.getId())) + "\n $p &7Powiadomienia beda wyswietlane co kilkanascie botow");
        //owner.sendMessage("$p &7Boty lacza za chwile do serwera: &a" + host + "&7, delay: &a" + msDelay + "ms&7, ilosc botow: &a" + amount);
        final Random rand = new Random();
        final Proxy proksi;
        if (proxy == null)
            proksi = ProxyManager.getRandomProxy();
        else
            proksi = proxy;
        for (int i = 0; i < amount; i++) {

            final int i2 = i + 1;
            new Thread(() -> {
                final String nick = "CasualProxy" + rand.nextInt(1000);
                connectBot(owner, nick, host, port, ping, msDelay, proksi, macro, i2, amount, true);
                if (msDelay != 0) {
                    try {
                        Thread.sleep(msDelay);
                    } catch (InterruptedException ex) {
                        owner.sendMessage("$p &cDelay nieudany! &7" + ex.getMessage());
                    }
                }
            }).start();
        }
    }

    private void connectBot(Player owner, String nick, String host, int port, boolean ping, long msDelay, Proxy proxy, Macro macro, final int amount, final int maxamount, boolean msg) {
        if (amount == 1 || amount == 10 || amount == 25 || amount == 40 || amount == 50 || amount == 70 || amount == 90 || amount == 100 || amount == 150 || amount == 200 || amount == 250 || amount == 300 || amount == 350 || amount == 400 || amount == 450 || amount == 500 || amount == 550 || amount == 600 || amount == 700 || amount == 800 || amount == 900 || amount == 1000) {
            owner.sendMessage("$p &aBot &7" + nick + " &a(ilosc: " + amount + "/" + maxamount + ", proxy: " + ((proxy == null || proxy == Proxy.NO_PROXY) ? "null, ip: 0, port: 0" : ("SOCKS, ip: " + proxy.address().toString().split(":")[0] + ", port: " + proxy.address().toString().split(":")[1])) + " &alaczy do: &7" + host + ":" + port + "&a)");
        }
        final Client c = new Client(host, port, new MinecraftProtocol(nick), new TcpSessionFactory(proxy));
        c.getSession().setConnectTimeout(owner.botOptions.timeOutConnect);
        if (ping) {
            final MinecraftProtocol protocol = new MinecraftProtocol(SubProtocol.STATUS);
            final Client client = new Client(host, port, protocol, new TcpSessionFactory(proxy));
            client.getSession().setConnectTimeout(owner.botOptions.timeOutPing);
            client.getSession().setFlag("server-info-handler", new ServerInfoHandler() {
                @Override
                public void handle(final Session session, final ServerStatusInfo info) {
                    client.getSession().disconnect("zpingowano.");
                }
            });
            client.getSession().connect();
        }
        c.getSession().connect();
        final Bot bot = new Bot(owner, nick, c.getSession());
        c.getSession().addListener(new SessionListener() {
            @Override
            public void packetReceived(PacketReceivedEvent e) {
                if (e.getPacket() instanceof ServerChatPacket) {
                    if (owner.playerOptions.chatFromBots && ((ServerChatPacket) e.getPacket()).getMessage() != null) {
                        if (((ServerChatPacket) e.getPacket()).getType() != MessageType.CHAT) return;
                        owner.sendMessage("&7[Bot &a" + nick + "&7]");
                        owner.sendMessage(((ServerChatPacket) e.getPacket()).getMessage().getText());
                        owner.sendMessage("&7[Bot &a" + nick + "&7]");
                    }
                }
                //autocaptcha
                if (e.getPacket() instanceof ServerChatPacket) {
                    if (owner.botOptions.autoCaptcha) {
                        if (e.getSession().getHost().contains("proxy")) return;
                        final ServerChatPacket p3 = e.getPacket();
                        if (p3.getMessage().toString().toLowerCase().contains("captcha:") || p3.getMessage().toString().toLowerCase().contains("kod:")) {
                            if (e.getSession().getHost().contains("megaxcore")) return;
                            final String message = p3.getMessage().toString();
                            final String[] args2 = message.split(":");
                            if (args2.length < 2 || args2[1] == null) return;
                            args2[1] = args2[1].replace(" ", "");
                            e.getSession().send(new ClientChatPacket("/register " + args2[1] + " cproxy123 cproxy123"));
                            e.getSession().send(new ClientChatPacket("/register cproxy123 cproxy123 " + args2[1]));
                            //owner.sendMessage("$p &7Wykryto kod captcha: &a" + args2[1]);
                        } else if (p3.getMessage().toString().toLowerCase().contains("kod") && p3.getMessage().toString().toLowerCase().contains("to")) {
                            if (e.getSession().getHost().contains("megaxcore")) return;
                            final String message = p3.getMessage().toString();
                            final String[] args2 = message.split("to ");
                            if (args2.length < 2 || args2[1] == null) return;
                            args2[1] = args2[1].replace(" ", "");
                            //p.sendMessage("$p &7Wykryto kod captcha: &a" + args2[1]);
                            e.getSession().send(new ClientChatPacket("/register " + args2[1] + " cproxy123 cproxy123"));
                            e.getSession().send(new ClientChatPacket("/register cproxy123 cproxy123 " + args2[1]));
                        }
                    }
                    return;
                }
                //autocaptcha
                else if (e.getPacket() instanceof ServerSpawnMobPacket && owner.botOptions.autoCaptcha) {
                    final ServerSpawnMobPacket p3 = e.getPacket();
                    for (int i = 0; i < p3.getMetadata().length; ++i) {
                        if (p3.getMetadata()[i].getType() == MetadataType.STRING) {
                            final String msg2 = p3.getMetadata()[i].getValue().toString();
                            if (msg2.toLowerCase().contains("captcha:") || msg2.toLowerCase().contains("kod:")) {
                                final String[] args2 = msg2.split(":");
                                if (args2.length < 2 || args2[1] == null) {
                                    return;
                                }
                                args2[1] = args2[1].replace(" ", "");
                                args2[1] = args2[1].replace("§c", "");
                                args2[1] = args2[1].replace("§e", "");
                                args2[1] = args2[1].replace("§6", "");
                                args2[1] = args2[1].replace("§a", "");
                                args2[1] = args2[1].replace("§b", "");
                                args2[1] = args2[1].replace("§2", "");
                                e.getSession().send(new ClientChatPacket("/register " + args2[1] + " cproxy123 cproxy123"));
                                e.getSession().send(new ClientChatPacket("/register cproxy123 cproxy123 " + args2[1]));
                            }
                        }
                    }
                }
                if (e.getPacket() instanceof ServerJoinGamePacket) {
                    if (owner.botOptions.join)
                        owner.sendMessage("$p &7Bot &a" + nick + "&7 dolaczyl do serwera: &a" + host + ":" + port + " &8(&a" + amount + "&7/&2" + maxamount + "&8)");
                    if (owner.botOptions.autoLogin) {
                        c.getSession().send(new ClientChatPacket("/register cproxy123 cproxy123"));
                        c.getSession().send(new ClientChatPacket("/l cproxy123"));
                    }
                    c.getSession().send(new ClientKeepAlivePacket(1));
                    if (macro != null)
                        macro.macroStartDoing(owner, false);
                    owner.addBot(bot);
                } else if (e.getPacket() instanceof ServerDisconnectPacket) {
                    final ServerDisconnectPacket packet = e.getPacket();
                    if (packet.getReason().getFullText().toLowerCase().contains("zaloguj") ||
                            packet.getReason().getFullText().toLowerCase().contains("bot") ||
                            packet.getReason().getFullText().toLowerCase().contains("wejdz")) {
                        if (owner.botOptions.quit)
                            owner.sendMessage("&c[AntyBot] &7Bot &a" + nick + "&7 zostal rozlaczony z &a" + host + "&7, powod: &a" + packet.getReason());
                        if (owner.botOptions.autoReconnect) {
                            if (owner.botOptions.autoReconnectTime > 0) {
                                try {
                                    Thread.sleep(1000L * owner.botOptions.autoReconnectTime);
                                } catch (InterruptedException ex) {
                                }
                            }
                            c.getSession().disconnect("antybot");
                            connectBot(owner, nick, host, port, ping, msDelay, proxy, macro, amount, maxamount, false);
                        }
                    } else {
                        if (owner.botOptions.quit)
                            owner.sendMessage("&7Bot &a" + nick + "&7 zostal rozlaczony z &a" + host + "&7, powod: &a" + packet.getReason());
                    }
                } else if (e.getPacket() instanceof LoginDisconnectPacket) {
                    final LoginDisconnectPacket packet = e.getPacket();
                    if (packet.getReason().getFullText().toLowerCase().contains("zaloguj") ||
                            packet.getReason().getFullText().toLowerCase().contains("bot") ||
                            packet.getReason().getFullText().toLowerCase().contains("wejdz")) {
                        if (owner.botOptions.quit)
                            owner.sendMessage("&c[AntyBot] &7Bot &a" + nick + "&7 zostal rozlaczony z &a" + host + "&7, powod: &a" + packet.getReason());
                        c.getSession().disconnect("antybot");
                        if (owner.botOptions.autoReconnect) {
                            if (owner.botOptions.autoReconnectTime > 0) {
                                try {
                                    Thread.sleep(1000L * owner.botOptions.autoReconnectTime);
                                } catch (InterruptedException ex) {
                                }
                            }
                        }
                        connectBot(owner, nick, host, port, ping, msDelay, proxy, macro, amount, maxamount, false);
                    } else {
                        if (owner.botOptions.quit)
                            owner.sendMessage("&7Bot &a" + nick + "&7 zostal rozlaczony z &a" + host + "&7, powod: &a" + packet.getReason());
                    }
                }
            }

            @Override
            public void packetSent(PacketSentEvent p0) {

            }

            @Override
            public void connected(ConnectedEvent p0) {
            }

            @Override
            public void disconnecting(DisconnectingEvent p0) {

            }

            @Override
            public void disconnected(DisconnectedEvent e) {
                owner.removeBot(bot);
                if (e.getReason().toLowerCase().contains("antybot") || e.getReason().contains("wejdz") ||
                        e.getReason().contains("zaloguj")) {
                    if (owner.botOptions.quit)
                        owner.sendMessage("&c[AntyBot] &7Bot &a" + nick + "&7 zostal rozlaczony z &a" + host + "&7, powod: &a" + e.getReason() + "&7, cause: &a"
                                + e.getCause().getMessage());
                    c.getSession().disconnect("antybot");
                    if (owner.botOptions.autoReconnect) {
                        if (owner.botOptions.autoReconnectTime > 0) {
                            try {
                                Thread.sleep(1000L * owner.botOptions.autoReconnectTime);
                            } catch (InterruptedException ex) {
                            }
                        }
                    }
                    connectBot(owner, nick, host, port, ping, msDelay, proxy, macro, amount, maxamount, false);
                } else {
                    if (owner.botOptions.quit)
                        owner.sendMessage("&7Bot &a" + nick + "&7 zostal rozlaczony z &a" + host + "&7, powod: &a" + e.getReason() + "&7, cause: &a"
                                + e.getCause().getMessage());
                }
            }
        });
    }
}