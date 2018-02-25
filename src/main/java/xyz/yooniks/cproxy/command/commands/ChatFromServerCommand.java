package xyz.yooniks.cproxy.command.commands;

import xyz.yooniks.cproxy.command.Command;
import xyz.yooniks.cproxy.enums.Group;
import xyz.yooniks.cproxy.objects.Player;

public class ChatFromServerCommand extends Command {

    public ChatFromServerCommand() {
        super("chatfromserver", "Wlacz/wylacz otrzymywanie wiadomosci od serwera!", ",chatfromserver",
                Group.GRACZ, "chatfromservers");
    }

    @Override
    public void onCommand(Player p, Command command, String[] args) {
        if (p.playerOptions.chatFromServer) {
            p.sendMessage("$p &7Od teraz &cnie przyjmujesz&7 wiadomosci z &aserwera&7!");
            p.playerOptions.chatFromServer = false;
            return;
        }
        p.sendMessage("$p &7Od teraz &aprzyjmujesz&7 wiadomosci z &aserwera&7!");
        p.playerOptions.chatFromServer = true;
    }
}