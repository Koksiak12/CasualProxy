package xyz.yooniks.cproxy.command.commands.bots;

import xyz.yooniks.cproxy.command.Command;
import xyz.yooniks.cproxy.enums.Group;
import xyz.yooniks.cproxy.objects.Player;

public class MessagesBotCommand extends Command {

    public MessagesBotCommand() {
        super("messagesbot", "Wlacz/wylacz wiadomosci o polaczeniu botow", ",messagesbot [quit/join]",
                Group.GRACZ, "botmessages");
    }

    @Override
    public void onCommand(Player p, Command command, String[] args) {
        if (args.length < 2){
            p.sendMessage("$p &7Poprawne uzycie: &a" + getUsage());
            return;
        }
        final String type = args[1];
        if (type.equalsIgnoreCase("quit")||type.equalsIgnoreCase("leave")) {
            if (p.botOptions.quit) {
                p.sendMessage("$p &7Wiadomosci o wyjsciu z serwera przez boty: &cwylaczone&7!");
                p.botOptions.quit=false;
                return;
            }
            p.sendMessage("$p &7Wiadomosci o wyjsciu z serwera przez boty: &awlaczone&7!");
            p.botOptions.quit=true;
        }
        else{
            if (p.botOptions.join) {
                p.sendMessage("$p &7Wiadomosci o dolaczeniu do serwera przez boty: &cwylaczone&7!");
                p.botOptions.quit=false;
                return;
            }
            p.sendMessage("$p &7Wiadomosci o dolaczeniu do serwera przez boty: &awlaczone&7!");
            p.botOptions.join=true;
        }
    }
}