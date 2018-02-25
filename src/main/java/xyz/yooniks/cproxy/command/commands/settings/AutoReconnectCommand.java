package xyz.yooniks.cproxy.command.commands.settings;

import xyz.yooniks.cproxy.command.Command;
import xyz.yooniks.cproxy.enums.Group;
import xyz.yooniks.cproxy.objects.Player;

public class AutoReconnectCommand extends Command {

    public AutoReconnectCommand() {
        super("autoreconnect", "Wlacz/wylacz autoreconnect (reconnect antybot)", ",autoreconnect [bots/runnables]",
                Group.GRACZ, "botsautoreconnect","botautoreconnect");
    }

    @Override
    public void onCommand(Player p, Command command, String[] args) {
        if (args.length < 2){
            p.sendMessage("$p &7Poprawne uzycie: &a" + getUsage());
            return;
        }
        final String type = args[1];
        if (type.equalsIgnoreCase("bots")||type.equalsIgnoreCase("bot")) {
            if (p.botOptions.autoReconnect) {
                p.sendMessage("$p &7AutoReconnect botow &cwylaczony&7!");
                p.botOptions.autoReconnect=false;
                return;
            }
            p.sendMessage("$p &7AutoReconnect botow &awlaczony&7!");
            p.botOptions.autoReconnect=true;
        }
        else{
            if (p.playerOptions.autoReconnect) {
                p.sendMessage("$p &7AutoReconnect playera&c wylaczony&7!");
                p.playerOptions.autoReconnect=false;
                return;
            }
            p.sendMessage("$p &7AutoReconnect playera &awlaczony&7!");
            p.playerOptions.autoReconnect=true;
        }
    }
}