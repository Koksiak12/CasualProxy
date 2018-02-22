package xyz.yooniks.cproxy.command.commands;

import org.spacehq.mc.protocol.MinecraftProtocol;
import org.spacehq.mc.protocol.data.SubProtocol;
import org.spacehq.mc.protocol.data.status.ServerStatusInfo;
import org.spacehq.mc.protocol.data.status.handler.ServerInfoHandler;
import org.spacehq.packetlib.Client;
import org.spacehq.packetlib.Session;
import org.spacehq.packetlib.tcp.TcpSessionFactory;
import xyz.yooniks.cproxy.command.Command;
import xyz.yooniks.cproxy.enums.Group;
import xyz.yooniks.cproxy.objects.Player;
import xyz.yooniks.cproxy.resolvers.SRVResolver;

import java.net.Proxy;

public class PingCommand extends Command {

    public PingCommand() {
        super("ping", "Pinguje serwer!", ",ping [serwer:port]",
                Group.GRACZ, "zrobping");
    }

    @Override
    public void onCommand(Player p, Command command, String[] args) {
        if (args.length < 2 || !args[1].contains(":")){
            p.sendMessage("$p &7Poprawne uzycie: &a" + getUsage());
            return;
        }
        //final String host = args[1].split(":")[0];
        /*String host;
        Integer port;
        final String argIp = args[1];
        try {
            host = SRVResolver.srv(args[1].split(":")[0]);
            port = Integer.parseInt(host.split(":")[1]);
        }
        catch (ArrayIndexOutOfBoundsException e){
            host = argIp.split(":")[0];
            port = Integer.parseInt(argIp.split(":")[1]);
        }*/
        p.sendMessage("$p &aPingowanie...");
        final long ms = System.currentTimeMillis();
        new Thread(new Runnable() {
            @Override
            public void run() {
                final MinecraftProtocol protocol = new MinecraftProtocol(SubProtocol.STATUS);
                final Client client = new Client(args[1].split(":")[0], Integer.parseInt(args[1].split(":")[1]), protocol, new TcpSessionFactory(Proxy.NO_PROXY));
                client.getSession().setConnectTimeout(p.playerOptions.timeOutPing);
                client.getSession().setFlag("server-info-handler", new ServerInfoHandler() {
                    @Override
                    public void handle(final Session session, final ServerStatusInfo info) {
                        p.sendMessage("$p &7Zpingowano. &a[ Silnik serwera: &7" + info.getVersionInfo().getVersionName() + "&a, graczy: &7"
                                + info.getPlayerInfo().getOnlinePlayers() + "&8/&7" + info.getPlayerInfo().getMaxPlayers() + "&a, motd: &7" +
                                info.getDescription().getFullText() + " &a]");
                        p.sendMessage("$p &aSerwer odpowiedzial na ping w: &7" + (System.currentTimeMillis() - ms) + "ms");
                        client.getSession().disconnect("zpingowano.");
                    }
                });
                client.getSession().connect();
            }
        }).start();
    }
}