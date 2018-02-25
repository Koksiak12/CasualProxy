package xyz.yooniks.cproxy.command.commands.settings;

import xyz.yooniks.cproxy.command.Command;
import xyz.yooniks.cproxy.enums.Group;
import xyz.yooniks.cproxy.objects.Player;

public class AutoReconnectTimeCommand extends Command {

    public AutoReconnectTimeCommand() {
        super("autoreconnecttime", "Ustaw czas reconnecta (antybot)", ",autoreconnecttime [bots/runnables] [czas w sek]",
                Group.GRACZ, "botsautoreconnecttime", "botautoreconnecttime");
    }

    @Override
    public void onCommand(Player p, Command command, String[] args) {
        if (args.length < 3) {
            p.sendMessage("$p &7Poprawne uzycie: &a" + getUsage());
            return;
        }
        final String type = args[1];
        final Integer sec = Integer.parseInt(args[2]);
        if (type.equalsIgnoreCase("bots") || type.equalsIgnoreCase("bot")) {
            p.botOptions.autoReconnectTime = sec;
            p.sendMessage("$p &7Ustawiles autoreconnecttime botow na: &a" + sec);
        } else {
            p.playerOptions.autoReconnectTime = sec;
            p.sendMessage("$p &7Ustawiles autoreconnecttime playera na: &a" + sec);
        }
    }
}