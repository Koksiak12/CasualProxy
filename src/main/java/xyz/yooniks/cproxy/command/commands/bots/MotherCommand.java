package xyz.yooniks.cproxy.command.commands.bots;

import xyz.yooniks.cproxy.command.Command;
import xyz.yooniks.cproxy.enums.Group;
import xyz.yooniks.cproxy.objects.Player;

public class MotherCommand extends Command {

    public MotherCommand() {
        super("mother", "Boty nasladuja twoje ruchy!", ",mother",
                Group.GRACZ, "follow", "botmother");
    }

    @Override
    public void onCommand(Player p, Command command, String[] args) {
        if (p.isMother()) {
            p.sendMessage("$p &cOd teraz boty nie nasladuja twoich ruchow!");
            p.setMother(false);
            return;
        }
        p.sendMessage("$p &aOd teraz boty nasladuja twoje ruchy!");
        p.setMother(true);
    }
}