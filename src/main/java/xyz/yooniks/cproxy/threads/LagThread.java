package xyz.yooniks.cproxy.threads;

import org.spacehq.packetlib.Session;
import xyz.yooniks.cproxy.CasualProxy;
import xyz.yooniks.cproxy.managers.PlayerManager;
import xyz.yooniks.cproxy.objects.Player;

public class LagThread extends Thread {

    @Override
    public void run() {
        for (Session session : CasualProxy.getPlayers()) {
            final Player p = PlayerManager.getPlayer(session);
            if (session.isConnected()) {
                p.updateLag();
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
