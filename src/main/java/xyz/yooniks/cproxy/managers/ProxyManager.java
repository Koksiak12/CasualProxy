package xyz.yooniks.cproxy.managers;

import xyz.yooniks.cproxy.utils.ChatUtilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class ProxyManager {

    public static List<xyz.yooniks.cproxy.objects.Proxy> proxies = new LinkedList<>();
    public static int allproxies;

    private static int proxyIterator;

    public static void loadProxies(final boolean broadcast) {
        new Thread() {
            @Override
            public void run() {
                Scanner sc = null;
                try {
                    sc = new Scanner(new File("CasualProxy", "socks.txt"));
                } catch (FileNotFoundException e3) {
                    System.out.println("Could not find proxy file, creating..");
                    try {
                        new File("CasualProxy", "socks.txt").createNewFile();
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                }
                if (sc == null) {
                    return;
                }
                while (sc.hasNextLine()) {
                    final String line = sc.nextLine();
                    ++ProxyManager.allproxies;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if (!line.startsWith("##") && line.contains(":")) {
                                final String[] parts = line.split(":");
                                final String host = parts[0];
                                final String port = parts[1];
                                final java.net.Proxy proxy = new java.net.Proxy(java.net.Proxy.Type.SOCKS, new InetSocketAddress(host, Integer.valueOf(port)));
                                final InetSocketAddress socketAddress = new InetSocketAddress("whois.internic.net", 43);
                                long connectTime = System.currentTimeMillis();
                                final Socket socket = new Socket(proxy);
                                try {
                                    socket.connect(socketAddress);
                                    connectTime = System.currentTimeMillis() - connectTime;
                                    final xyz.yooniks.cproxy.objects.Proxy proxyObj =
                                            new xyz.yooniks.cproxy.objects.Proxy(proxy, true, connectTime);
                                    ProxyManager.proxies.add(proxyObj);
                                    if (broadcast)
                                        ChatUtilities.broadcast(
                                                "&8[&2Proxy&aManager&8] &aKolejne proxy socks zaladowane i gotowe do uzywania," +
                                                        " pozycja w liscie: " + ProxyManager.proxies.size() + ", wiecej pod &7,proxy list");
                                } catch (Throwable t) {
                                }
                            }
                        }
                    }).start();
                }
            }
        }.start();
    }

    public static java.net.Proxy getRandomProxy() {
        if (proxies.size() < 1)
            return Proxy.NO_PROXY;
        if (ProxyManager.proxyIterator > ProxyManager.proxies.size() - 1) {
            ProxyManager.proxyIterator = 0;
        }
        final java.net.Proxy proxy = ProxyManager.proxies.get(ProxyManager.proxyIterator).proxy;
        ++ProxyManager.proxyIterator;
        return proxy;
    }
}
