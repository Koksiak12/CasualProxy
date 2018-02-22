package xyz.yooniks.cproxy.command.commands;

import xyz.yooniks.cproxy.Proxy;
import xyz.yooniks.cproxy.command.Command;
import xyz.yooniks.cproxy.enums.Group;
import xyz.yooniks.cproxy.managers.ProxyManager;
import xyz.yooniks.cproxy.objects.Player;

import java.net.InetSocketAddress;
import java.net.Socket;

public class ProxyCommand extends Command {

    public ProxyCommand() {
        super("proxy", "Zarzadzaj ip proxies!", ",proxy [add/recheck/list]",
                Group.GRACZ, "proksi");
    }

    @Override
    public void onCommand(Player p, Command command, String[] args) {
        if (args.length < 2) {
            p.sendMessage("$p &a,proxy [add/recheck/list] &7- pokazuje poprawne uzycie");
            return;
        }
        if (args[1].equalsIgnoreCase("list") && args.length < 3) {
            if (ProxyManager.proxies.size() <= 0) {
                p.sendMessage("$p &cBrak aktywnych socks proxy! Mozesz dodac wlasne &csocks proxy przy uzyciu &6,proxy add");
                return;
            }
            p.sendMessage("$p &cLista proxy: &a( " + ProxyManager.proxies.size() + " )");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (final xyz.yooniks.cproxy.objects.Proxy proxy : ProxyManager.proxies) {
                        proxy.getInfo(p);
                    }
                }
            }).start();
        }
        else {
            if (args[1].equalsIgnoreCase("add") && args.length < 3) {
                p.sendMessage("$p &7Poprawne uzycie: &a,proxy add <ip:port> [checker ms, host:ip (np: whois.internic.net:43)]");
                return;
            }
            if (args.length > 3) {
                final String ip1 = args[2];
                if (!ip1.contains(":")) {
                    p.sendMessage("$p &cNie podales portu proxy!");
                    return;
                }
                final String host1 = args[2].split(":")[0];
                final int port1 = Integer.valueOf(args[2].split(":")[1]);
                final String ip2 = args[3];
                if (!ip2.contains(":")) {
                    p.sendMessage("$p &cNie podales portu checkera!");
                    return;
                }
                final String host2 = args[3].split(":")[0];
                final int port2 = Integer.valueOf(args[3].split(":")[1]);
                p.sendMessage("$p &aSprawdzam proxy..");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final java.net.Proxy proxy = new java.net.Proxy(java.net.Proxy.Type.SOCKS, new InetSocketAddress(host1, port1));
                        final InetSocketAddress socketAddress = new InetSocketAddress(host2, port2);
                        long connectTime = System.currentTimeMillis();
                        final Socket socket = new Socket(proxy);
                        try {
                            socket.connect(socketAddress);
                            connectTime = System.currentTimeMillis() - connectTime;
                            final xyz.yooniks.cproxy.objects.Proxy proxyObj =
                                    new xyz.yooniks.cproxy.objects.Proxy(proxy, true, connectTime);
                            ProxyManager.proxies.add(proxyObj);
                            System.out.println("[ProxyChecker] Proxy " + proxy.address() + " jest online." +
                                    " [" + connectTime + " ms, " + ProxyManager.proxies.size() + "]");
                            p.sendMessage("$p &aZaladowano i sprawdzono proxy! &7(ip: &a" +
                                    proxy.address().toString().split(":")[0] + "&7, port:" +
                                    " &a" + proxy.address().toString().split(":")[1] + "&7, ms: &a" + connectTime + "&7)");
                        } catch (Throwable e) {
                            p.sendMessage("$p &cBlad podczas sprawdzania proxy! &6" + e.getMessage());
                        }
                    }
                }).start();
            }
            if (args[1].equalsIgnoreCase("recheck")) {
                if (!p.can(Group.ADMIN)) {
                    p.sendMessage("$p &cNie masz dostepu do tej komendy!");
                    return;
                }
                p.sendMessage("$p &aLaduje od nowa proxy.. Broadcast: true");
                ProxyManager.proxies.clear();
                ProxyManager.loadProxies(true);
            }
        }
    }
}