package xyz.yooniks.cproxy.command.commands.settings;

import xyz.yooniks.cproxy.command.Command;
import xyz.yooniks.cproxy.enums.Group;
import xyz.yooniks.cproxy.objects.Player;

public class AutoCaptchaCommand extends Command {

    public AutoCaptchaCommand() {
        super("autocaptcha", "Wlacz/wylacz autocaptche", ",autocaptcha [bots/player]",
                Group.GRACZ, "botsautocaptcha","botautocaptcha");
    }

    @Override
    public void onCommand(Player p, Command command, String[] args) {
        if (args.length < 2){
            p.sendMessage("$p &7Poprawne uzycie: &a" + getUsage());
            return;
        }
        final String type = args[1];
        if (type.equalsIgnoreCase("bots")||type.equalsIgnoreCase("bot")) {
            if (p.botOptions.autoCaptcha) {
                p.sendMessage("$p &7AutoCaptcha botow &cwylaczona&7!");
                p.botOptions.autoCaptcha=false;
                return;
            }
            p.sendMessage("$p &7AutoCaptcha botow &awlaczona&7!");
            p.botOptions.autoCaptcha=true;
        }
        else{
            if (p.playerOptions.autoCaptcha) {
                p.sendMessage("$p &7AutoCaptcha playera&c wylaczona&7!");
                p.playerOptions.autoCaptcha=false;
                return;
            }
            p.sendMessage("$p &7AutoCaptcha playera &awlaczona&7!");
            p.playerOptions.autoCaptcha=true;
        }
    }
}