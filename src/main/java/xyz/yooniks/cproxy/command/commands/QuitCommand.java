package xyz.yooniks.cproxy.command.commands;

import xyz.yooniks.cproxy.command.Command;
import xyz.yooniks.cproxy.enums.Group;
import xyz.yooniks.cproxy.objects.Player;

public class QuitCommand extends Command {

    public QuitCommand() {
        super("quit", "Wyjdz z serwera (rozlacza calkowicie)", ",quit",
                Group.GRACZ, "wyjdz", "left", "leave", "q");
    }

    @Override
    public void onCommand(Player p, Command command, String[] args) {
        if (p.isConnected()) {
            p.getSessionConnect().disconnect("Rozlaczono przy uzyciu komendy");
            p.setConnected(false);
            p.setSessionConnect(null);
            p.setLastPacketMs(0L);
            p.setLastPacketMs(0L);
            p.sendMessage("$p &aRozlaczono przy uzyciu komendy! :)");
        } else {
            p.sendMessage("$p &cNie jestes polaczony z zadnym serwerem!");
        }
    }
}