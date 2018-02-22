package xyz.yooniks.cproxy.resolvers;

import xyz.yooniks.cproxy.objects.Player;

import java.util.*;
import javax.naming.directory.*;
import javax.naming.*;

public class SRVResolver {

    private static final DirContext srvContext;

    public static String resolve(final String domain, final String type, Player p) {
        try {
            final Attributes e = SRVResolver.srvContext.getAttributes("_" + type + "._tcp." + domain, new String[] { "SRV" });
            if (e != null) {
                final Attribute attrib = e.get("srv");
                if (attrib != null) {
                    final Object obj = attrib.get(0);
                    final String[] array = obj.toString().split(" ");
                    if (obj != null) {
                        return array[3].substring(0, array[3].length() - 1) + ":" + array[2];
                    }
                }
            }
        }
        catch (Exception ex) {
            //System.out.println(ex.getMessage());
            //p.sendMessage("$p &c[SRVResolver] Blad! &7"+ex.getMessage()+", Cause: "+ex.getCause());
        }
        return "";
    }

    static {
        final Hashtable<String, String> env = new Hashtable<String, String>();
        env.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");
        env.put("java.naming.provider.url", "dns:");
        try {
            srvContext = new InitialDirContext(env);
        }
        catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String srv(final String ip, Player p) {
        final String resolved = SRVResolver.resolve(ip, "minecraft", p);
        if (resolved.length() > 0) {
            return resolved.split(":")[0] + ":" + resolved.split(":")[1];
        }
        return ip+":25565";
    }
}
