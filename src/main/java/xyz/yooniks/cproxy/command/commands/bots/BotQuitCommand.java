package xyz.yooniks.cproxy.command.commands.bots;

import xyz.yooniks.cproxy.command.Command;
import xyz.yooniks.cproxy.enums.Group;
import xyz.yooniks.cproxy.objects.Bot;
import xyz.yooniks.cproxy.objects.Player;

public class BotQuitCommand extends Command {

    public BotQuitCommand() {
        super("botquit", "Boty wychodza z serwerow!", ",botquit [all/nick bota]",
                Group.GRACZ, "botsquit", "bquit", "quitbot", "quitbots");
    }

    @Override
    public void onCommand(Player p, Command command, String[] args) {
        if (args.length < 2) {
            p.sendMessage("$p &7Poprawne uzycie: &a" + getUsage());
            return;
        }
        if (p.getBots().size() < 1) {
            p.sendMessage("$p &cNie masz zadnych botow!");
            return;
        }
        final String nickBot = args[1];
        if (nickBot.equalsIgnoreCase("all")) {
            for (Bot bot : p.getBots())
                bot.getSession().disconnect("Rozlaczono za pomoca komendy");
        } else {
            boolean exists = false;
            for (Bot bot : p.getBots()) {
                if (bot.getName().equalsIgnoreCase(nickBot)) {
                    exists = true;
                    bot.getSession().disconnect("Rozlaczono za pomoca komendy");
                }
            }
            if (!exists)
                p.sendMessage("$p &cBot o nazwie: &7" + nickBot + "&c nie istnieje!");
        }
    }
}