package xyz.yooniks.cproxy.command.commands;

import xyz.yooniks.cproxy.command.Command;
import xyz.yooniks.cproxy.enums.Group;
import xyz.yooniks.cproxy.objects.Player;

public class StopCommand extends Command {

    public StopCommand() {
        super("stop", "Zastopuj petle!", ",stop [chatbot, crashbot, crash, macrobot, macro]",
                Group.GRACZ, "stopcrash","stopbot");
    }

    @Override
    public void onCommand(Player p, Command command, String[] args) {
        if (args.length <2 ){
            p.sendMessage("$p &7Poprawne uzycie: &a" + getUsage());
            return;
        }
        final String type = args[1];
        if (type.equalsIgnoreCase("chatbot")||type.equalsIgnoreCase("botchat")) {
            if (p.chatBotsSpamThread != null) {
                p.sendMessage("$p &cProba stopowania taska z chatem od botow..");
                p.chatBotsSpamThread.stop();
                return;
            }
            else{
                p.sendMessage("$p &cTaki thread nie istnieje!");
            }
        }
        else if (type.equalsIgnoreCase("crashbot")||type.equalsIgnoreCase("botcrash")) {
            if (p.crashBotsThread != null) {
                p.sendMessage("$p &cProba stopowania taska z crashem od botow..");
                p.crashBotsThread.stop();
                return;
            }
            else{
                p.sendMessage("$p &cTaki thread nie istnieje!");
            }
        }
        else if (type.equalsIgnoreCase("crash")||type.equalsIgnoreCase("crashplayer")) {
            if (p.crashPlayerThread != null) {
                p.sendMessage("$p &cProba stopowania taska z crashem od playera..");
                p.crashPlayerThread.stop();
                return;
            }
            else{
                p.sendMessage("$p &cTaki thread nie istnieje!");
            }
        }
        else if (type.equalsIgnoreCase("macrobot")||type.equalsIgnoreCase("botmacro")) {
            if (!p.stopMacroBot) { //to kiedys tez zedytuje xd
                p.sendMessage("$p &cProba stopowania taska z robieniem macra botow..");
                p.stopMacroBot=true;
                return;
            }
            else{
                p.sendMessage("$p &cTa opcja jest juz wlaczona!");
            }
        }
        else if (type.equalsIgnoreCase("macro")||type.equalsIgnoreCase("macroplayer")) {
            if (!p.stopMacroPlayer) {
                p.sendMessage("$p &cProba stopowania taska z robieniem macra playera.");
                p.stopMacroPlayer=true;
                return;
            }
            else{
                p.sendMessage("$p &cTa opcja jest juz wlaczona!");
            }
        }
        else{
            p.sendMessage("$p &cNiepoprawny typ! &7("+type+")");
        }
    }
}