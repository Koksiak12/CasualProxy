package xyz.yooniks.cproxy.command.commands.bots;

import org.spacehq.mc.protocol.packet.ingame.client.ClientChatPacket;
import xyz.yooniks.cproxy.command.Command;
import xyz.yooniks.cproxy.enums.Group;
import xyz.yooniks.cproxy.objects.Bot;
import xyz.yooniks.cproxy.objects.Player;

public class BotChatCommand extends Command {

    public BotChatCommand() {
        super("botchat", "Boty pisza na czacie!", ",botchat [nick bota/all] [infinite-true/false] [czas w sek] [wiadomosc]",
                Group.GRACZ, "botczat", "chatbot");
    }

    @Override
    public void onCommand(Player p, Command command, String[] args) {
        if (args.length < 5) {
            p.sendMessage("$p &7Poprawne uzycie: &a" + getUsage());
            return;
        }
        if (p.getBots().size() < 1) {
            p.sendMessage("$p &cNie masz zadnych botow!");
            return;
        }
        final Boolean infinite = Boolean.parseBoolean(args[2]);
        final Integer seconds = Integer.parseInt(args[3]);
        final StringBuilder builder = new StringBuilder();
        builder.append(args[4]);
        for (int i = 5; i < args.length; i++)
            builder.append(" " + args[i]);
        final String nickBot = args[1];
        if (nickBot.equalsIgnoreCase("all") || nickBot.contains("all")) {
            if (infinite) {
                final Thread t = new Thread(() -> {
                        while (true) {
                            if (p.getBots().size() == 0) {
                                Thread.currentThread().stop();
                                break;
                            }
                            for (Bot bot : p.getBots())
                                bot.getSession().send(new ClientChatPacket(builder.toString()));
                            try{
                                Thread.sleep(1000L*seconds);
                            }
                            catch (InterruptedException e){

                            }
                        }
                });
                t.start();
                p.chatBotsSpamThread = t;
            } else {
                final Thread t = new Thread(() -> {
                    if (p.getBots().size() == 0) {
                        Thread.currentThread().stop();
                        return;
                    }
                    for (Bot bot : p.getBots())
                        bot.getSession().send(new ClientChatPacket(builder.toString()));
                    try {
                        Thread.sleep(1000L * seconds);
                    } catch (InterruptedException e) {

                    }
                });
                t.start();
                p.chatBotsSpamThread = t;
            }
        } else {
            boolean exists = false;
            for (Bot bot : p.getBots()) {
                if (bot.getName().equalsIgnoreCase(nickBot)) {
                    exists = true;
                    bot.getSession().send(new ClientChatPacket(builder.toString()));
                }
            }
            if (!exists)
                p.sendMessage("$p &cBot o nazwie: &7" + nickBot + "&c nie istnieje!");
        }
    }
}