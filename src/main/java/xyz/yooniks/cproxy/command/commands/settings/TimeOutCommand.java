package xyz.yooniks.cproxy.command.commands.settings;

import xyz.yooniks.cproxy.command.Command;
import xyz.yooniks.cproxy.enums.Group;
import xyz.yooniks.cproxy.objects.Player;

public class TimeOutCommand extends Command {

    public TimeOutCommand() {
        super("timeout", "Ustaw timeout polaczen!", ",timeout [player/bots] [ping/connect] [czas w sek]",
                Group.GRACZ, "connecttimeout", "settimeout");
    }

    @Override
    public void onCommand(Player p, Command command, String[] args) {
        if (args.length < 4){
            p.sendMessage("$p &7Poprawne uzycie: &a" + getUsage());
            return;
        }
        final String type = args[1];
        final String type2 = args[2];
        final Integer seconds = Integer.parseInt(args[3]);
        if (type.equalsIgnoreCase("player")||type.equalsIgnoreCase("gracz")){
            if (type2.equalsIgnoreCase("ping")){
                p.playerOptions.timeOutPing=seconds;
                p.sendMessage("$p &7Ustawiono timeout pingu na: &a"+p.playerOptions.timeOutPing);
            }
            else{
                p.playerOptions.timeOutConnect=seconds;
                p.sendMessage("$p &7Ustawiono timeout connecta na: &a"+p.playerOptions.timeOutConnect);
            }
        }
        else{
            if (type2.equalsIgnoreCase("ping")){
                p.botOptions.timeOutPing=seconds;
                p.sendMessage("$p &7Ustawiono timeout pingu (botow) na: &a"+p.botOptions.timeOutPing);
            }
            else{
                p.botOptions.timeOutConnect=seconds;
                p.sendMessage("$p &7Ustawiono timeout connecta (botow) na: &a"+p.botOptions.timeOutConnect);
            }
        }
    }
}