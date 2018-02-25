package xyz.yooniks.cproxy.command.commands.bots;

import xyz.yooniks.cproxy.command.Command;
import xyz.yooniks.cproxy.enums.Group;
import xyz.yooniks.cproxy.objects.Player;

public class ChatFromBotCommand extends Command {

    public ChatFromBotCommand() {
        super("chatfrombots", "Otrzymuj wiadomosci z chatu botow!", ",chatfrombots",
                Group.GRACZ, "chatfrombot");
    }

    @Override
    public void onCommand(Player p, Command command, String[] args) {
        if (p.playerOptions.chatFromBots) {
            p.sendMessage("$p &7Od teraz &cnie przyjmujesz&7 wiadomosci z &achatu botow&7!");
            p.playerOptions.chatFromBots = false;
            return;
        }
        p.sendMessage("$p &7Od teraz &aprzyjmujesz&7 wiadomosci z &achatu botow&7!");
        p.playerOptions.chatFromBots = true;
    }
}