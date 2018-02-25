package xyz.yooniks.cproxy.command.commands.settings;

import xyz.yooniks.cproxy.command.Command;
import xyz.yooniks.cproxy.enums.Group;
import xyz.yooniks.cproxy.objects.Player;

public class AutoLoginCommand extends Command {

    public AutoLoginCommand() {
        super("autologin", "Wlacz/wylacz autologowanie na serwerach", ",autologin [bots/runnables]",
                Group.GRACZ, "botsautologin", "autoregister");
    }

    @Override
    public void onCommand(Player p, Command command, String[] args) {
        if (args.length < 2) {
            p.sendMessage("$p &7Poprawne uzycie: &a" + getUsage());
            return;
        }
        final String type = args[1];
        if (type.equalsIgnoreCase("bots") || type.equalsIgnoreCase("bot")) {
            if (p.botOptions.autoLogin) {
                p.sendMessage("$p &7AutoLogin botow &cwylaczony&7!");
                p.botOptions.autoLogin = false;
                return;
            }
            p.sendMessage("$p &7AutoLogin botow &awlaczony&7!");
            p.botOptions.autoLogin = true;
        } else {
            if (p.playerOptions.autoLogin) {
                p.sendMessage("$p &7AutoLogin playera&c wylaczony&7!");
                p.playerOptions.autoLogin = false;
                return;
            }
            p.sendMessage("$p &7AutoLogin playera &awlaczony&7!");
            p.playerOptions.autoLogin = true;
        }
    }
}