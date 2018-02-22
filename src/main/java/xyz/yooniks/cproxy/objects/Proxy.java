package xyz.yooniks.cproxy.objects;

import com.fasterxml.jackson.databind.*;
import java.net.*;
import java.io.*;

public class Proxy
{
    public boolean online;
    public long ms;
    public java.net.Proxy proxy;

    public Proxy(final java.net.Proxy proxy, final boolean online, final long ms) {
        this.online = online;
        this.proxy = proxy;
        this.ms = ms;
    }

    public void getInfo(Player p) {
        final String url = "http://proxycheck.io/v1/" + this.proxy.address().toString().split(":")[0].replace("/",
                "") + "&key=111111-222222-333333-444444&vpn=1&asn=1&node=1&time=1&tag=forum%20signup%20page";
        try {
            final ObjectMapper objectMapper = new ObjectMapper();
            p.sendMessage("&aIp: &7" + this.proxy.address().toString().split(":")[0]
                    + "&a, port: &7" + this.proxy.address().toString().split(":")[1]
                    + "\n&aKraj: &7" + ((objectMapper.readTree(new URL(url)).get("country") == null)
                    ? "brak" : objectMapper.readTree(new URL(url)).get("country").toString()) +
                    " \n&aCzas polaczenia (ms): &7" + this.ms + "\n&aOnline: &7" + (this.online ? "&6tak" : "&cnie"));
        }
        catch (IOException e) {
            System.out.println("couldn't get info proxy! " + e.getMessage());
            p.sendMessage("$p &cBlad z uzyskaniem informacji o proxy!");
        }
    }
}
